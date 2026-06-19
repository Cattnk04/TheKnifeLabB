package main.java.server.security;

public class PasswordService {

    public boolean verify(String plain, String hash) {
        if (plain == null || hash == null) {
            return false;
        }

        if (PasswordUtils.isBCryptHash(hash)) {
            return PasswordUtils.verifyBCrypt(plain, hash);
        }

        return PasswordUtils.verifySHA256(plain, hash);
    }

    public boolean isLegacy(String hash) {
        return hash != null && !PasswordUtils.isBCryptHash(hash);
    }

    public String upgradeToBCrypt(String plain) {
        if (plain == null) {
            throw new IllegalArgumentException("La password non può essere null");
        }
        return PasswordUtils.hashBCrypt(plain);
    }

    public String hash(String plainPassword) {
        if (plainPassword == null) {
            throw new IllegalArgumentException("La password non può essere null");
        }
        return PasswordUtils.hashBCrypt(plainPassword);
    }
}