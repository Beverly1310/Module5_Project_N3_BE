package com.ra.service;

import com.ra.model.dto.req.ProductDetailRequest;
import com.ra.model.dto.req.ProductForm;
import com.ra.model.dto.res.ProductDetailResponse;
import com.ra.model.dto.res.ProductIdAndName;
import com.ra.model.dto.res.ProductResponse;
import com.ra.model.entity.Product;
import com.ra.model.entity.ProductDetail;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    Page<ProductResponse> findProductsByKeywordAndCategory(String keyword, Long categoryId, int page, int size, String sortBy, String sortDirection);

    Page<ProductDetailResponse> findProductDetailsByKeyword(String keyword, int page, int size, String sortBy, String sortDirection);
    void add(ProductForm productForm);

    void addDetail(ProductDetailRequest productDetailRequest);

    void deleteDetail(Long productDetailId);
    void delete(Long id);
    List<ProductResponse> findProductsByCategory(Long categoryId);
    ProductForm getProductToUpdate(Long id);
    void removeImageFromForm(ProductForm productForm,Long imageId);
    void update(ProductForm productForm);

    List<ProductIdAndName> getInputList();

    void updateDetail(ProductDetailRequest request);
    ProductResponse findById(Long id);
    List<ProductResponse> getNewest();
    List<ProductResponse> getRelatedProducts(Long id);
    Page<ProductResponse> getAllProductsOnSale(String keyword, int page, int size, String sortBy, String sortDirection);
    List<ProductResponse> getMostSold();
    List<ProductResponse> getMostNotable();
    ProductResponse getResponse(Product product);

    ProductDetailResponse getDetailResponse(ProductDetail productDetail);
    ProductDetailResponse findDetailById(Long id);


}
