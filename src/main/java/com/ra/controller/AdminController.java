package com.ra.controller;


import com.ra.model.entity.User;
import com.ra.service.AdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private AdminService adminService;
    @GetMapping("/user")
    public ResponseEntity<Page<User>> getAllUsers( @PageableDefault(page = 0, size = 3, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<User> users = adminService.getUserWithPagingAndSorting(pageable);
        return ResponseEntity.ok().body(users);
    }
}
