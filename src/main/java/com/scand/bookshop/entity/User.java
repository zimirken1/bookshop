package com.scand.bookshop.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "uuid", nullable = false, unique = true)
  private UUID uuid;

  @Column(name = "login", nullable = false, unique = true)
  private String login;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "registration_date", nullable = false)
  private LocalDateTime registrationDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private Role role;

  @Column(name = "avatar")
  private String avatar;

  public enum Role {
    ADMIN,
    USER,
    GUEST,
    BANNED
  }

  @Column(name = "is_active")
  private boolean isActive = false;

  @Column(name = "activation_code")
  private String activationCode;
}