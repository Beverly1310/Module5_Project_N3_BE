package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "review")
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comments;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    private Integer rating;
    private boolean status;
    @ManyToOne
    @JoinColumn(name = "product_detail_id",referencedColumnName = "id")
    private ProductDetail productDetail;
}
