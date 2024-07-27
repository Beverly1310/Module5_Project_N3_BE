package com.ra.service.imp;

import com.ra.model.dto.req.ChangePasswordRequest;
import com.ra.model.dto.req.UserEdit;
import com.ra.model.entity.User;
import com.ra.repository.UserRepository;
import com.ra.security.principal.CustomUserDetail;
import com.ra.service.UserService;
import com.ra.util.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    @Override
    public boolean changePassword(ChangePasswordRequest changePasswordRequest) {
      try {
          User user = userRepository.findById(getCurrentUser().getId()).orElseThrow(() -> new NoSuchElementException("User not found"));
          user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
          userRepository.save(user);
          return true;
      } catch (NoSuchElementException e) {
          throw new RuntimeException("Change password failed");
      }
    }

    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;
    public static CustomUserDetail getCurrentUser() {
        return (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public User editUser(UserEdit userEdit) {
        User user = userRepository.findById(getCurrentUser().getId()).orElseThrow(() -> new NoSuchElementException("User not found"));
        if (userEdit.getFullName() != null && !userEdit.getFullName().isBlank()) {
            user.setFullName(userEdit.getFullName());
        }
        if (userEdit.getEmail() != null && !userEdit.getEmail().isBlank()) {
            user.setEmail(userEdit.getEmail());
        }
        if (userEdit.getPhone() != null && !userEdit.getPhone().isBlank()) {
            user.setPhone(userEdit.getPhone());
        }
        if (userEdit.getAvatar() != null && !userEdit.getAvatar().isEmpty()) {
            user.setAvatar(fileUploadService.uploadFileToServer(userEdit.getAvatar()));
        }
        user.setUpdatedAt(userEdit.getUpdatedAt());
        return userRepository.save(user);
    }
}
