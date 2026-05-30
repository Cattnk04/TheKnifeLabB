package main.java.server.service;

import main.java.server.dao.UtenteDAO;
import main.java.server.security.PasswordUtils;
import main.java.shared.domain.Utente;

import java.util.Optional;

public class AuthService {

    private final UtenteDAO utenteDAO;

    // Dependency Injection (consigliato)
    public AuthService(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    public boolean login(String email, String passwordInput) {

        Optional<Utente> optUtente = utenteDAO.trovaUtente(email);

        if (optUtente.isEmpty()) {
            return false;
        }

        Utente utente = optUtente.get();
        String hashSalvato = utente.getHashpwd();

        boolean passwordCorretta;

        // Caso 1: già BCrypt
        if (PasswordUtils.isBCryptHash(hashSalvato)) {

            passwordCorretta = PasswordUtils.verifyBCrypt(
                    passwordInput,
                    hashSalvato
            );

        }
        // Caso 2: legacy SHA-256 + upgrade automatico
        else {
            passwordCorretta = PasswordUtils.verifySHA256(
                    passwordInput,
                    hashSalvato
            );

            if (passwordCorretta) {
                String nuovoHash = PasswordUtils.hashBCrypt(passwordInput);

                utenteDAO.aggiornaPasswordHash(
                        utente.getNomeUtente(),
                        nuovoHash
                );
            }
        }
        return passwordCorretta;
    }
}