package com.ra.Validator.impl;


import com.ra.Validator.OldPasswordMatching;
import com.ra.service.imp.UserServiceImpl;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class OldPasswordMatchingValidator implements ConstraintValidator<OldPasswordMatching, String> {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String oldPassword = UserServiceImpl.getCurrentUser().getPassword();
        return passwordEncoder.matches(s, oldPassword);
    }
}