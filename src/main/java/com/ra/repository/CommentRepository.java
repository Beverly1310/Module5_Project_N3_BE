package com.ra.repository;

import com.ra.model.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(countQuery = "SELECT count(c) FROM Comment c WHERE c.product.id = :productId AND c.user.id!= :userId")
    Page<Comment> findAllByProductIdAndUserIdNot(Long productId, Long userId, Pageable pageable);
    Comment findAllByUserIdAndProductId(Long userId, Long productId);

    boolean existsByUserIdAndProductId(Long userId, Long productId);


}
