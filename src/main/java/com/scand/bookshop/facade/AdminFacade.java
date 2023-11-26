package com.scand.bookshop.facade;

import com.scand.bookshop.dto.BanRequestDTO;
import com.scand.bookshop.dto.DTOConverter;
import com.scand.bookshop.dto.UserPageResponseDTO;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminFacade {

  private final UserService userService;

  public UserPageResponseDTO getUsersPage(int pageNumber,
                                          int pageSize,
                                          String searchTerm) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    Page<User> userPage;
    userPage = searchTerm == null ? userService.getUsersPage(pageable)
        : userService.searchUsersPage(searchTerm, pageable);
    int totalPages = userPage.getTotalPages();
    return new UserPageResponseDTO(userPage.map(DTOConverter::toDTO).getContent(), totalPages);
  }

  public void ban(BanRequestDTO requestDTO) {
    userService.ban(userService.getUserByUuid(requestDTO.getUuid()));
  }
}
