package main.java.server.security;

import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class PasswordUtils {

    /**
     *
     */
    private PasswordUtils() {}

    /**
     *
      * @param password
     * @return
     */
    public static String hashBCrypt(String password) {
        if (password == null) {
            throw new IllegalArgumentException("La password non può essere null");
        }

        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     *
     * @param password
     * @param hash
     * @return
     */
    public static boolean verifyBCrypt(String password, String hash) {
        if (password == null || hash == null) {
            return false;
        }
        return BCrypt.checkpw(password, hash);
    }

    /**
     *
      * @param password
     * @return
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
     *
     * @param password
     * @param storedHash
     * @return
     */
    public static boolean verifySHA256(String password, String storedHash) {
        if (password == null || storedHash == null) {
            return false;
        }
        return hashSHA256(password).equals(storedHash);
    }

    /**
     *
     * @param hash
     * @return
     */
    public static boolean isBCryptHash(String hash) {
        return hash != null &&
                (hash.startsWith("$2a$")
                        || hash.startsWith("$2b$")
                        || hash.startsWith("$2y$"));
    }
}