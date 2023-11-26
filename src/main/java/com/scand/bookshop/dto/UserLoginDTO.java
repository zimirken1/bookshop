package com.scand.bookshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {
        @NotBlank
        @Size(min = 5, max = 20)
        @Pattern(regexp = "[a-zA-Z0-9]*")
        private String username;

        @NotBlank
        @Size(min = 5)
        private String password;
}
