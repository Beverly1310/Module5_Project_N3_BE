package com.ra.security.principal;


import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomUserDetail implements UserDetails {

    private Long id;

    private String username;

    private String password;

    private String fullName;

    private String email;

    private String phone;

    private Boolean status=true;

    private String avatar;

    private Date createdAt=new Date();

    private Date updatedAt=new Date();
    private Double point;
    private Boolean isDeleted=false;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return (status || isDeleted) ;
    }
}