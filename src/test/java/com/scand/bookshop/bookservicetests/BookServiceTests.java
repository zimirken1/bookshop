package com.scand.bookshop.bookservicetests;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.service.BookCoverService;
import com.scand.bookshop.service.BookService;
import com.scand.bookshop.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BookServiceTests {

    @Autowired
    BookService bookService;

    @MockBean
    FileService fileService;

    @MockBean
    BookCoverService bookCoverService;

    @Test
    public void shouldCreateBook() {
        String title = "The Book";
        String author = "John Doe";
        String subject = "Computer Science";
        String extension = "pdf";
        byte[] content = "This is the content of the book.".getBytes();
        Book book = bookService.createBook(title, author, subject, extension, content, 6.0);
        assertAll(
                () -> assertThat(book).isNotNull(),
                () -> assertThat(book.getTitle()).isEqualTo(title),
                () -> assertThat(book.getAuthor()).isEqualTo(author),
                () -> assertThat(book.getGenre()).isEqualTo(subject)
        );
    }

    @Test
    public void createBook_shouldNotSaveBookIfIOException() {
        String title = "The Book";
        String author = "John Doe";
        String subject = "Computer Science";
        String extension = "pdf";
        byte[] content = "This is the content of the book.".getBytes();
        doThrow(new RuntimeException("Error")).when(fileService).writeFile(any(), any());
        Book book = null;
        try {
            book = bookService.createBook(title, author, subject, extension, content, 6.0);
        } catch (RuntimeException e) {
            //
        }
        assertThat(book).isNull();
    }

    @Test
    public void shouldRollbackIfBookCreationFails() {
        String author = "John Doe";
        String subject = "Computer Science";
        String extension = "pdf";
        byte[] content = "This is the content of the book.".getBytes();
        Book book = null;
        try {
            book = bookService.createBook(null, author, subject, extension, content, 6.0);
        } catch (Exception e) {
            // expected
        }
        assertThat(book).isNull();
    }

    @Test
    public void shouldUpdateBook() {
        String title = "The Book";
        String author = "John Doe";
        String subject = "Computer Science";
        String extension = "pdf";
        byte[] content = "This is the content of the book.".getBytes();
        Book book = bookService.createBook(title, author, subject, extension, content, 6.0);
        String newTitle = "The New Book";
        String newAuthor = "Jane Doe";
        book = bookService.updateBook(book, newTitle, "new genre", newAuthor, "desc", 6.0);
        Optional<Book> newBook = bookService.findBookByUuid(book.getUuid());
        assertThat(newBook).isPresent();
        assertThat(newBook.get().getTitle()).isEqualTo(newTitle);
        assertThat(book.getAuthor()).isEqualTo(newAuthor);
    }
}