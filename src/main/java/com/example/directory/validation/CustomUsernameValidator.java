package com.example.directory.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CustomUsernameValidator implements ConstraintValidator<ValidUsername, String> {

    private static final String USERNAME_PATTERN = "^[a-zA-Z]{2,100}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Pattern.compile(USERNAME_PATTERN)
                .matcher(value)
                .matches();
        }
}
