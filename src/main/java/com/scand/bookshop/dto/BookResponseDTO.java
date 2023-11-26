package com.scand.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO {
    private String title;
    private String genre;
    private String author;
    private String uuid;
    private String description;
    private double price;
    private Boolean isPaid;
}