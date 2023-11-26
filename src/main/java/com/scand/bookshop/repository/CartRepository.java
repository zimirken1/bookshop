package com.scand.bookshop.repository;

import com.scand.bookshop.entity.Cart;
import com.scand.bookshop.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
  Optional<Cart> findByUser(User user);
}
