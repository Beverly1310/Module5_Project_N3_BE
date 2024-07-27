package com.ra.service;

import com.ra.model.dto.req.FormSignIn;
import com.ra.model.dto.req.FormSignUp;
import com.ra.model.dto.res.JWTResponse;
import com.ra.model.entity.User;

public interface AuthService {
    User signUp(FormSignUp formSignUp);
    JWTResponse login(FormSignIn formSignIn);
}
