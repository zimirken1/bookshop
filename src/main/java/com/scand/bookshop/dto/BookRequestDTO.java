package com.scand.bookshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String genre;
    @NotBlank
    private String author;
    private String description;
    @NotNull
    private Double price;
}
