package com.ra.Validator;

import com.ra.model.dto.req.CategoryForm;
import com.ra.repository.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueCategoryNameValidator implements ConstraintValidator<UniqueCategoryName, CategoryForm> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void initialize( UniqueCategoryName constraintAnnotation) {
    }

    @Override
    public boolean isValid(CategoryForm categoryForm, ConstraintValidatorContext context) {

        if (categoryForm.getCategoryName() == null || categoryForm.getCategoryName().isEmpty()) {
            return true;
        }
        if (categoryForm.getId() != null) {
            if (categoryForm.getCategoryName().equals(categoryRepository.findById(categoryForm.getId()).get().getCategoryName())) {
                return true;
            }
        }
        return !categoryRepository.existsByCategoryName(categoryForm.getCategoryName());
    }
}
