package main.java.server.security;

import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Classe di utilità che fornisce i metodi statici per l'hashing e la
 * verifica delle password, sia con l'algoritmo BCrypt (attuale) sia
 * con SHA-256 (formato legacy, mantenuto solo per compatibilità con
 * gli hash già presenti nel database).
 */
public class PasswordUtils {

    /**
     * Costruttore privato: la classe espone solo membri statici e non è
     * pensata per essere istanziata.
     */
    private PasswordUtils() {}

    /**
     * Genera l'hash BCrypt di una password, utilizzando un salt casuale
     * con costo (work factor) pari a 12.
     *
     * @param password la password in chiaro da hashare
     * @return l'hash BCrypt della password
     * @throws IllegalArgumentException se {@code password} è {@code null}
     */
    public static String hashBCrypt(String password) {
        if (password == null) {
            throw new IllegalArgumentException("La password non può essere null");
        }

        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * Verifica se una password in chiaro corrisponde a un hash BCrypt.
     *
     * @param password la password in chiaro da verificare
     * @param hash l'hash BCrypt con cui effettuare il confronto
     * @return true se la password corrisponde all'hash, false altrimenti (o se un parametro è {@code null})
     */
    public static boolean verifyBCrypt(String password, String hash) {
        if (password == null || hash == null) {
            return false;
        }
        return BCrypt.checkpw(password, hash);
    }

    /**
     * Genera l'hash SHA-256 di una password, rappresentato come stringa
     * esadecimale. Algoritmo mantenuto solo per compatibilità con gli
     * hash legacy già presenti nel database (vedi {@link PasswordService}).
     *
     * @param password la password in chiaro da hashare
     * @return l'hash SHA-256 della password, in formato esadecimale
     * @throws IllegalArgumentException se {@code password} è {@code null}
     * @throws RuntimeException se l'algoritmo SHA-256 non è disponibile
     */
    public static String hashSHA256(String password) {
        if (password == null) {
            throw new IllegalArgumentException("La password non può essere null");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();

            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica se una password in chiaro corrisponde a un hash SHA-256
     * memorizzato, ricalcolando l'hash della password e confrontandolo
     * con quello salvato.
     *
     * @param password la password in chiaro da verificare
     * @param storedHash l'hash SHA-256 memorizzato con cui effettuare il confronto
     * @return true se la password corrisponde all'hash, false altrimenti (o se un parametro è {@code null})
     */
    public static boolean verifySHA256(String password, String storedHash) {
        if (password == null || storedHash == null) {
            return false;
        }
        return hashSHA256(password).equals(storedHash);
    }

    /**
     * Verifica se una stringa rappresenta un hash in formato BCrypt,
     * controllando la presenza dei prefissi caratteristici ({@code $2a$},
     * {@code $2b$} o {@code $2y$}).
     *
     * @param hash la stringa da controllare
     * @return true se la stringa è un hash BCrypt, false altrimenti (o se {@code null})
     */
    public static boolean isBCryptHash(String hash) {
        return hash != null &&
                (hash.startsWith("$2a$")
                        || hash.startsWith("$2b$")
                        || hash.startsWith("$2y$"));
    }
}