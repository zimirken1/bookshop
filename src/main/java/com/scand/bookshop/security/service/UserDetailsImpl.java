package com.scand.bookshop.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scand.bookshop.entity.User;
import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {

  @Serial
  private static final long serialVersionUID = 1L;

  private final Long id;

  private final String username;

  private final String email;

  @JsonIgnore
  private final String password;

  private final Collection<? extends GrantedAuthority> authorities;

  public static UserDetailsImpl build(User user) {
    User.Role role = user.getRole();
    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    return new UserDetailsImpl(user.getId(),
        user.getLogin(),
        user.getEmail(),
        user.getPassword(),
        authorities);
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }
}
