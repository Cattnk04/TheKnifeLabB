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
                password == null ||
                password.isBlank()) {
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

        if (dto == null) {
            System.out.println("Registrazione fallita: DTO null");
            return false;
        }
        if (!ValidationUtils.isValidEmail(dto.getEmail())) {
            System.out.println("Registrazione fallita: email non valida -> " + dto.getEmail());
            return false;
        }
        if (!PasswordPolicy.isStrong(dto.getPassword())) {
            System.out.println("Registrazione fallita: password non abbastanza forte");
            return false;
        }
        if (!ValidationUtils.isValidName(dto.getNome())) {
            System.out.println("Registrazione fallita: nome non valido -> " + dto.getNome());
            return false;
        }
        if (!ValidationUtils.isValidName(dto.getCognome())) {
            System.out.println("Registrazione fallita: cognome non valido -> " + dto.getCognome());
            return false;
        }
        if (!ValidationUtils.isValidCitta(dto.getCitta())) {
            System.out.println("Registrazione fallita: città non valida -> " + dto.getCitta());
            return false;
        }
        if (!ValidationUtils.isValidNazione(dto.getNazione())) {
            System.out.println("Registrazione fallita: nazione non valida -> " + dto.getNazione());
            return false;
        }
        if (utenteDAO.esisteUtente(dto.getEmail())) {
            System.out.println("Registrazione fallita: utente già esistente -> " + dto.getEmail());
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
                dto.getCitta(),
                dto.getNazione(),
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