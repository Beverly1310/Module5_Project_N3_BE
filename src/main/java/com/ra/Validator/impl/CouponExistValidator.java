package com.ra.Validator.impl;


import com.ra.Validator.CouponExist;
import com.ra.repository.CouponRepository;
import com.ra.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponExistValidator implements ConstraintValidator<CouponExist,String> {
    @Autowired
    private CouponRepository couponRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return couponRepository.findByCode(s).isEmpty();
    }
}