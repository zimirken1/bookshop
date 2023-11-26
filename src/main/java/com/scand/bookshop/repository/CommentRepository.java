package com.scand.bookshop.repository;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBook(Book book);
    Optional<Comment> findByUuid(String uuid);
}
