package main.java.server.security;

public class PasswordPolicy {

    public static boolean isStrong(String password) {

        if (password == null) return false;

        boolean length = password.length() >= 8;
        boolean upper = password.chars().anyMatch(Character::isUpperCase);
        boolean lower = password.chars().anyMatch(Character::isLowerCase);
        boolean digit = password.chars().anyMatch(Character::isDigit);

        return length && upper && lower && digit;
    }
}
