package com.scand.bookshop.security.service;

import com.scand.bookshop.entity.User;
import com.scand.bookshop.entity.User.Role;
import com.scand.bookshop.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;
  private final MessageSource messageSource;
  private final HttpServletRequest request;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByLogin(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException(messageSource.getMessage(
          "user_not_found", null, request.getLocale()) + ": " + username);
    }
    if (user.get().getRole().equals(Role.BANNED)) {
      throw new LockedException("User is blocked");
    }
    if (!user.get().isActive()) {
      throw new LockedException("This account is not activated yet. Check your email.");
    }
    return UserDetailsImpl.build(user.get());
  }

}
