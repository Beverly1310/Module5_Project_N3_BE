package com.ra.service.impl;

import com.ra.model.cons.RoleName;
import com.ra.model.dto.req.FormSignUp;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import com.ra.repository.RoleRepository;
import com.ra.repository.UserRepository;
import com.ra.security.jwt.JWTProvider;
import com.ra.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTProvider jwtProvider;
   @Autowired
   private com.ra.project.util.FileUploadService fileUploadService;
    @Override
    public User signUp(FormSignUp formSignUp) {
        User user = User.builder()
                .email(formSignUp.getEmail())
                .phone(formSignUp.getPhone())
                .password(passwordEncoder.encode(formSignUp.getPassword()))
                .fullName(formSignUp.getFullName())
                .username(formSignUp.getUsername())
                .createdAt(formSignUp.getCreatedAt())
                .updatedAt(formSignUp.getUpdatedAt())
                .isDeleted(formSignUp.getIsDeleted())
                .status(true)
                .point(0D)
                .build();
        Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findRoleByRoleName(RoleName.USER).orElseThrow(() -> new NoSuchElementException("Role not found")));
        user.setRoles(roles);
        if (formSignUp.getAvatar()!=null && !formSignUp.getAvatar().isEmpty()){
            user.setAvatar(fileUploadService.uploadFileToServer(formSignUp.getAvatar()));
        } else {
            user.setAvatar("https://bestnycacupuncturist.com/wp-content/uploads/2016/11/anonymous-avatar-sm.jpg");
        }
        userRepository.save(user);
        return user;
    }
}
