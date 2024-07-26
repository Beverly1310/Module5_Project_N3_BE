package com.ra.service;

import com.ra.model.cons.OrderStatus;
import com.ra.model.dto.res.DetailedOrderResponse;
import com.ra.model.dto.res.OrderResponse;
import com.ra.model.entity.Orders;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderService {
    Page<OrderResponse> findAll(int page, int pageSize,String sortField,String sortDirection);
    void changeStatus(Long orderId, String status);
    DetailedOrderResponse viewFullDetail(Long orderId);
    OrderResponse getResponse(Orders orders);
}
