package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    private String description;
    private String image;
    private String productName;
    private String sku;
    private boolean status;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate updatedAt;
    @ManyToOne
    @JoinColumn(name = "brand_id",referencedColumnName = "id")
    private Brand brand;
    @ManyToOne
    @JoinColumn(name = "category_Id", referencedColumnName = "id")
    private Category category;


}