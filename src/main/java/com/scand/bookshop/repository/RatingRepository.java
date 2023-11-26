package com.scand.bookshop.repository;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Rating;
import com.scand.bookshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByBookAndUser(Book book, User user);
}
