package main.java.server.service;

import main.java.server.dao.UtenteDAO;
import main.java.server.security.*;
import main.java.server.service.ValidationUtils;
import main.java.shared.domain.Utente;

import java.util.Optional;

public class UtenteService {

    private final UtenteDAO utenteDAO;
    private final PasswordService passwordService;

    // Dependency Injection
    public UtenteService(UtenteDAO utenteDAO, PasswordService passwordService) {
        this.utenteDAO = utenteDAO;
        this.passwordService = passwordService;
    }

    // LOGIN
    public boolean login(String email, String passwordInput) {

        if (!ValidationUtils.isValidEmail(email) ||
                !ValidationUtils.isValidPassword(passwordInput)) {
            return false;
        }

        Optional<Utente> optUtente = utenteDAO.trovaUtente(email);

        return optUtente
                .map(utente -> {

                    String hashSalvato = utente.getHashpwd();

                    boolean passwordCorretta = passwordService.verify(passwordInput, hashSalvato);

                    // migrazione automatica SHA -> BCrypt
                    if (passwordCorretta && passwordService.isLegacy(hashSalvato)) {
                        String nuovoHash = passwordService.upgradeToBCrypt(passwordInput);
                        utenteDAO.aggiornaPasswordHash(utente.getEmail(), nuovoHash);
                    }

                    return passwordCorretta;
                })
                .orElse(false);
    }

    // Registrazione
    public boolean registraUtente(Utente u, String passwordPlain) {

        if (u == null ||
                !ValidationUtils.isValidEmail(u.getEmail()) ||
                !PasswordPolicy.isStrong(passwordPlain)) {
            return false;
        }
        if (utenteDAO.esisteUtente(u.getEmail())) {
            return false;
        }

        String hash = passwordService.hash(passwordPlain);

        Utente nuovo = new Utente(
                u.getEmail(),
                u.getNomeUtente(),
                u.getCognomeUtente(),
                hash,
                u.getCitta(),
                u.getNazione(),
                u.getRistoratore()
        );

        return utenteDAO.registrazione(nuovo);
    }

    // CANCELLAZIONE
    public boolean cancellaUtente(String email) {
        if (!ValidationUtils.isValidEmail(email)) {
            return false;
        }
        return utenteDAO.cancellaUtente(email);
    }
}