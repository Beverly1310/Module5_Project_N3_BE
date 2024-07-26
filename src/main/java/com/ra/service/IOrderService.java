package com.ra.service;

import com.ra.model.cons.OrderStatus;
import com.ra.model.dto.res.OrderResponse;
import com.ra.model.entity.Orders;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderService {
    List<OrderResponse> findAll(int page, int pageSize,String sortDirection);
    void changeStatus(Long orderId, String status);
    OrderResponse getResponse(Orders orders);
}
