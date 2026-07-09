package main.java.server.security;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Servizio per la gestione delle password degli utenti.
 * <p>
 * Si occupa della verifica delle credenziali e dell'hashing delle password,
 * supportando sia il formato BCrypt (attuale) sia il formato legacy SHA-256,
 * per permettere la migrazione graduale degli hash già presenti nel database
 * (vedi {@link PasswordUtils}).
 * </p>
 */
public class PasswordService {

    /**
     * Verifica se una password in chiaro corrisponde all'hash memorizzato,
     * riconoscendo automaticamente se l'hash è in formato BCrypt o legacy SHA-256.
     *
     * @param plain la password in chiaro inserita dall'utente
     * @param hash l'hash memorizzato con cui effettuare il confronto
     * @return true se la password corrisponde all'hash, false altrimenti (o se un parametro è {@code null})
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
     * Indica se l'hash fornito è in formato legacy (SHA-256) anziché BCrypt,
     * per determinare se la password dell'utente necessita di una migrazione.
     *
     * @param hash l'hash da controllare
     * @return true se l'hash non è in formato BCrypt (quindi legacy), false altrimenti
     */
    public boolean isLegacy(String hash) {
        return hash != null && !PasswordUtils.isBCryptHash(hash);
    }

    /**
     * Genera un nuovo hash BCrypt a partire dalla password in chiaro,
     * da utilizzare per migrare un utente da un hash legacy (SHA-256) a BCrypt
     * dopo un login effettuato con successo.
     *
     * @param plain la password in chiaro da hashare
     * @return il nuovo hash BCrypt della password
     * @throws IllegalArgumentException se {@code plain} è {@code null}
     */
    public String upgradeToBCrypt(String plain) {
        if (plain == null) {
            throw new IllegalArgumentException("La password non può essere null");
        }
        return PasswordUtils.hashBCrypt(plain);
    }

    /**
     * Genera l'hash BCrypt di una nuova password, da utilizzare in fase
     * di registrazione o cambio password.
     *
     * @param plainPassword la password in chiaro da hashare
     * @return l'hash BCrypt della password
     * @throws IllegalArgumentException se {@code plainPassword} è {@code null}
     */
    public String hash(String plainPassword) {
        if (plainPassword == null) {
            throw new IllegalArgumentException("La password non può essere null");
        }
        return PasswordUtils.hashBCrypt(plainPassword);
    }
}