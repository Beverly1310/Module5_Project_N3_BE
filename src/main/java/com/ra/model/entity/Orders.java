package com.ra.model.entity;

import com.ra.model.cons.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ManyToAny;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate createdAt;
    private String district;
    private String note;
    private String phone;
    private String province;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate receiveAt;
    private String serialNumber;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String streetAddress;
    private Double totalDiscountedPrice;
    private Double totalPrice;
    private Double totalPriceAfterCoupon;
    private String ward;
    @ManyToOne
    @JoinColumn(name = "coupons_id",referencedColumnName = "id")
    private Coupons coupons;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
}
