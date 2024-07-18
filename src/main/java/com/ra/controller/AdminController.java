package com.ra.controller;


import com.ra.model.entity.User;
import com.ra.service.AdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
//@CrossOrigin("*")
public class AdminController {
    private final AdminService adminService;
    @GetMapping("/user")
    public ResponseEntity<Page<User>> getAllUsers( @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                   @RequestParam(required = false) String direction,
                                                   @RequestParam(defaultValue = "") String search
                                       ) {
        Page<User> users = adminService.getUserWithPagingAndSorting(pageable,direction,search);
        return ResponseEntity.ok().body(users);
    }
    @PutMapping("/user/{userId}")
    public ResponseEntity<User> changeStatus(@PathVariable("userId") Long userId) {
        User user = adminService.changStatus(userId);
        return ResponseEntity.ok().body(user);
    }
    @PutMapping("user/setrole/{userId}")
    public ResponseEntity<User> changeRole(@PathVariable("userId") Long userId) {
       User user = adminService.setRole(userId);
       return ResponseEntity.ok().body(user);
    }
}
