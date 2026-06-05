package main.java.server.service;

import main.java.server.dao.UtenteDAO;
import main.java.server.security.*;
import main.java.server.service.ValidationUtils;
import main.java.shared.domain.Utente;
import main.java.shared.dto.LoginDTO;
import main.java.shared.dto.RegistrazioneDTO;

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
    public boolean login(LoginDTO dto) {

        if (dto == null) return false;

        String email = dto.getEmail();
        String password = dto.getPassword();

        if (!ValidationUtils.isValidEmail(email) ||
                !ValidationUtils.isValidPassword(password)) {
            return false;
        }

        Optional<Utente> opt = utenteDAO.trovaUtente(email);

        return opt.map(utente -> {

            String hash = utente.getHashpwd();

            boolean ok = passwordService.verify(password, hash);

            if (ok && passwordService.isLegacy(hash)) {
                String newHash = passwordService.upgradeToBCrypt(password);
                utenteDAO.aggiornaPasswordHash(email, newHash);
            }
            return ok;

        }).orElse(false);
    }

    // Registrazione
    public boolean registraUtente(RegistrazioneDTO dto) {

        if (dto == null) return false;

        if (!ValidationUtils.isValidEmail(dto.getEmail()) ||
                !PasswordPolicy.isStrong(dto.getPassword()) ||
                !ValidationUtils.isValidName(dto.getNome()) ||
                !ValidationUtils.isValidName(dto.getCognome()) ||
                !ValidationUtils.isValidCitta(dto.getCitta()) ||
                !ValidationUtils.isValidNazione(dto.getNazione())) {
            return false;
        }

        if (utenteDAO.esisteUtente(dto.getEmail())) {
            return false;
        }

        String hash = passwordService.hash(dto.getPassword());

        Utente utente = new Utente(
                dto.getEmail(),
                dto.getNome(),
                dto.getCognome(),
                hash,
                dto.getNazione(),
                dto.getCitta(),
                dto.isRistoratore()
        );

        return utenteDAO.registrazione(utente);
    }

    // CANCELLAZIONE
    public boolean cancellaUtente(String email) {
        return ValidationUtils.isValidEmail(email)
                && utenteDAO.cancellaUtente(email);
    }
}