package com.scand.bookshop.service;

import com.scand.bookshop.entity.Token;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.TokenRepository;
import com.scand.bookshop.security.jwt.JwtResponse;
import com.scand.bookshop.security.jwt.JwtUtils;
import com.scand.bookshop.security.service.UserDetailsImpl;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogInService {

  private final JwtUtils jwtUtils;
  private final AuthenticationManager authenticationManager;
  private final TokenRepository tokenRepository;
  private final UserService userService;

  public JwtResponse logIn(String username, String password) {
    log.info("Attempting to authenticate user: " + username);
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(username, password));
    return generateToken(authentication);
  }

  private JwtResponse generateToken(Authentication authentication) {
    String jwt = jwtUtils.generateJwtToken(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String refresh = jwtUtils.generateRefreshToken(userDetails);
    log.info("Generated JWT for user: " + userDetails.getUsername());
    List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
    return new JwtResponse(jwt,
        refresh,
        userDetails.getId(),
        userDetails.getUsername(),
        userDetails.getEmail(),
        roles);
  }

  public String refreshToken(String token) {
    if (!jwtUtils.verifyRefreshToken(token)) {
      throw new LockedException("Refresh token expired or invalid");
    }
    Optional<Token> refreshToken = tokenRepository.findByValue(token);
    User user = userService.getUserById(refreshToken.get().getUserId());
    UserDetails userDetails = UserDetailsImpl.build(user);
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        userDetails, null, userDetails.getAuthorities());
    return jwtUtils.generateJwtToken(authentication);
  }
}
