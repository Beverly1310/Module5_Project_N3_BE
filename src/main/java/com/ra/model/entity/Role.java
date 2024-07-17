package com.ra.model.entity;


import com.ra.model.cons.RoleName;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleName roleName;

}