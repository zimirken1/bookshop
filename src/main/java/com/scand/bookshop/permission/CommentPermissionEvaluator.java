package com.scand.bookshop.permission;

import com.scand.bookshop.entity.Comment;
import com.scand.bookshop.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component("commentPermissionEvaluator")
@RequiredArgsConstructor
public class CommentPermissionEvaluator implements PermissionEvaluator {
    private final CommentRepository commentRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    public boolean hasPermission(Authentication authentication, String commentId) {
        String currentUsername = authentication.getName();
        Comment comment = commentRepository.findByUuid(commentId).orElse(null);
        return comment != null && comment.getUser().getLogin().equals(currentUsername);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}