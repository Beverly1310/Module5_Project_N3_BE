package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comments;
    private LocalDate createdAt;
    private Integer rating;
    private boolean status;
    @ManyToOne
    @JoinColumn(name = "product_detail_id",referencedColumnName = "id")
    private ProductDetail productDetail;
}
