package com.ra.model.dto.res;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SaleRevenue {
    private String month;
    private Long revenue;
}
