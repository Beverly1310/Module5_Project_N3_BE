package com.ra.repository;

import com.ra.model.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishList, Long> {
    void deleteByUserIdAndProductId(Long userId, Long productId);
    List<WishList> getAllByUserId(Long userId);
    void deleteAllByUserId(Long userId);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    void deleteByProductId(Long productId);
}
