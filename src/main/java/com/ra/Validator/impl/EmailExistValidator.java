package com.ra.Validator.impl;


import com.ra.Validator.EmailExist;
import com.ra.model.entity.User;
import com.ra.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailExistValidator implements ConstraintValidator<EmailExist, String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (userRepository==null){
            return true;
        }
        return !userRepository.findAll().stream().map(User::getEmail).toList().contains(s);
    }
}