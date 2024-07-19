package com.ra.service;

import com.ra.model.dto.req.ProductForm;
import com.ra.model.dto.res.ProductResponse;
import com.ra.model.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    Page<ProductResponse> findProductsByKeyword(String keyword, int page, int size, String sortBy, String sortDirection);

    void add(ProductForm productForm);
    void delete(Long id);
    List<ProductResponse> findProductsByCategory(Long categoryId);
    ProductForm getProductToUpdate(Long id);
    void removeImageFromForm(ProductForm productForm,Long imageId);
    void update(ProductForm productForm);

    ProductResponse findById(Long id);
    List<ProductResponse> getNewest();
    Page<ProductResponse> getAllProductsOnSale(String keyword, int page, int size, String sortBy, String sortDirection);
    List<ProductResponse> getMostSold();
    List<ProductResponse> getMostNotable();
    ProductResponse getResponse(Product product);
}
