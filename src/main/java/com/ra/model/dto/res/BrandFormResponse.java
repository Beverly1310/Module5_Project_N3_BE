package com.ra.model.dto.res;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BrandFormResponse {
    private Long brandId;
    private String brandName;
}
