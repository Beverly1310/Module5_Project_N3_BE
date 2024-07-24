package com.ra.repository;

import com.ra.model.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long> {
    void deleteAllByProductId(Long productId);
    List<ProductDetail> findAllByProductId(Long productId);
    boolean existsByProductIdAndColorId(Long productId,Long colorId);
    ProductDetail findByProductIdAndColorId(Long productId,Long colorId);
}
