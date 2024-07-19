package com.ra.Validator;

import com.ra.model.dto.req.CategoryForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, CategoryForm> {
    @Override
    public boolean isValid(CategoryForm form, ConstraintValidatorContext context) {
        // Check if the form is null to avoid NullPointerException
        if (form == null) {
            return false;
        }
        // Check if either image or imageFile is not null or not empty
        return (form.getImage() != null && !form.getImage().isEmpty()) ||
                (form.getImageFile() != null && !form.getImageFile().isEmpty());
    }
}
