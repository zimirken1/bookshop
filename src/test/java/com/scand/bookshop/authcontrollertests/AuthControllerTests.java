package com.scand.bookshop.authcontrollertests;

import com.scand.bookshop.BaseTest;
import com.scand.bookshop.dto.UserLoginDTO;
import com.scand.bookshop.dto.UserRegistrationDTO;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.UserRepository;
import com.scand.bookshop.security.jwt.JwtResponse;
import com.scand.bookshop.service.PasswordEncryptor;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.utility.ServerMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTests extends BaseTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationService registrationService;

    @Test
    public void register_shouldRegisterUser() {
        UserRegistrationDTO regDto = new UserRegistrationDTO("usertest",
                "usertest",
                "usertest@test.com");
        ResponseEntity<String> response =
                makePostRequestWithToken(null, "/auth/register", regDto, String.class);
        Optional<User> user = userRepository.findByLogin("usertest");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(user).isPresent();
        assertThat(user.get().getLogin()).isEqualTo("usertest");
    }

    @Test
    public void register_shouldNotRegisterWithInvalidData() {
        UserRegistrationDTO regDto = new UserRegistrationDTO("usertest123",
                "s",
                "usertest123.com");
        makePostRequestWithToken(null, "/auth/register", regDto, String.class);
        Optional<User> user = userRepository.findByLogin("usertest123");
        assertThat(user).isNotPresent();
    }

    @Test
    public void login_shouldLogInUser() {
        registrationService.register("usertestlogin",
                PasswordEncryptor.encryptPassword("usertestlogin"),
                "userlogin@user.com",
                LocalDateTime.now(),
                User.Role.USER);
        UserLoginDTO userLoginDTO = new UserLoginDTO("usertestlogin", "usertestlogin");
        ResponseEntity<JwtResponse> response =
                makePostRequestWithToken(null,
                        "/auth/signin",
                        userLoginDTO,
                        JwtResponse.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void login_shouldNotLogInWithWrongCredentials() {
        registrationService.register("usertes1t",
                PasswordEncryptor.encryptPassword("usertes1t"),
                "user1@user.com",
                LocalDateTime.now(),
                User.Role.USER);
        UserLoginDTO userLoginDTO = new UserLoginDTO("usertes1t", "123123");
        ResponseEntity<String> response =
                makePostRequestWithToken(null, "/auth/signin", userLoginDTO, String.class);
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }
}
