package com.ra.repository;

import com.ra.model.cons.OrderStatus;
import com.ra.model.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query("select count(od) from Orders od where od.status=:status and YEAR(od.createdAt) =:year and month(od.createdAt)=:month ")
 Long getOrdersByStatusAndYear(OrderStatus status, Integer year,Integer month);
}
