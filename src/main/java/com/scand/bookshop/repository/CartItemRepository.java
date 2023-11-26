package com.scand.bookshop.repository;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Cart;
import com.scand.bookshop.entity.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  Optional<CartItem> findByCartAndBook(Cart cart, Book book);
}
