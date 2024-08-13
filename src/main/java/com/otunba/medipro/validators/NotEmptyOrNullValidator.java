package com.otunba.medipro.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotEmptyOrNullValidator implements ConstraintValidator<NotEmptyOrNull, String> {
    @Override
    public void initialize(NotEmptyOrNull constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && !value.trim().isEmpty();
    }


}
