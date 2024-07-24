package com.ra.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "banner")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bannerName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    private String description;
    private String image;
    private boolean status=true;

    // Getters and setters
}
