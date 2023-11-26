package com.scand.bookshop.controller;

import com.scand.bookshop.dto.BanRequestDTO;
import com.scand.bookshop.dto.UserPageResponseDTO;
import com.scand.bookshop.facade.AdminFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("admin")
public class AdminController {

  private final AdminFacade adminFacade;

  @GetMapping("/list")
  public UserPageResponseDTO getUsersPage(@RequestParam int page,
                                          @RequestParam int size,
                                          @RequestParam(required = false) String searchTerm) {
    return adminFacade.getUsersPage(page, size, searchTerm);
  }

  @PostMapping("/ban")
  public void ban(@RequestBody @Valid BanRequestDTO banRequestDTO) {
    adminFacade.ban(banRequestDTO);
  }

}
