package com.scand.bookshop.facade;

import com.scand.bookshop.dto.CommentRequestDTO;
import com.scand.bookshop.dto.CommentResponseDTO;
import com.scand.bookshop.dto.DTOConverter;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Comment;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.security.service.UserDetailsImpl;
import com.scand.bookshop.service.BookService;
import com.scand.bookshop.service.CommentService;
import com.scand.bookshop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentFacade {
    private final UserService userService;
    private final CommentService commentService;
    private final BookService bookService;
    private final MessageSource messageSource;
    private final HttpServletRequest request;

    public void addComment(CommentRequestDTO comment, UserDetailsImpl userPrincipal) {
        User user = userService.getUserById(userPrincipal.getId());
        Book book = bookService.getBookByUuid(comment.getBookUuid());
        commentService.add(comment.getText(), book, user, comment.getParentUuid());
    }

    public List<CommentResponseDTO> getComments(String uuid) {
        Book book = bookService.getBookByUuid(uuid);
        return commentService.getComments(book).stream()
                .map(DTOConverter::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteComment(String uuid, UserDetailsImpl userPrincipal) {
        Comment comment = commentService.getCommentByUuid(uuid);
        commentService.deleteComment(comment);
    }

    public void updateComment(String uuid, String newText, UserDetailsImpl userPrincipal) {
        Comment comment = commentService.getCommentByUuid(uuid);
        commentService.updateComment(comment, newText);
    }
}
