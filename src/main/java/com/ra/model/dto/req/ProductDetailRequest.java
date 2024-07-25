package com.ra.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class ProductDetailRequest {
    private Long productDetailId;
    private String image;
    private MultipartFile imageFile;
    @NotEmpty(message = "Name cannot be empty!")
    @NotBlank(message = "Name cannot be empty!")
    private String productDetailName;
    @PositiveOrZero(message = "Stocks must be equal to or greater than 0!")
    private Long stock;
    @PositiveOrZero(message = "Price must be equal to or greater than 0!")
    private boolean status;
    private double unitPrice;
    private Long colorId;
    private Long productId;
}
