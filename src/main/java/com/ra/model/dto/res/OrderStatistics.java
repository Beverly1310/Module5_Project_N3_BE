package com.ra.model.dto.res;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderStatistics {
    private String month;
    private Long successOrder;
    private Long cancelOrder;
}
