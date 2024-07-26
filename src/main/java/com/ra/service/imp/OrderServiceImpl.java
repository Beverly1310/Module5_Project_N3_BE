package com.ra.service.imp;

import com.ra.model.cons.OrderStatus;
import com.ra.model.dto.res.DetailedOrderResponse;
import com.ra.model.dto.res.OrderDetailResponse;
import com.ra.model.dto.res.OrderResponse;
import com.ra.model.entity.*;
import com.ra.repository.MessageRepository;
import com.ra.repository.OrderDetailRepository;
import com.ra.repository.OrdersRepository;
import com.ra.repository.ProductDetailRepository;
import com.ra.service.IOrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final OrdersRepository ordersRepository;
    private final MessageRepository messageRepository;
    private final ProductDetailRepository productDetailRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public Page<OrderResponse> findAll(int page, int pageSize, String sortField, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<Orders> orders = ordersRepository.findAll(pageable);
        return orders.map(this::getResponse);
    }

    @Transactional
    @Override
    public void changeStatus(Long orderId, String status) {
        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByIdOrderId(orderId);
        User user = orders.getUser();
        if (!orders.getStatus().equals(OrderStatus.SUCCESS)) {
            if (orders.getStatus().equals(OrderStatus.CANCEL)) {
                throw new RuntimeException("This order has already been cancelled!");
            }
            Message message;
            if (orders.getStatus().equals(OrderStatus.DENIED)&&!status.equals("DENIED")) {
                // Restock items
                for (OrderDetail orderDetail : orderDetails) {
                    ProductDetail productDetail = orderDetail.getId().getProductDetail();
                    Long stock = orderDetail.getOrderQuantity();
                    productDetail.setStock(productDetail.getStock() - stock);
                    productDetailRepository.save(productDetail);
                }
            }

            switch (status) {
                case "CONFIRM":
                    orders.setStatus(OrderStatus.CONFIRM);
                    message = Message.builder()
                            .user(user)
                            .createdAt(LocalDate.now())
                            .message("Your order " + orders.getSerialNumber() + " has been accepted! Expect it to arrive within a few days!")
                            .build();
                    messageRepository.save(message);
                    break;
                case "DELIVERY":
                    orders.setStatus(OrderStatus.DELIVERY);
                    break;
                case "DENIED":
                    if (orders.getStatus().equals(OrderStatus.DENIED)) {
                        throw new RuntimeException("This order has already been denied!");
                    }
                    // Restock items
                    for (OrderDetail orderDetail : orderDetails) {
                        ProductDetail productDetail = orderDetail.getId().getProductDetail();
                        Long stock = orderDetail.getOrderQuantity();
                        productDetail.setStock(productDetail.getStock() + stock);
                        productDetailRepository.save(productDetail);
                    }
                    orders.setStatus(OrderStatus.DENIED);
                    message = Message.builder()
                            .createdAt(LocalDate.now())
                            .user(user)
                            .message("Due to our internal issues, your order " + orders.getSerialNumber() + " has unfortunately been denied!")
                            .build();
                    messageRepository.save(message);
                    break;
                case "SUCCESS":
                    orders.setStatus(OrderStatus.SUCCESS);
                    orders.setReceiveAt(LocalDate.now());
                    message = Message.builder()
                            .user(user)
                            .createdAt(LocalDate.now())
                            .message("Your order " + orders.getSerialNumber() + " has been registered as received! Please contact our support if you have any issues!")
                            .build();
                    messageRepository.save(message);
                    break;
                default:
                    throw new RuntimeException("Invalid status");
            }
            ordersRepository.save(orders);
        } else {
            throw new RuntimeException("You cannot change the status of an order that has been successfully delivered!");
        }
    }


    @Override
    public DetailedOrderResponse viewFullDetail(Long orderId) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
        List<OrderDetail> details = orderDetailRepository.findAllByIdOrderId(orderId);
        String fullAddress = String.join(", ",
                order.getStreetAddress(),
                order.getWard(),
                order.getDistrict(),
                order.getProvince()
        );

        // Calculate or retrieve discount information if necessary
        String discount = order.getCoupon() != null ? order.getCoupon().getDiscount() : "0";

        // Build username
        String username = order.getUser() != null ? order.getUser().getUsername() : null;

        // Convert order details
        List<OrderDetailResponse> orderDetails = details.stream()
                .map(detail -> new OrderDetailResponse(
                        detail.getId().getProductDetail().getImage(),
                        detail.getId().getProductDetail().getProduct().getProductName() + " " + detail.getId().getProductDetail().getProductDetailName(),
                        detail.getOrderQuantity(),
                        detail.getUnitPrice()
                ))
                .collect(Collectors.toList());

        return DetailedOrderResponse.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .note(order.getNote())
                .phone(order.getPhone())
                .receiveAt(order.getReceiveAt())
                .serialNumber(order.getSerialNumber())
                .status(order.getStatus().name())
                .totalDiscountedPrice(order.getTotalDiscountedPrice())
                .totalPrice(order.getTotalPrice())
                .totalPriceAfterCoupon(order.getTotalPriceAfterCoupon())
                .fullAddress(fullAddress)
                .discount(discount)
                .username(username)
                .orderDetails(orderDetails)
                .build();
    }

    @Override
    public OrderResponse getResponse(Orders orders) {

        return OrderResponse.builder()
                .id(orders.getId())
                .createdAt(orders.getCreatedAt())
                .totalPriceAfterCoupon(orders.getTotalPriceAfterCoupon())
                .serialNumber(orders.getSerialNumber())
                .receiveAt(orders.getReceiveAt())
                .note(orders.getNote())
                .phone(orders.getPhone())
                .user(orders.getUser().getUsername())
                .status(orders.getStatus().toString())
                .build();
    }
}
