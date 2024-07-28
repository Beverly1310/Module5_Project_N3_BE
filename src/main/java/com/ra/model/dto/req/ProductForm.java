package com.ra.model.dto.req;

import com.ra.model.dto.res.BrandFormResponse;
import com.ra.model.dto.res.CategoryFormResponse;
import com.ra.model.dto.res.ColorFormResponse;
import com.ra.model.dto.res.ImageFormResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    private Long categoryId;
    List<CategoryFormResponse> categoryList;
    List<BrandFormResponse> brandList;
    private Long brandId;
    List<ImageFormResponse> imageList;
    List<Long> imageIdList;
    List<MultipartFile> imageFileList;
}
