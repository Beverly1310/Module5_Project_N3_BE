package com.ra.repository;

import com.ra.model.entity.OrderDetail;
import com.ra.model.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    List<OrderDetail> findAllByIdOrderId(Long orderId);
}
