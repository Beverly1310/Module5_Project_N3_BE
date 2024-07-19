package com.ra.controller;

import com.ra.model.dto.req.FormSignIn;
import com.ra.model.dto.req.FormSignUp;
import com.ra.model.dto.res.JWTResponse;
import com.ra.model.dto.res.ResponseData;
import com.ra.model.entity.User;
import com.ra.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
private final AuthService authService;
    @PostMapping("/sign-up")
    public ResponseEntity<User> signUp(@Valid @ModelAttribute FormSignUp formSignUp) {
        User user = authService.signUp(formSignUp);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    @PostMapping("/sign-in")
    public ResponseEntity<ResponseData<JWTResponse>> signIn(@Valid @RequestBody FormSignIn formSignIn) {
        JWTResponse jwtResponse = authService.login(formSignIn);
        return new ResponseEntity<>(new ResponseData<>("success",jwtResponse, HttpStatus.OK),HttpStatus.OK);
    }
}
