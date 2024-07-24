package com.ra.repository;

import com.ra.model.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<ShoppingCart,Long> {
    void deleteAllByUserId(Long userId);
    boolean existsByUserIdAndProductDetailId(Long userId, Long productDetailId);
}
