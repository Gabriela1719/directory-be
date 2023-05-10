package com.example.directory.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CustomPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{6,}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Pattern.compile(PASSWORD_PATTERN)
                .matcher(value)
                .matches();
    }
}
