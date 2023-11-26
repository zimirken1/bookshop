package com.scand.bookshop.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPageResponseDTO {
  private List<AdminPanelUserResponseDTO> users;
  private Integer totalPages;
}
