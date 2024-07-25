package com.ra.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TotalPriceRes  implements Serializable {
    private Double totalPrice;
    private Double totalPriceAfterDiscount;
}
