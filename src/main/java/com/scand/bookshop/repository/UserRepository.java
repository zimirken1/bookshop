package com.scand.bookshop.repository;

import com.scand.bookshop.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUuid(UUID uuid);

  Optional<User> findByEmail(String email);

  Optional<User> findByLogin(String username);

  Optional<User> findByActivationCode(String activationCode);

  Page<User> findByLoginContainingIgnoreCaseOrEmailContainingIgnoreCase(String login,
                                                                        String email,
                                                                        Pageable pageable);
}