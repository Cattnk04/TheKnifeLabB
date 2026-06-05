package main.java.server.security;

public class PasswordService {

    public boolean verify(String plain, String hash) {

        if (PasswordUtils.isBCryptHash(hash)) {
            return PasswordUtils.verifyBCrypt(plain, hash);
        }

        return PasswordUtils.verifySHA256(plain, hash);
    }

    public boolean isLegacy(String hash) {
        return !PasswordUtils.isBCryptHash(hash);
    }

    public String upgradeToBCrypt(String plain) {
        return PasswordUtils.hashBCrypt(plain);
    }

    public String hash(String plainPassword) {
        return PasswordUtils.hashBCrypt(plainPassword);
    }
}