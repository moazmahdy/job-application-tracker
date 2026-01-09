package com.elzozcode.job_tracker.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})");

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
}
