package com.scand.bookshop.repository;

import com.scand.bookshop.entity.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

  Optional<Token> findByUserId(Long userId);
  Optional<Token> findByValue(String token);

}
