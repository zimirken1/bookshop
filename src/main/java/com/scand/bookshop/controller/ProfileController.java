package com.scand.bookshop.controller;

import com.scand.bookshop.dto.BookRequestDTO;
import com.scand.bookshop.dto.ProfileCredentialsDTO;
import com.scand.bookshop.dto.UserResponseDTO;
import com.scand.bookshop.facade.BookFacade;
import com.scand.bookshop.facade.ProfileFacade;
import com.scand.bookshop.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileFacade profileFacade;

    @GetMapping("/")
    public UserResponseDTO getCurrentUserProfile(@AuthenticationPrincipal UserDetailsImpl userPrincipal) {
        return profileFacade.getUserProfile(userPrincipal);
    }

    @PostMapping("/update")
    public void updateCredentials(@AuthenticationPrincipal UserDetailsImpl userPrincipal,
                                  @Valid @RequestBody ProfileCredentialsDTO updatedCredentials) {
        profileFacade.updateCredentials(userPrincipal, updatedCredentials);
    }

    @GetMapping("/avatar")
    @ResponseBody
    public ResponseEntity<Resource> getAvatar(@AuthenticationPrincipal UserDetailsImpl userPrincipal) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(profileFacade.getAvatar(userPrincipal));
    }

    @PostMapping(value = "/avatar/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadAvatar(@AuthenticationPrincipal UserDetailsImpl userPrincipal,
                             @RequestParam("avatar") @NotNull MultipartFile file) {
        profileFacade.uploadAvatar(userPrincipal, file);
    }
}
