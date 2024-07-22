package com.ra.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BannerEdit {
    private Long id;
    @NotNull(message = "Banner name must not be null")
    @NotBlank(message = "Banner name must not be empty")
    private String bannerName;
    private String description;
    private MultipartFile image;
}

