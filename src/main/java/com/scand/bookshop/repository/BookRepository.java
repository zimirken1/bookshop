package com.scand.bookshop.repository;

import com.scand.bookshop.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByUuid(String uuid);
    Page<Book> findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String genre, String author, Pageable pageable);
    void deleteByUuid(String uuid);
}
