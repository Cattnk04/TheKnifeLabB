package main.java.server.service;

import main.java.server.dao.UtenteDAO;
import main.java.server.security.PasswordUtils;
import main.java.shared.domain.Utente;

import java.util.Optional;

public class AuthService {

    private final UtenteDAO utenteDAO = new UtenteDAO();

    public boolean login(String email, String passwordInserita) {

        Optional<Utente> utente = utenteDAO.trovaUtente(email);

        if (utente.isEmpty()) {
            return false;
        }

        String hashSalvato = utente.get().getHashpwd();

        if (PasswordUtils.isBCryptHash(hashSalvato)) {
            return PasswordUtils.verifyBCrypt(
                    passwordInserita,
                    hashSalvato
            );
        }

        boolean match = PasswordUtils.verifySHA256(
                passwordInserita,
                hashSalvato
        );

        if (!match) {
            return false;
        }
        String nuovoHash =
                PasswordUtils.hashBCrypt(passwordInserita);
        utenteDAO.aggiornaPasswordHash(
                utente.get().getNomeUtente(),
                nuovoHash
        );
        return true;
    }
}