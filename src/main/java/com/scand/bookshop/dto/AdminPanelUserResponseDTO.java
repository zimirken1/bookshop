package com.scand.bookshop.dto;

import com.scand.bookshop.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminPanelUserResponseDTO {
  private String uuid;
  private String userName;
  private String email;
  private String regDate;
  private User.Role role;
}
