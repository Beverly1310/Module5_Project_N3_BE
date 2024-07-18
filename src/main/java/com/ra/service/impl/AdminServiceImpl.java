package com.ra.service.impl;

import com.ra.model.cons.RoleName;
import com.ra.model.entity.User;
import com.ra.repository.RoleRepository;
import com.ra.repository.UserRepository;
import com.ra.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    public Page<User> getUserWithPagingAndSorting(Pageable pageable,String direction,String search) {
        if (direction!=null){
            if (direction.equalsIgnoreCase("desc")){
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),pageable.getSort().descending());
            } else {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),pageable.getSort().ascending());
            }
        }
        Page<User> users;
        if (search.isEmpty()) {
            users = userRepository.getAll(pageable);
        } else {
            users = userRepository.findAllByFullNameContains(search, pageable);
        }
        return users;
    }
    @Override
    public User changStatus(Long id) {
        User user =findById(id);
        if (user!=null) {
            if (user.getRoles().stream().anyMatch(role -> role.getRoleName()==RoleName.ADMIN)) {
                throw new RuntimeException("You can not block Admin");
            }
            else {
                userRepository.updateQueryChangeStatus(id);
                return findById(id);
            }
        }
        else {
            throw new NoSuchElementException("User not found");
        }
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User setRole(Long id) {
        User user = findById(id);
        if (user != null) {
            if (user.getRoles().stream().anyMatch(role -> role.getRoleName()==RoleName.MANAGER)){
                user.getRoles().remove(roleRepository.findRoleByRoleName(RoleName.MANAGER));
            } else {
                user.getRoles().add(roleRepository.findRoleByRoleName(RoleName.MANAGER));
            }
            userRepository.save(user);
            return user;
        }
        else {
            throw new NoSuchElementException("User not found");
        }
    }
}
