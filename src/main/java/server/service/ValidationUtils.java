package main.java.server.service;
import java.util.regex.Pattern;


public class ValidationUtils {

    //Email
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_REGEX.matcher(email).matches();
    }

    //Password
    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return password.length() >= 8;
    }

    //Stringhe generiche
    public static boolean isNotBlank(String value){
        return value != null && !value.trim().isEmpty();
    }
    public static boolean isValidStringLength(String value, int min, int max) {
        if (value == null) return false;
        int len = value.trim().length();
        return len >= min && len <= max;
    }

    //Ristorante
    public static boolean isValidName(String nome) {
        return isNotBlank(nome) && isValidStringLength(nome, 2, 50);
    }

    public static boolean isValidCitta(String citta) {
        return isNotBlank(citta) && isValidStringLength(citta, 2, 50);
    }

    public static boolean isValidNazione(String nazione) {
        return isNotBlank(nazione) && isValidStringLength(nazione, 2, 50);
    }

    public static boolean isValidVia(String via) {
        return isNotBlank(via) && isValidStringLength(via, 2, 80);
    }

    public static boolean isValidNumeroCivico(int numero) {
        return numero > 0 && numero <= 100000;
    }

    // BOOLEANI (espliciti per chiarezza service layer)
    public static boolean isValidBoolean(Boolean value) {
        return value != null;
    }
}
