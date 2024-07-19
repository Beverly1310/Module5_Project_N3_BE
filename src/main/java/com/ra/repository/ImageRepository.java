package com.ra.repository;

import com.ra.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByProductId(Long id);

    void deleteAllByProductId(Long id);
}
