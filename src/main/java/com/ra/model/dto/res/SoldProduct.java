package com.ra.model.dto.res;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SoldProduct {
    private String month;
    private Long soldQuantity;
}
