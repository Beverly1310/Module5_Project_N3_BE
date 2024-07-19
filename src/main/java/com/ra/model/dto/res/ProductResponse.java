package com.ra.model.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ProductResponse {
    private Long id;

    private String sku;

    private String productName;

    private String description;

    private Double unitPrice;
    private String brandName;
    private String categoryName;
    private Integer stockQuantity;

    private String image;

    private LocalDate createdAt;

    private LocalDate updatedAt;

}
