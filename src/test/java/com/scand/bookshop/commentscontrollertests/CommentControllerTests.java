package com.scand.bookshop.commentscontrollertests;

import com.scand.bookshop.BaseTest;
import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.dto.CommentRequestDTO;
import com.scand.bookshop.dto.CommentUpdateDTO;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Comment;
import com.scand.bookshop.repository.BookRepository;
import com.scand.bookshop.repository.CommentRepository;
import com.scand.bookshop.service.BookService;
import com.scand.bookshop.service.CommentService;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.service.UserService;
import org.hibernate.sql.Update;
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

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentControllerTests extends BaseTest {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    private Book createBook() {
        book = new Book(null,
                "test",
                "test",
                "test",
                "testpath",
                UUID.randomUUID().toString(),
                "desc", 6.0);
        return bookRepository.save(book);
    }

    @BeforeAll
    private void setUp() {
        createAdmin(registrationService, "adminComment", "admin");
    }

    @BeforeEach
    private void createBookForEveryTest() {
        book = createBook();
    }

    private String jwtToken;

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;


    @BeforeEach
    private void login() {
        jwtToken = login(testRestTemplate, "adminComment", "admin");
    }

    @Test
    public void addComment_shouldCreateComment() {
        CommentRequestDTO requestDTO = new CommentRequestDTO("test", book.getUuid(), null);
        ResponseEntity<String> response = makePostRequestWithToken(jwtToken, "/comments/add", requestDTO, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(commentRepository.findByBook(book)).isNotNull();
    }

    @Test
    public void addComment_shouldCreateReply() {
        Book testBook = createBook();
        CommentRequestDTO requestDTO = new CommentRequestDTO("test", testBook.getUuid(), null);
        makePostRequestWithToken(jwtToken, "/comments/add", requestDTO, String.class);
        CommentRequestDTO replyDTO = new CommentRequestDTO("test2",
                testBook.getUuid(),
                commentRepository.findByBook(testBook).get(0).getUuid());
        ResponseEntity<String> response = makePostRequestWithToken(jwtToken, "/comments/add", replyDTO, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(commentRepository.findByBook(book).get(1).getParentComment()).isNotNull();
    }

    @Test
    public void addComment_shouldNotCreateCommentWithEmptyText() {
        CommentRequestDTO requestDTO = new CommentRequestDTO("", book.getUuid(),null);
        ResponseEntity<String> response = makePostRequestWithToken(jwtToken, "/comments/add", requestDTO, String.class);
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(commentRepository.findByBook(book)).isEmpty();
    }

    @Test
    public void updateComment_shouldUpdateComment() {
        Comment comment = new Comment(null,
                null,
                null,
                "created",
                book,
                userService.findUserByUsername("adminComment").get(),
                LocalDateTime.now(),
                UUID.randomUUID().toString(),
                false);
        comment = commentRepository.save(comment);
        CommentUpdateDTO requestDTO = new CommentUpdateDTO("updated");
        ResponseEntity<String> response =
                makePostRequestWithToken(jwtToken,
                        "/comments/" + comment.getUuid() + "/update",
                        requestDTO,
                        String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(commentRepository.findByUuid(comment.getUuid())).isNotEmpty();
        assertThat(commentRepository.findByUuid(comment.getUuid())).isPresent();
        assertThat(commentRepository.findByUuid(comment.getUuid()).get().getText()).isEqualTo("updated");
    }

    @Test
    public void deleteComment_shouldDeleteComment() {
        Comment comment = new Comment(null,
                null,
                null,
                "created",
                book,
                userService.findUserByUsername("adminComment").get(),
                LocalDateTime.now(),
                UUID.randomUUID().toString(),
                false);
        comment = commentRepository.save(comment);
        ResponseEntity<String> response = makeDeleteRequestWithToken(jwtToken,
                "/comments/" + comment.getUuid(),
                String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(commentService.findCommentByUuid(comment.getUuid())).isEmpty();
        assertThat(commentRepository.findByUuid(comment.getUuid())).isNotPresent();
    }
}
