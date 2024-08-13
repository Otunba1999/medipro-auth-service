package com.otunba.medipro.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotEmptyOrNullValidator.class )
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyOrNull {
    String message() default  "Field is required and cannot be empty";
    String fieldName() default  "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
