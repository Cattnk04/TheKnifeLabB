package main.java.server.service;
import java.util.regex.Pattern;

/**
 *
 */
public class ValidationUtils {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_REGEX.matcher(email).matches();
    }

    /**
     *
      * @param password
     * @return
     */
    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return password.length() >= 8;
    }

    /**
     *
      * @param value
     * @return
     */
    public static boolean isNotBlank(String value){
        return value != null && !value.trim().isEmpty();
    }

    /**
     *
     * @param value
     * @param min
     * @param max
     * @return
     */

    public static boolean isValidStringLength(String value, int min, int max) {
        if (value == null) return false;
        int len = value.trim().length();
        return len >= min && len <= max;
    }

    /**
     *
      * @param nome
     * @return
     */
    public static boolean isValidName(String nome) {
        return isNotBlank(nome) && isValidStringLength(nome, 2, 50);
    }

    /**
     *
     * @param citta
     * @return
     */
    public static boolean isValidCitta(String citta) {
        return isNotBlank(citta) && isValidStringLength(citta, 2, 50);
    }

    /**
     *
     * @param nazione
     * @return
     */
    public static boolean isValidNazione(String nazione) {
        return isNotBlank(nazione) && isValidStringLength(nazione, 2, 50);
    }

    /**
     *
     * @param via
     * @return
     */
    public static boolean isValidVia(String via) {
        return isNotBlank(via) && isValidStringLength(via, 2, 80);
    }

    /**
     *
     * @param numero
     * @return
     */
    public static boolean isValidNumeroCivico(int numero) {
        return numero > 0 && numero <= 100000;
    }

    /**
     *
      * @param value
     * @return
     */
    public static boolean isValidBoolean(Boolean value) {
        return value != null;
    }
}
