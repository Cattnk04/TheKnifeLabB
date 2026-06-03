package main.java.server.service;

import main.java.server.dao.UtenteDAO;
import main.java.server.security.PasswordUtils;
import main.java.shared.domain.Utente;

import java.util.Optional;

public class UtenteService {

    private final UtenteDAO utenteDAO;

    // Dependency Injection (consigliato)
    public UtenteService(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    public boolean login(String email, String passwordInput) {

        try {
            Optional<Utente> optUtente = utenteDAO.trovaUtente(email);

            if (optUtente.isEmpty()) {
                return false;
            }

            Utente utente = optUtente.get();
            String hashSalvato = utente.getHashpwd();

            if (PasswordUtils.isBCryptHash(hashSalvato)) {
                return PasswordUtils.verifyBCrypt(passwordInput, hashSalvato);
            }

            boolean passwordCorretta = PasswordUtils.verifySHA256(passwordInput, hashSalvato);

            if (passwordCorretta) {
                String nuovoHash = PasswordUtils.hashBCrypt(passwordInput);
                utenteDAO.aggiornaPasswordHash(utente.getNomeUtente(), nuovoHash);
            }

            return passwordCorretta;

        } catch (Exception e) {
            // log dell'errore (meglio usare logger vero)
            System.err.println("Errore durante login: " + e.getMessage());

            return false; // fallback sicuro
        }
    }

    // REGISTRAZIONE
    public boolean registraUtente(Utente u, String passwordPlain) {

        if (utenteDAO.esisteUtente(u.getEmail())) {
            return false;
        }

        String hash = PasswordUtils.hashBCrypt(passwordPlain);

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
        return utenteDAO.cancellaUtente(email);
    }
}