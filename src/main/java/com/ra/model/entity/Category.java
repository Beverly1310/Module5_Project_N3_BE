package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "category_name",unique = true)
    private String categoryName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt ;
    private String description;
    private String image;
    private boolean status=true;

}