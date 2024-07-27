package com.ra.repository;

import com.ra.Validator.UserExist;
import com.ra.model.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> getAllByUserId(Long userId);
//    @Query(value="delete from WishList wl where wl.product.id=:productId", nativeQuery = true)
    void deleteByProductId(Long productId);
}
