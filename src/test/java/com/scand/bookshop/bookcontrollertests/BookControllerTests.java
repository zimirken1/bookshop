package com.scand.bookshop.bookcontrollertests;

import com.scand.bookshop.BaseTest;
import com.scand.bookshop.dto.BookRequestDTO;
import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.dto.RatingRequestDTO;
import com.scand.bookshop.dto.RatingResponseDTO;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Rating;
import com.scand.bookshop.repository.BookRepository;
import com.scand.bookshop.repository.RatingRepository;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookControllerTests extends BaseTest {
    private String jwtToken;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private RatingRepository ratingRepository;

    @BeforeAll
    void createAdmin() {
        createAdmin(registrationService, "admin", "admin@mail.ru");
    }

    @BeforeEach
    public void authenticate() {
        jwtToken = login(testRestTemplate, "admin", "admin");
    }

    @Test
    public void shouldUploadBook() {
        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                createEntityWithFile("src/test/resources/files/book1.pdf", jwtToken, "file");
        Objects.requireNonNull(requestEntity.getBody()).add("price", 3.0);
        ResponseEntity<BookResponseDTO> response =
                makePostRequestWithFile("/books/upload", requestEntity, BookResponseDTO.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUuid()).isNotNull();
        assertThat(bookRepository.findByUuid(response.getBody().getUuid())).isPresent();
    }

    @Test
    void shouldThrowExceptionForWrongExtension() {
        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                createEntityWithFile("src/test/resources/files/bg.jpg", jwtToken, "file");
        ResponseEntity<String> stringResponseEntity =
                makePostRequestWithFile("/books/upload", requestEntity, String.class);
        assertThat(stringResponseEntity.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldThrowExceptionForNoFile() {
        ResponseEntity<String> stringResponseEntity =
                makePostRequestWithToken(jwtToken, "/books/upload", null, String.class);
        assertThat(stringResponseEntity.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldUpdateBook() {
        Book book = new Book(null,
                "old title",
                "old genre",
                "old author",
                "old filepath",
                UUID.randomUUID().toString(),
                "description", 6.00);
        book = bookRepository.save(book);
        BookRequestDTO bookRequestDto = new BookRequestDTO("new title",
                "new genre",
                "new author",
                "new filepath", 6.00);
        ResponseEntity<BookResponseDTO> response =
                makePostRequestWithToken(jwtToken,
                        "/books/" + book.getUuid() + "/update",
                        bookRequestDto,
                        BookResponseDTO.class);
        Optional<Book> updatedBook = bookRepository.findByUuid(book.getUuid());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(updatedBook).isPresent();
        assertThat(updatedBook.get().getTitle()).isEqualTo(bookRequestDto.getTitle());
        assertThat(updatedBook.get().getAuthor()).isEqualTo(bookRequestDto.getAuthor());
        assertThat(updatedBook.get().getGenre()).isEqualTo(bookRequestDto.getGenre());
    }

    private Book createBook() {
        Book book = new Book(null,
                "old title",
                "old genre",
                "old author",
                "old filepath",
                UUID.randomUUID().toString(),
                "description", 6.00);
        return bookRepository.save(book);
    }

    @Test
    void shouldNotUpdateBookWithNotFullData() {
        Book book = createBook();
        BookRequestDTO bookRequestDto = new BookRequestDTO("new title",
                "new genre",
                null,
                null, 6.00);
        String uuid = book.getUuid();
        makePostRequestWithToken(jwtToken, "/books/" + uuid + "/update", bookRequestDto, String.class);
        Optional<Book> updatedBook = bookRepository.findByUuid(book.getUuid());
        assertThat(updatedBook).isPresent();
        assertThat(updatedBook.get().getTitle()).isEqualTo("old title");
        assertThat(updatedBook.get().getAuthor()).isEqualTo("old author");
    }

    @Test
    public void addRating_shouldAddRating() {
        Book book = createBook();
        RatingRequestDTO ratingRequestDTO = new RatingRequestDTO(5);
        String url = String.format("/books/%s/update-rating", book.getUuid());
        ResponseEntity<Void> response = makePostRequestWithToken(jwtToken, url, ratingRequestDTO, Void.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(ratingRepository.findByBookAndUser(book, userService.findUserByUsername("admin").get())).isPresent();
    }

    @Test
    public void addRating_shouldUpdateOldRating() {
        Book book = createBook();
        RatingRequestDTO ratingRequestDTO = new RatingRequestDTO(5);
        String url = String.format("/books/%s/update-rating", book.getUuid());
        makePostRequestWithToken(jwtToken, url, ratingRequestDTO, Void.class);
        ResponseEntity<Void> response =
                makePostRequestWithToken(jwtToken, url, new RatingRequestDTO(4), Void.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(ratingRepository.findByBookAndUser(book, userService.findUserByUsername("admin").get()).get().getRatingValue()).isEqualTo((4));
    }

    @Test
    public void getAverageRating_shouldReturnAverageRating() {
        createAdmin(registrationService, "admin22", "admin22@mail.ru");
        Book book = createBook();
        Rating rating = new Rating(null,book,userService.findUserByUsername("admin22").get(),5);
        ratingRepository.save(rating);
        Rating rating2 = new Rating(null,book,userService.findUserByUsername("admin").get(),4);
        ratingRepository.save(rating2);
        String url = String.format("/books/%s/rating", book.getUuid());
        ResponseEntity<RatingResponseDTO> response = makeGetRequestWithToken(jwtToken, url, RatingResponseDTO.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).getRating()).isEqualTo(4.5);
        assertThat(ratingRepository.findByBookAndUser(book, userService.findUserByUsername("admin").get()).get().getRatingValue()).isEqualTo((4));
    }
}
