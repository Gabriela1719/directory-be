package com.example.directory.validation;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValueValidator {
    private static final String EMAIL_REGEX = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$";
    private static final String MOBILE_NUMBER_REGEX = "^06[0-9]{7}$";
    private static final String LANDLINE_NUMBER_REGEX = "^03[0-9]-[0-9]{3}-[0-9]{3}$";


    public static void validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    public static void validateMobileNumber(String value) {
        if (!Pattern.matches(MOBILE_NUMBER_REGEX, value)) {
            throw new IllegalArgumentException("Invalid mobile number");
        }
    }

    public static void validateLandlineNumber(String value) {
        if (!Pattern.matches(LANDLINE_NUMBER_REGEX, value)) {
            throw new IllegalArgumentException("Invalid landline number");
        }
    }
}
