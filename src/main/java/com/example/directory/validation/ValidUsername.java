package com.example.directory.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = CustomUsernameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    String message() default "Invalid username format. Username must contain only letters.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
