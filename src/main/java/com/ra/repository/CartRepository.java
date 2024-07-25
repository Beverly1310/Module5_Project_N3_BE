package com.ra.repository;

import com.ra.model.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<ShoppingCart,Long> {
    void deleteAllByUserId(Long userId);
    ShoppingCart findByUserIdAndProductDetailId(Long userId, Long productDetailId);
    List<ShoppingCart> findAllByUserId(Long userId);
    boolean existsByUserIdAndProductDetailId(Long userId, Long productDetailId);
}
