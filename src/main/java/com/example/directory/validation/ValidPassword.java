package com.example.directory.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Constraint(validatedBy = CustomPasswordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "Password cannot be null.")
@NotEmpty(message = "Password is required.")
public @interface ValidPassword {
    String message() default "Password must be at least 6 characters long, contains at least 1 lower case letter, and 1 digit.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
