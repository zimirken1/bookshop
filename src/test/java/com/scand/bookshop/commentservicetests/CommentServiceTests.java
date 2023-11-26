package com.scand.bookshop.commentservicetests;

import com.scand.bookshop.BaseTest;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Comment;
import com.scand.bookshop.repository.BookRepository;
import com.scand.bookshop.repository.CommentRepository;
import com.scand.bookshop.repository.UserRepository;
import com.scand.bookshop.service.CommentService;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentServiceTests extends BaseTest {
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    private Book createBook() {
        book = new Book(null,
                "testService",
                "test",
                "test",
                "testpath",
                UUID.randomUUID().toString(),
                "desc", 6.0);
        return bookRepository.save(book);
    }

    @BeforeAll
    private void setUp() {
        createAdmin(registrationService, "adminCommentService", "admin@mail.ru");
        book = createBook();
    }

    @Test
    public void add_shouldAddComment() {
        commentService.add("test",
                book,
                userRepository.findByLogin("adminCommentService").get(),
                null);
        assertThat(commentRepository.findByBook(book)).isNotEmpty();
    }

    @Test
    public void add_shouldAddReply() {
        Book testBook = createBook();
        commentService.add("test",
                testBook,
                userRepository.findByLogin("adminCommentService").get(),
                null);
        commentService.add("test2",
                testBook,
                userRepository.findByLogin("adminCommentService").get(),
                commentRepository.findByBook(book).get(0).getUuid());
        assertThat(commentRepository.findByBook(book)).isNotEmpty();
        assertThat(commentRepository.findByBook(book).get(1).getParentComment()).isNotNull();
    }

    @Test
    public void updateComment_shouldUpdateComment() {
        Comment comment = new Comment(null,
                null,
                null,
                "created",
                book,
                userService.findUserByUsername("adminCommentService").get(),
                LocalDateTime.now(),
                UUID.randomUUID().toString(),
                false);
        comment = commentRepository.save(comment);
        commentService.updateComment(comment, "testUpdate");
        assertThat(commentService.getCommentByUuid(comment.getUuid()).getText()).isEqualTo("testUpdate");
    }

    @Test
    public void deleteComment_shouldDeleteComment() {
        Comment comment = new Comment(null,
                null,
                new ArrayList<Comment>(),
                "created",
                book,
                userService.findUserByUsername("adminCommentService").get(),
                LocalDateTime.now(),
                UUID.randomUUID().toString(),
                false);
        comment = commentRepository.save(comment);
        commentService.deleteComment(comment);
        assertThat(commentService.findCommentByUuid(comment.getUuid())).isNotPresent();
    }
}
