package main.java.server.service;
import java.util.regex.Pattern;


public class ValidationUtils {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_REGEX.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return password.length() >= 8;
    }
}
