package main.java.server.service;

import main.java.server.dao.UtenteDAO;
import main.java.server.security.*;
import main.java.server.service.ValidationUtils;
import main.java.shared.domain.Utente;
import main.java.shared.dto.LoginDTO;
import main.java.shared.dto.RegistrazioneDTO;
import main.java.shared.enums.CampoUtente;

import java.util.Optional;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Service che espone la logica applicativa relativa agli utenti: login,
 * registrazione, recupero, modifica ed eliminazione dell'account. Si occupa
 * della validazione dei dati (tramite {@link ValidationUtils} e
 * {@link PasswordPolicy}) e della gestione delle password (tramite
 * {@link PasswordService}), delegando l'accesso ai dati allo {@link UtenteDAO}.
 */
public class UtenteService {

    private final UtenteDAO utenteDAO;
    private final PasswordService passwordService;

    /**
     * Costruisce un nuovo service per la gestione degli utenti.
     *
     * @param utenteDAO il DAO da utilizzare per l'accesso ai dati degli utenti
     * @param passwordService il servizio da utilizzare per la verifica e l'hashing delle password
     */
    public UtenteService(UtenteDAO utenteDAO, PasswordService passwordService) {
        this.utenteDAO = utenteDAO;
        this.passwordService = passwordService;
    }

    /**
     * Effettua il login di un utente, verificando email e password. Se la password
     * è corretta ma memorizzata con l'algoritmo legacy (SHA-256), viene automaticamente
     * aggiornata al formato BCrypt.
     *
     * @param dto il {@link LoginDTO} con le credenziali inserite dall'utente
     * @return true se il login va a buon fine, false se {@code dto} è {@code null}, se i dati
     * non sono validi, se l'utente non esiste o se la password è errata
     */
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

    /**
     * Registra un nuovo utente, dopo aver validato email, robustezza della password,
     * nome, cognome, città e nazione, e aver verificato che l'email non sia già in uso.
     * La password viene salvata come hash BCrypt.
     *
     * @param dto il {@link RegistrazioneDTO} con i dati del nuovo utente
     * @return true se la registrazione va a buon fine, false se {@code dto} è {@code null},
     * se un dato non supera la validazione o se l'email è già registrata
     */
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

    /**
     * Recupera i dati di un utente a partire dalla sua email.
     *
     * @param email email dell'utente da cercare
     * @return il {@link RegistrazioneDTO} con i dati dell'utente (password esclusa), oppure
     * {@code null} se l'email non è valida o l'utente non esiste
     */
    public RegistrazioneDTO getUtente(String email){
        if(!ValidationUtils.isValidEmail(email)){
            System.out.println("Recupero utente fallito: email non valida");
            return null;
        }
        Optional<Utente> opt = utenteDAO.trovaUtente(email);
        if(opt.isEmpty()){
            System.out.println("Recupero utente fallito: utente insesistente con questa email: " + email);
            return null;
        }

        Utente utente = opt.get();
        return new RegistrazioneDTO(
                utente.getNomeUtente(),
                utente.getCognomeUtente(),
                utente.getEmail(),
                null,
                utente.getCitta(),
                utente.getNazione(),
                utente.getRistoratore()
        );
    }

    /**
     * Modifica i dati anagrafici (nome, cognome, città, nazione) di un utente esistente,
     * dopo averli validati.
     *
     * @param dto il {@link RegistrazioneDTO} con i nuovi dati dell'utente
     * @return true se tutti gli aggiornamenti vanno a buon fine, false se {@code dto} è {@code null}
     * o se i dati non superano la validazione
     */
    public boolean modificaUtente(RegistrazioneDTO dto) {
        if (dto == null || !ValidationUtils.isValidEmail(dto.getEmail())) {
            System.out.println("Modifica fallita: email non valida");
            return false;
        }
        if (!ValidationUtils.isValidName(dto.getNome()) ||
                !ValidationUtils.isValidName(dto.getCognome()) ||
                !ValidationUtils.isValidCitta(dto.getCitta()) ||
                !ValidationUtils.isValidNazione(dto.getNazione())) {
            System.out.println("Modifica fallita: dati non validi");
            return false;
        }

        String email = dto.getEmail();

        boolean ok = utenteDAO.aggiornamentoUtente(email, CampoUtente.NOMEUTENTE, dto.getNome());
        ok &= utenteDAO.aggiornamentoUtente(email, CampoUtente.COGNOMEUTENTE, dto.getCognome());
        ok &= utenteDAO.aggiornamentoUtente(email, CampoUtente.CITTA, dto.getCitta());
        ok &= utenteDAO.aggiornamentoUtente(email, CampoUtente.NAZIONE, dto.getNazione());
        //ok &= utenteDAO.aggiornamentoUtente(email, CampoUtente.PASSWORD, dto.getPassword());
        //ok &= utenteDAO.aggiornamentoUtente(email, CampoUtente.RISTORATORE, dto.isRistoratore());
        //ok &= utenteDAO.aggiornamentoUtente(email, CampoUtente.EMAIL, dto.getEmail());
        //DECIDERE SE SERVONO

        return ok;
    }

    /**
     * Elimina l'account di un utente, dopo aver verificato che l'email sia valida.
     *
     * @param email email dell'utente da eliminare
     * @return true se l'eliminazione va a buon fine, false se l'email non è valida
     * o l'eliminazione fallisce
     */
    public boolean cancellaUtente(String email) {
        return ValidationUtils.isValidEmail(email)
                && utenteDAO.cancellaUtente(email);
    }
}