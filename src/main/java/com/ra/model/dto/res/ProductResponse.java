package com.ra.model.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class ProductResponse {
    private Long id;

    private String sku;

    private String productName;

    private String description;

    private String brandName;
    private String categoryName;

    private String image;

    private LocalDate createdAt;

    private LocalDate updatedAt;
    private boolean status;
    private boolean onWishlist;
    List<ProductDetailResponse> productDetails;
}
