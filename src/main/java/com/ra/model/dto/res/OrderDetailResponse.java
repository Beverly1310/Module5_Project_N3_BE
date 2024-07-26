package com.ra.model.dto.res;

import lombok.*;
import lombok.experimental.FieldDefaults;
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    String image;
    String productName;
    Long orderQuantity;
    Double unitPrice;
}
