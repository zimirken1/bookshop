package com.scand.bookshop.facade;

import com.scand.bookshop.dto.JwtDTO;
import com.scand.bookshop.dto.RefreshTokenDTO;
import com.scand.bookshop.dto.UserLoginDTO;
import com.scand.bookshop.dto.UserRegistrationDTO;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.exception.UserAlreadyExistsException;
import com.scand.bookshop.security.jwt.JwtResponse;
import com.scand.bookshop.service.CartService;
import com.scand.bookshop.service.EmailService;
import com.scand.bookshop.service.LogInService;
import com.scand.bookshop.service.PasswordEncryptor;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.service.UserService;
import com.scand.bookshop.utility.ServerMessage;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

  private final UserService userService;
  private final RegistrationService registrationService;
  private final LogInService logInService;
  private final MessageSource messageSource;
  private final HttpServletRequest request;
  private final CartService cartService;
  private final EmailService emailService;

  public ServerMessage register(UserRegistrationDTO userData) {
    if (userService.findUserByEmail(userData.getEmail()).isPresent()
        || userService.findUserByUsername(userData.getUsername()).isPresent()) {
      throw new UserAlreadyExistsException(messageSource.getMessage("user_exists",
          null,
          request.getLocale()));
    }
    User user = registrationService.register(userData.getUsername(),
        PasswordEncryptor.encryptPassword(userData.getPassword()),
        userData.getEmail(),
        LocalDateTime.now(),
        User.Role.USER);
    cartService.createCart(user);
    String message = String.format(
            "Welcome to our site. Please, visit next link to activate your account: " +
            "http://localhost:8054/auth/activate/%s", user.getActivationCode());
    emailService.sendEmail(user.getEmail(), "Activate account", message);
    return new ServerMessage("success",
        messageSource.getMessage("success_registration", null, request.getLocale()));
  }

  public JwtResponse authenticateUser(UserLoginDTO loginRequest) {
    return logInService.logIn(loginRequest.getUsername(), loginRequest.getPassword());
  }

  public JwtDTO refreshToken(RefreshTokenDTO refreshTokenDTO) {
    return new JwtDTO(logInService.refreshToken(refreshTokenDTO.getRefreshToken()));
  }

  public void activate(String activationCode) {
    registrationService.activateAccount(userService.getUserByActivationCode(activationCode));
  }
}
