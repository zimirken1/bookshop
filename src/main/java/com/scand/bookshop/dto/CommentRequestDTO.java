package com.scand.bookshop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDTO {
    @NotBlank
    private String text;
    @NotBlank
    private String bookUuid;
    
    private String parentUuid;
}
