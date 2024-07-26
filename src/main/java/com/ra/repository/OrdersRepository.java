package com.ra.repository;

import com.ra.model.cons.OrderStatus;
import com.ra.model.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query("select count(o) from Orders o where o.status=:status and YEAR(o.createdAt) =:year and month(o.createdAt)=:month ")
    Long getOrdersByStatusAndYear(OrderStatus status, Integer year, Integer month);
    @Query("select sum(od.orderQuantity) from Orders o join OrderDetail od on o.id = od.id.order.id where o.status=:status and year(o.createdAt)=:year and month(o.createdAt)=:month ")
    Long countByStatusAndYear(OrderStatus status, Integer year,Integer month);

}
