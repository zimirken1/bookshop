package com.scand.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class PageResponseDTO {
    List<BookResponseDTO> books;
    int totalPages;
}
