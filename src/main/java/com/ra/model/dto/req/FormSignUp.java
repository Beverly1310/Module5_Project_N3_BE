package com.ra.model.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ra.Validator.EmailExist;
import com.ra.Validator.UserExist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormSignUp {
    @Pattern(regexp = "^(?!.*[^\\w]).{6,100}$", message = "Username is not valid")
    @NotEmpty(message = "Username is empty")
    @NotBlank(message = "Username is blank")
    @UserExist
    private String username;
    @NotEmpty(message = "Password is empty")
    @NotBlank(message = "Password is blank")
    private String password;
    @NotEmpty(message = "Full name is empty")
    @NotBlank(message = "Full name is blank")
    private String fullName;
    @NotEmpty(message = "Email is empty")
    @NotBlank(message = "Email is blank")
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "Email is not valid")
    @EmailExist
    private String email;
    @Pattern(regexp = "^(03|05|07|08|09)\\d{8}$", message = "Phone is not Valid")
    private String phone;
    private MultipartFile avatar ;
    @DateTimeFormat(pattern = "yyyy-MM-đ")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt = LocalDate.now();
    @DateTimeFormat(pattern = "yyyy-MM-đ")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedAt = LocalDate.now();
    @Column(name = "is_delete")
    private Boolean isDeleted = false;
    private String address;
    private Set<String> roles;
}