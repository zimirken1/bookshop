package com.scand.bookshop.service;

import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {

  private final UserRepository userRepository;

  public User register(String username, String hashPassword, String email, LocalDateTime regDate,
                       User.Role role) {
    log.info("Starting registration process for user: " + username);
    User newUser = userRepository.save(new User(
        null,
        java.util.UUID.randomUUID(),
        username,
        hashPassword,
        email,
        regDate,
        role,
        null,
        false,
        UUID.randomUUID().toString()));
    log.info("User: " + username + " registered successfully.");
    return newUser;
  }

  @Transactional
  public void activateAccount(User user) {
    user = userRepository.getReferenceById(user.getId());
    user.setActive(true);
    user.setActivationCode(null);
  }
}
