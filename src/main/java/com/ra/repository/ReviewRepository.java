package com.ra.repository;

import com.ra.model.entity.Product;
import com.ra.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByProductDetailId(Long productDetailId);
    boolean existsByUserIdAndProductDetailId(Long userId, Long productDetailId);
    @Transactional
    @Modifying
    @Query("update Review r set r.status = CASE WHEN r.status = true THEN false ELSE true END where r.id = :reviewId")
    void toggleDisplay(@Param("reviewId") Long reviewId);

}
