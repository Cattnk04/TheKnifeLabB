package main.java.server.security;

/**
 *
 */
public class PasswordService {

    /**
     *
     * @param plain
     * @param hash
     * @return
     */
    public boolean verify(String plain, String hash) {
        if (plain == null || hash == null) {
            return false;
        }

        if (PasswordUtils.isBCryptHash(hash)) {
            return PasswordUtils.verifyBCrypt(plain, hash);
        }

        return PasswordUtils.verifySHA256(plain, hash);
    }

    /**
     *
     * @param hash
     * @return
     */
    public boolean isLegacy(String hash) {
        return hash != null && !PasswordUtils.isBCryptHash(hash);
    }

    /**
     *
     * @param plain
     * @return
     */
    public String upgradeToBCrypt(String plain) {
        if (plain == null) {
            throw new IllegalArgumentException("La password non può essere null");
        }
        return PasswordUtils.hashBCrypt(plain);
    }

    /**
     *
     * @param plainPassword
     * @return
     */
    public String hash(String plainPassword) {
        if (plainPassword == null) {
            throw new IllegalArgumentException("La password non può essere null");
        }
        return PasswordUtils.hashBCrypt(plainPassword);
    }
}