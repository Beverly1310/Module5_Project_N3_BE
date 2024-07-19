package com.ra.model.dto.req;

import com.ra.model.entity.Image;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductForm {
    private Long id;
    @NotBlank(message = "Name cannot be empty")
    @NotEmpty(message = "Name cannot be empty")
    private String productName;
    private String description;

    private String image;
    private MultipartFile imageFile;

    @NotNull(message = "Category ID is mandatory")
    private Long categoryId;
    @NotNull(message = "Brand ID is mandatory")
    private Long brandId;

    List<Image> imageList;
    List<MultipartFile> imageFileList;
}
