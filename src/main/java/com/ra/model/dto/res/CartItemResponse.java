package com.ra.model.dto.res;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    Long cartId;
    String image;
    Long productDetailId;
    String productName;
    double unitPrice;
    Long quantity;

}
