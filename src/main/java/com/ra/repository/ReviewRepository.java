package com.ra.repository;

import com.ra.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
