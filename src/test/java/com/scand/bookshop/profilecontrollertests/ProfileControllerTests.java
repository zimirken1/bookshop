package com.scand.bookshop.profilecontrollertests;

import com.scand.bookshop.BaseTest;
import com.scand.bookshop.dto.ProfileCredentialsDTO;
import com.scand.bookshop.dto.UserResponseDTO;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.UserRepository;
import com.scand.bookshop.service.RegistrationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileControllerTests extends BaseTest {
    private String jwtToken;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeAll
    private void createAdmin() {
        createAdmin(registrationService, "adminProfile", "adminprof@mail.ru");
    }

    @BeforeEach
    void login() {
        jwtToken = login(testRestTemplate, "adminProfile", "admin");
    }

    @Test
    public void getProfile_shouldReturnProfileInfo() {
        ResponseEntity<UserResponseDTO> response =
                makeGetRequestWithToken(jwtToken, "/profile/", UserResponseDTO.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isNotNull();
        assertThat(response.getBody().getEmail()).isNotNull();
        assertThat(response.getBody().getRegDate()).isNotNull();
    }

    @Test
    public void updateCredentials_shouldUpdateCredentials() {
        String newEmail = "newEmail@mail.ru";
        ProfileCredentialsDTO newCredentials = new ProfileCredentialsDTO("admin", newEmail);
        ResponseEntity<Void> response =
                makePostRequestWithToken(jwtToken, "/profile/update", newCredentials, Void.class);
        Optional<User> updatedUser = userRepository.findByLogin("adminProfile");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getEmail()).isEqualTo(newEmail);
    }

    @Test
    public void updateCredentials_shouldNotUpdateCredentialsWithNotValidData() {
        String newEmail = "mail.ru";
        ProfileCredentialsDTO newCredentials = new ProfileCredentialsDTO("admin", newEmail);
        ResponseEntity<String> response =
                makePostRequestWithToken(jwtToken, "/profile/update", newCredentials, String.class);
        Optional<User> updatedUser = userRepository.findByLogin("adminProfile");
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getEmail()).isNotEqualTo(newEmail);
    }

    @Test
    public void uploadAvatar() {
        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                createEntityWithFile("src/test/resources/files/bg.jpg", jwtToken, "avatar");
        makePostRequestWithFile("/profile/avatar/upload", requestEntity, Void.class);
        Optional<User> updatedUser = userRepository.findByLogin("adminProfile");
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getAvatar()).isEqualTo("uploads/avatar/" + updatedUser.get().getUuid() + ".jpg");
    }

    @Test
    public void uploadAvatar_shouldNotUpdateWithBadFile() {
        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                createEntityWithFile("src/test/resources/files/book1.pdf", jwtToken, "avatar");
        ResponseEntity<String> response = makePostRequestWithFile("/profile/avatar/upload", requestEntity, String.class);
        Optional<User> updatedUser = userRepository.findByLogin("adminProfile");
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getAvatar()).isNotEqualTo("uploads/avatar/" + updatedUser.get().getUuid() + ".pdf");
    }
}

