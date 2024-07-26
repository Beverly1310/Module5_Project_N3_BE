package com.ra.model.dto.res;

import com.ra.model.cons.OrderStatus;
import com.ra.model.entity.Coupon;
import com.ra.model.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    private Long id;
    private LocalDate createdAt;
    private String note;
    private String phone;
    private LocalDate receiveAt;
    private String serialNumber;
    private String status;
    private Double totalPriceAfterCoupon;
    private String user;
}
