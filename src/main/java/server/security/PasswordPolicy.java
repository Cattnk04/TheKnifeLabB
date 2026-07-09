package main.java.server.security;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Classe di utilità che definisce la policy di robustezza richiesta
 * per le password degli utenti.
 */
public class PasswordPolicy {

    /**
     * Verifica se la password rispetta la policy di robustezza richiesta:
     * lunghezza minima di 8 caratteri, presenza di almeno una lettera
     * maiuscola, una minuscola e una cifra numerica.
     *
     * @param password la password da verificare
     * @return true se la password rispetta la policy, false altrimenti (o se {@code null})
     */
    public static boolean isStrong(String password) {

        if (password == null) return false;

        boolean length = password.length() >= 8;
        boolean upper = password.chars().anyMatch(Character::isUpperCase);
        boolean lower = password.chars().anyMatch(Character::isLowerCase);
        boolean digit = password.chars().anyMatch(Character::isDigit);

        return length && upper && lower && digit;
    }
}
