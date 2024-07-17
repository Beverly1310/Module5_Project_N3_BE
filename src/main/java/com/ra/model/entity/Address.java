package com.ra.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "district")
    private String district;
    @Column(name = "phone")
    private String phone;
    @NotNull(message = "Priority must not be null")
    @Column(name = "priority")
    private boolean priority ;
    @Column(name = "province")
    private String province;
    @Column(name = "receive_name")
    private String receiveName;
    @Column(name = "street_address")
    private String streetAddress;
    @Column(name = "ward")
    private String ward;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

}
