package com.scand.bookshop.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileCredentialsDTO {
    @NotBlank
    @Size(min = 5)
    private String password;

    @NotBlank @Email
    private String email;
}
