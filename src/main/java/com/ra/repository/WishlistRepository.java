package com.ra.repository;

import com.ra.model.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<WishList,Long> {
    void deleteByUserIdAndProductId(Long userId, Long productId);
    List<WishList> getAllByUserId(Long userId);
    void deleteAllByUserId(Long userId);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
