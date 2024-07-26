package com.ra.repository;

import com.ra.model.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long> {
    void deleteAllByProductId(Long productId);
    List<ProductDetail> findAllByProductId(Long productId);
    boolean existsByProductIdAndColorId(Long productId,Long colorId);
    ProductDetail findByProductIdAndColorId(Long productId,Long colorId);
    @Query("SELECT pd FROM ProductDetail pd JOIN OrderDetail od ON pd.id = od.id.productDetail.id WHERE od.id.order.id = :orderId")
    List<ProductDetail> findAllByOrderId(Long orderId);

}
