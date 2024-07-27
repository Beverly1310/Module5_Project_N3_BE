package com.ra.service.imp;

import com.ra.model.dto.res.BrandFormResponse;
import com.ra.model.dto.res.CategoryFormResponse;
import com.ra.model.entity.Brand;
import com.ra.model.entity.Category;
import com.ra.repository.BrandRepository;
import com.ra.service.IBrandService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements IBrandService {
    private final BrandRepository brandRepository;
    @Override
    public List<BrandFormResponse> getAllForInput() {
        List<Brand> brands=brandRepository.findAll();
        List<BrandFormResponse> responses=new ArrayList<>();
        for (Brand brand:brands){
            BrandFormResponse brandFormResponse= BrandFormResponse.builder()
                    .brandId(brand.getId())
                    .brandName(brand.getBrandName()).
                    build();
            responses.add(brandFormResponse);
        }
        return responses;
    }
}
