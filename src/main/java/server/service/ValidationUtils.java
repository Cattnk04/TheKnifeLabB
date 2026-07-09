package main.java.server.service;
import java.util.regex.Pattern;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Classe di utilità che raccoglie i metodi statici di validazione dei dati
 * in ingresso (email, password, nomi, indirizzi, ecc.), utilizzati nei
 * vari service per garantire la correttezza dei dati prima di persisterli.
 */
public class ValidationUtils {

    /**
     * Espressione regolare utilizzata per la validazione del formato dell'email.
     */
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    /**
     * Verifica se una stringa rappresenta un indirizzo email dal formato valido.
     *
     * @param email la stringa da validare
     * @return true se il formato dell'email è valido, false altrimenti (o se {@code null})
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_REGEX.matcher(email).matches();
    }

    /**
     * Verifica se una password rispetta la lunghezza minima richiesta.
     *
     * @param password la password da validare
     * @return true se la password ha almeno 8 caratteri, false altrimenti (o se {@code null})
     */
    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return password.length() >= 8;
    }

    /**
     * Verifica se una stringa non è nulla e non è vuota (o composta solo da spazi).
     *
     * @param value la stringa da verificare
     * @return true se la stringa contiene almeno un carattere non di spaziatura, false altrimenti
     */
    public static boolean isNotBlank(String value){
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Verifica se la lunghezza di una stringa (al netto degli spazi iniziali e finali)
     * rientra in un intervallo specificato.
     *
     * @param value la stringa da verificare
     * @param min lunghezza minima consentita
     * @param max lunghezza massima consentita
     * @return true se la lunghezza della stringa è compresa tra {@code min} e {@code max}, false altrimenti
     */
    public static boolean isValidStringLength(String value, int min, int max) {
        if (value == null) return false;
        int len = value.trim().length();
        return len >= min && len <= max;
    }

    /**
     * Verifica se una stringa è un nome (o cognome) valido: non vuota e lunga tra 2 e 50 caratteri.
     *
     * @param nome il nome da validare
     * @return true se il nome è valido, false altrimenti
     */
    public static boolean isValidName(String nome) {
        return isNotBlank(nome) && isValidStringLength(nome, 2, 50);
    }

    /**
     * Verifica se una stringa è un nome di città valido: non vuota e lunga tra 2 e 50 caratteri.
     *
     * @param citta la città da validare
     * @return true se la città è valida, false altrimenti
     */
    public static boolean isValidCitta(String citta) {
        return isNotBlank(citta) && isValidStringLength(citta, 2, 50);
    }

    /**
     * Verifica se una stringa è un nome di nazione valido: non vuota e lunga tra 2 e 50 caratteri.
     *
     * @param nazione la nazione da validare
     * @return true se la nazione è valida, false altrimenti
     */
    public static boolean isValidNazione(String nazione) {
        return isNotBlank(nazione) && isValidStringLength(nazione, 2, 50);
    }

    /**
     * Verifica se una stringa è un indirizzo (via) valido: non vuota e lunga tra 2 e 80 caratteri.
     *
     * @param via la via da validare
     * @return true se la via è valida, false altrimenti
     */
    public static boolean isValidVia(String via) {
        return isNotBlank(via) && isValidStringLength(via, 2, 80);
    }

    /**
     * Verifica se un numero civico è valido, ovvero compreso tra 1 e 100000.
     *
     * @param numero il numero civico da validare
     * @return true se il numero civico è valido, false altrimenti
     */
    public static boolean isValidNumeroCivico(int numero) {
        return numero > 0 && numero <= 100000;
    }

    /**
     * Verifica se un valore booleano (wrapper {@link Boolean}) è stato effettivamente impostato.
     *
     * @param value il valore booleano da verificare
     * @return true se il valore non è {@code null}, false altrimenti
     */
    public static boolean isValidBoolean(Boolean value) {
        return value != null;
    }
}
