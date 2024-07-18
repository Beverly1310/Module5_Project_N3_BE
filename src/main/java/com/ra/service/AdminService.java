package com.ra.service;


import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    Page<User> getUserWithPagingAndSorting(Pageable pageable);
}
