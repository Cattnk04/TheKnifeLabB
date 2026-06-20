package main.java.shared.dto;

import java.io.Serializable;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * DTO utilizzato per trasferire le informazioni di login dell'utente.
 * <p>
 * Contiene le credenziali necessarie per l'autenticazione,
 * ovvero email e password.
 * Questo oggetto viene utilizzato nella comunicazione tra client e server.
 * </p>
 */

public class LoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String email;
    private final String password;

    /**
     * Costruisce un nuovo LoginDTO con email e password.
     *
     * @param email email dell'utente
     * @param password password dell'utente
     */
    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Restituisce l'email dell'utente.
     * @return email
     */
    public String getEmail() { return email; }

    /**
     * Restituisce la password dell'utente.
     * @return password
     */
    public String getPassword() { return password; }
}