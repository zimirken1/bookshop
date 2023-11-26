package com.scand.bookshop.service;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Comment;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.CommentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final MessageSource messageSource;
    private final HttpServletRequest request;

    public void add(String text, Book book, User user, String parentUuid) {
        log.info("Adding comment for book with ID: " + book.getId() + " by user with ID: " + user.getId());
        Optional<Comment> parentComment = findCommentByUuid(parentUuid);
        Comment comment = new Comment(null,
                parentComment.orElse(null),
                new ArrayList<Comment>(),
                text,
                book,
                user,
                LocalDateTime.now(),
                UUID.randomUUID().toString(),
                false);
        commentRepository.save(comment);
        log.info("Comment saved with UUID: " + comment.getUuid());
    }

    public Optional<Comment> findCommentByUuid(String uuid) {
        return commentRepository.findByUuid(uuid);
    }

    public Comment getCommentByUuid(String uuid) {
        log.info("Fetching comment with UUID: " + uuid);
        return findCommentByUuid(uuid)
                .orElseThrow(() -> {
                    log.warn("Comment with UUID: " + uuid + " not found");
                    return new NoSuchElementException(messageSource.getMessage("comment_not_found", null, request.getLocale()));
                });
    }

    @Transactional
    public void deleteComment(Comment comment) {
        log.info("Deleting comment with ID: " + comment.getId() + " and UUID: " + comment.getUuid());
        if(comment.getReplies().isEmpty()) {
            commentRepository.delete(comment);
            log.info("Comment deleted");
        } else {
            comment.setRemoved(true);
            log.info("Comment marked as removed");
        }
    }

    @Transactional
    public void updateComment(Comment comment, String newText) {
        log.info("Updating comment with ID: " + comment.getId());
        comment = commentRepository.getReferenceById(comment.getId());
        comment.setText(newText);
        log.info("Comment updated");
    }

    public void clearComments(List<Comment> comments) {
        commentRepository.deleteAll(comments);
    }

    public List<Comment> getComments(Book book) {
        log.info("Fetching comments for book with ID: " + book.getId());
        return commentRepository.findByBook(book);
    }
}
