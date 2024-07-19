package com.ra.service;

import com.ra.model.dto.req.FormSignUp;
import com.ra.model.entity.User;

public interface AuthService {
    User signUp(FormSignUp formSignUp);
}
