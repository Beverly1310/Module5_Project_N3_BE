package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_detail")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String productDetailName;
    private boolean status;
    private int stock;
    private double unitPrice;
    @ManyToOne
    @JoinColumn(name = "color_Id", referencedColumnName = "id")
    private Color color;
    @ManyToOne
    @JoinColumn(name = "product_Id", referencedColumnName = "id")
    private Product product;
}