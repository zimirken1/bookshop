package com.scand.bookshop.registrationservicetests;

import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.UserRepository;
import com.scand.bookshop.service.PasswordEncryptor;
import com.scand.bookshop.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RegistrationServiceTests {
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void register_shouldRegisterUser() {
        String username = "testuser";
        String password = PasswordEncryptor.encryptPassword("usertest");
        String email = "tests@test.com";
        User.Role role = User.Role.USER;
        User user = registrationService.register(username,
                PasswordEncryptor.encryptPassword(password),
                email,
                LocalDateTime.now(),
                role);
        Optional<User> newUser = userRepository.findById(user.getId());
        assertAll(
                () -> assertThat(newUser).isPresent(),
                () -> assertThat(newUser.get().getLogin()).isEqualTo(username),
                () -> assertThat(newUser.get().getEmail()).isEqualTo(email)
        );
    }
}
