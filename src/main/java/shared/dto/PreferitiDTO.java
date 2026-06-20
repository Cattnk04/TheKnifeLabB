package main.java.shared.dto;

import java.io.Serializable;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * DTO utilizzato per rappresentare l'associazione tra un utente e un ristorante
 * nei preferiti.
 * <p>
 * Viene utilizzato per trasferire i dati necessari ad aggiungere o rimuovere
 * un ristorante dalla lista dei preferiti di un utente.
 * </p>
 */

public class PreferitiDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String email;
    private final int idRistorante;

    /**
     * Costruisce un nuovo PreferitiDTO.
     *
     * @param email email dell'utente che gestisce i preferiti
     * @param idRistorante identificativo del ristorante
     */
    public PreferitiDTO(String email, int idRistorante) {
        this.email = email;
        this.idRistorante = idRistorante;
    }

    /**
     * Restituisce l'email dell'utente.
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Restituisce l'identificativo del ristorante.
     * @return id del ristorante
     */
    public int getIdRistorante() {
        return idRistorante;
    }
}
