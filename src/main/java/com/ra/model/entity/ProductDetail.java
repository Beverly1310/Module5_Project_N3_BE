package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "product_detail")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String productDetailName;
    private boolean status;
    private Long stock;
    private double unitPrice;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "color_Id", referencedColumnName = "id")
    private Color color;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_Id", referencedColumnName = "id")
    private Product product;
}