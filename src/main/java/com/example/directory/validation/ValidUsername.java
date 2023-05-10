package com.example.directory.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Constraint(validatedBy = CustomUsernameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "Username cannot be null.")
@NotEmpty(message = "Username is required.")
public @interface ValidUsername {
    String message() default "Invalid username format. Username must contain only letters.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
