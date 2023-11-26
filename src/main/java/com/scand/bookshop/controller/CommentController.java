package com.scand.bookshop.controller;

import com.scand.bookshop.dto.CommentRequestDTO;
import com.scand.bookshop.dto.CommentResponseDTO;
import com.scand.bookshop.dto.CommentUpdateDTO;
import com.scand.bookshop.facade.CommentFacade;
import com.scand.bookshop.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("comments")
public class CommentController {
    private final CommentFacade commentFacade;

    @PostMapping(value = "/add")
    public void addComment(@Valid @RequestBody CommentRequestDTO comment, @AuthenticationPrincipal UserDetailsImpl userPrincipal) {
        commentFacade.addComment(comment, userPrincipal);
    }

    @GetMapping(value = "/{uuid}")
    public List<CommentResponseDTO> getComments(@PathVariable String uuid) {
        return commentFacade.getComments(uuid);
    }

    @DeleteMapping(value = "/{uuid}")
    @PreAuthorize("hasAuthority('ADMIN') or @commentPermissionEvaluator.hasPermission(authentication, #uuid)")
    public void deleteComment(@PathVariable String uuid, @AuthenticationPrincipal UserDetailsImpl userPrincipal) {
        commentFacade.deleteComment(uuid, userPrincipal);
    }

    @PostMapping(value = "/{uuid}/update")
    @PreAuthorize("hasAuthority('ADMIN') or @commentPermissionEvaluator.hasPermission(authentication, #uuid)")
    public void updateComment(@PathVariable String uuid,
                              @AuthenticationPrincipal UserDetailsImpl userPrincipal,
                              @RequestBody CommentUpdateDTO newText) {

        commentFacade.updateComment(uuid, newText.getText(), userPrincipal);
    }
}
