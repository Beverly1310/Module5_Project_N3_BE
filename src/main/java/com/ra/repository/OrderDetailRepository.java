package com.ra.repository;

import com.ra.model.cons.OrderStatus;
import com.ra.model.entity.OrderDetail;
import com.ra.model.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("select od.id.productDetail from OrderDetail od join Orders o on od.id.order.id=o.id where o.status=:orderStatus and o.user.id=:userId")
    List<ProductDetail> findByUserIdAndOrderStatus(@Param("userId") Long userId, @Param("orderStatus") OrderStatus orderStatus);

}
