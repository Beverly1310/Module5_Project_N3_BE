package com.ra.model.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryFormResponse {
    private Long categoryId;
    private String categoryName;
}
