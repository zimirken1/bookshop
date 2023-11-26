package com.scand.bookshop.admincontrollertests;

import static org.assertj.core.api.Assertions.assertThat;

import com.scand.bookshop.BaseTest;
import com.scand.bookshop.dto.BanRequestDTO;
import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.entity.User.Role;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminControllerTests extends BaseTest {
  private String jwtToken;

  @Autowired
  private RegistrationService registrationService;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private UserService userService;

  @BeforeAll
  void createAdmin() {
    createAdmin(registrationService, "MainAdmin", "adminm@mail.ru");
    createAdmin(registrationService, "TestAdmin", "adminTT@mail.ru");
  }

  @BeforeEach
  public void authenticate() {
    jwtToken = login(testRestTemplate, "MainAdmin", "admin");
  }

  @Test
  public void ban() {
    BanRequestDTO requestDTO =
        new BanRequestDTO(userService.findUserByUsername("TestAdmin").get().getUuid());
    ResponseEntity<String> response =
        makePostRequestWithToken(jwtToken,"/admin/ban", requestDTO, String.class);
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(userService.findUserByUsername("TestAdmin").get().getRole()).isEqualTo(Role.BANNED);
    response =
        makePostRequestWithToken(jwtToken,"/admin/ban", requestDTO, String.class);
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(userService.findUserByUsername("TestAdmin").get().getRole()).isEqualTo(Role.USER);
  }

}
