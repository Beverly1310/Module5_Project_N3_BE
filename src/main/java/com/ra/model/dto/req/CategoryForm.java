package com.ra.model.dto.req;

import com.ra.Validator.AtLeastOneNotNull;
import com.ra.Validator.UniqueCategoryName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@UniqueCategoryName
@AtLeastOneNotNull
public class CategoryForm {
    Long id;
    @NotBlank(message = "Name can not be blank!")
    @NotEmpty(message = "Name can not be empty!")

    String categoryName;
    String image;
    String description;
    MultipartFile imageFile;

}
