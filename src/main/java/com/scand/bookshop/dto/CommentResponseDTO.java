package com.scand.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentResponseDTO {
    private String username;
    private String timestamp;
    private String text;
    private String uuid;
    private List<CommentResponseDTO> replies;
    private boolean isRemoved;
}
