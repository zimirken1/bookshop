package com.scand.bookshop.userservicetests;

import com.scand.bookshop.BaseTest;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.entity.User.Role;
import com.scand.bookshop.service.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTests extends BaseTest {
    @Autowired
    UserService userService;

    @Autowired
    RegistrationService registrationService;

    @MockBean
    FileService fileService;

    private User user;

    @BeforeAll
    private void registerUser() {
        user = registrationService.register("usertest",
                PasswordEncryptor.encryptPassword("usertest"),
                "test@test.com",
                LocalDateTime.now(),
                User.Role.USER);
    }

    @Test
    public void updateCredentials_shouldUpdateCredentials() {
        String newEmail = "new@test.com";
        userService.updateCredentials(user, newEmail, "usertest");
        Optional<User> updatedUser = userService.findUserById(user.getId());
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getEmail()).isEqualTo(newEmail);
    }

    @Test
    public void uploadAvatar_shouldUpdateAvatar() {
        byte[] content = "This is the content of the picture.".getBytes();
        String extension = "jpg";
        userService.uploadAvatar(user, content, extension);
        Optional<User> updatedUser = userService.findUserById(user.getId());
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getAvatar()).isNotNull();
        assertThat(updatedUser.get().getAvatar()).isEqualTo("uploads/avatar/" + user.getUuid() + ".jpg");
    }

    @Test
    public void ban() {
        createAdmin(registrationService,"adminUserS", "adminUserS@mail.ru");
        userService.ban(userService.findUserByUsername("adminUserS").get());
        assertThat(userService.findUserByUsername("adminUserS").get().getRole()).isEqualTo(Role.BANNED);
        userService.ban(userService.findUserByUsername("adminUserS").get());
        assertThat(userService.findUserByUsername("adminUserS").get().getRole()).isEqualTo(Role.USER);
    }
}
