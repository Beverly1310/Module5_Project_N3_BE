package com.ra.model.dto.req;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEdit {
    private String fullName;
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "Email is not valid")
    private String email;
    @Pattern(regexp = "^(03|05|07|08|09)\\d{8}$", message = "Phone is not Valid")
    private String phone;
    private MultipartFile avatar;
    @DateTimeFormat(pattern = "yyyy-MM-đ")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedAt= LocalDate.now();
}
