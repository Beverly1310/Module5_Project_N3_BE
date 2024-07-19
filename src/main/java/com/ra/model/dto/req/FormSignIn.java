package com.ra.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormSignIn {
    @NotEmpty(message = "Username is empty")
    @NotBlank(message = "Username is blank")
    private String username;
    @NotEmpty(message = "Password is empty")
    @NotBlank(message = "Password is blank")
    private String password;
}