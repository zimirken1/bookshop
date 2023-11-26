package com.scand.bookshop.ratingservicetests;

import com.scand.bookshop.BaseTest;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.BookRepository;
import com.scand.bookshop.repository.RatingRepository;
import com.scand.bookshop.repository.UserRepository;
import com.scand.bookshop.service.BookService;
import com.scand.bookshop.service.RatingService;
import com.scand.bookshop.service.RegistrationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RatingServiceTests extends BaseTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RatingService ratingService;

    private Book book;

    @BeforeAll
    private void setUp() {
        createAdmin(registrationService, "adminRating", "adminRat@mail.ru");
        book = new Book(null,
                "testRating",
                "test",
                "test",
                "testpathRating",
                UUID.randomUUID().toString(),
                "desc", 6.0);
        book = bookRepository.save(book);
    }

    @Test
    public void addRating_shouldAddRating() {
        User user = userRepository.findByLogin("adminRating").get();
        ratingService.addRating(book, user, 4);
        assertThat(ratingRepository.findByBookAndUser(book, user)).isPresent();
    }

    @Test
    public void calculateAverage_shouldReturnAverageRating() {
        User user = userRepository.findByLogin("adminRating").get();
        Book testBook = bookService.getBookByUuid(book.getUuid());
        ratingService.addRating(testBook, user, 3);
        assertThat(ratingService.calculateAverageRating(testBook)).isEqualTo(3.00);
    }

    @Test
    public void calculateAverage_shouldUpdateOldRating() {
        User user = userRepository.findByLogin("adminRating").get();
        Book testBook = bookRepository.findByUuid(book.getUuid()).get();
        ratingService.addRating(testBook, user, 3);
        ratingService.addRating(testBook, user, 4);
        assertThat(ratingService.calculateAverageRating(testBook)).isEqualTo(4.00);
    }
}
