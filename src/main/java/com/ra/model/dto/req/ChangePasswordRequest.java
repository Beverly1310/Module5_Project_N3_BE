package com.ra.model.dto.req;


import com.ra.Validator.OldPasswordMatching;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordRequest {
    @OldPasswordMatching
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}