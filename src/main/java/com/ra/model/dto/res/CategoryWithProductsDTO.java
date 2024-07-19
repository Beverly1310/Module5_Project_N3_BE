package com.ra.model.dto.res;

import com.ra.model.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CategoryWithProductsDTO {
    private Category category;
    private List<ProductResponse> products;
    private String message;
}
