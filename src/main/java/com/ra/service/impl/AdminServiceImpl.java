package com.ra.service.impl;

import com.ra.model.entity.User;
import com.ra.repository.UserRepository;
import com.ra.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdminServiceImpl implements AdminService {
    private UserRepository userRepository;
    @Override
    public Page<User> getUserWithPagingAndSorting(Pageable pageable) {
        return userRepository.getAll(pageable);
    }
}
