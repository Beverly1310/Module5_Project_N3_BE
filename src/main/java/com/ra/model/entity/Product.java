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
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate createdAt;
    private String description;
    private String image;
    private String productName;
    private String sku;
    private boolean status;
    private LocalDate updatedAt;
    private Long brandId;
    @ManyToOne
    @JoinColumn(name = "category_Id", referencedColumnName = "id")
    private Category category;

}