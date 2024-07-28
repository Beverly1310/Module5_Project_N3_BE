package com.ra.model.dto.res;

import com.ra.model.entity.Color;
import com.ra.model.entity.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class    ProductDetailResponse {
    private Long productDetailId;
    private String image;
    private String productDetailName;
    private boolean status;
    private Long stock;
    private double unitPrice;
    private Long colorId;
    private String color;
    private Long productId;
}
