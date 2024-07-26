package com.ra.model.dto.res;

import com.ra.model.cons.OrderStatus;
import com.ra.model.entity.Coupon;
import com.ra.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DetailedOrderResponse {
    private Long id;
    private LocalDate createdAt;
    private String note;
    private String phone;
    private LocalDate receiveAt;
    private String serialNumber;
    private String status;
    private Double totalDiscountedPrice;
    private Double totalPrice;
    private Double totalPriceAfterCoupon;
    private String fullAddress;
    private String discount;
    private String username;
    List<OrderDetailResponse> orderDetails;

}
