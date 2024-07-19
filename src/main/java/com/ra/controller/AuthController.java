package com.ra.controller;

import com.ra.model.dto.req.FormSignUp;
import com.ra.model.entity.User;
import com.ra.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
private final AuthService authService;
    @PostMapping("/sign-in")
    public ResponseEntity<User> signUp(@Valid @ModelAttribute FormSignUp formSignUp) {
        User user = authService.signUp(formSignUp);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
