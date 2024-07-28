package com.ra.model.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductIdAndName {
    private Long productId;
    private String productName;
}
