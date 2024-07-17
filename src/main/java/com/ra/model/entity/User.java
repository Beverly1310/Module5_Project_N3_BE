package com.ra.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username",length = 100)
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "fullname",length = 100)
    @NotBlank
    @NotEmpty
    private String fullName;
    @NotBlank
    @NotEmpty
    @Email
    private String email;
    @Pattern(regexp = "^(03|05|07|08|09)\\d{8}$")
    private String phone;
    @Column(name = "status")
    private Boolean status=true;
    @Column(name = "avatar")
    private String avatar="https://bestnycacupuncturist.com/wp-content/uploads/2016/11/anonymous-avatar-sm.jpg";
    @Column(name = "created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt=new Date();
    @Column(name = "updated_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt=new Date();
    @Column(name = "is_delete")
    private Boolean isDeleted=false;
    private Double point;
    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}