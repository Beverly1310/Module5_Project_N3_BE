package com.ra.model.dto.res;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ProductPageResponse {
    private List<ProductResponse> products;
    private int totalPages;

}
