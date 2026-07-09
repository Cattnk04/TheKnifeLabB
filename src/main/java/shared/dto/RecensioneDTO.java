package main.java.shared.dto;

import java.io.Serializable;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * DTO utilizzato per rappresentare una recensione di un utente su un ristorante.
 * <p>
 * Contiene le informazioni relative alla valutazione, al testo della recensione
 * e a un'eventuale risposta del ristoratore.
 * Viene utilizzato per il trasferimento dati tra client e server.
 * </p>
 */

public class RecensioneDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private int idRistorante;
    private int valutazione;
    private String recensione;
    private String risposta;

    public RecensioneDTO() {}
    /**
     * Costruisce una recensione completa.
     *
     * @param email email dell'utente autore della recensione
     * @param idRistorante identificativo del ristorante recensito
     * @param valutazione voto assegnato (1-5)
     * @param recensione testo della recensione
     * @param risposta eventuale risposta del ristoratore
     */
    public RecensioneDTO(String email, int idRistorante, int valutazione, String recensione, String risposta) {
        this.email = email;
        this.idRistorante = idRistorante;
        this.valutazione = valutazione;
        this.recensione = recensione;
        this.risposta = risposta;
    }

    /**
     * Costruisce una recensione senza risposta del ristoratore.
     *
     * @param email
     * @param idRistorante identificativo del ristorante recensito
     * @param valutazione  voto assegnato (1-5)
     * @param recensione   testo della recensione
     */
    public RecensioneDTO(String email, int idRistorante, int valutazione, String recensione) {
        this(null, idRistorante, valutazione, recensione, null);
    }

    /**
     * Costruisce un DTO minimale contenente solo identificativi.
     * Utile per operazioni di ricerca o eliminazione.
     *
     * @param email email dell'utente
     * @param idRistorante identificativo del ristorante
     */
    public RecensioneDTO(String email, int idRistorante) {
        this(email, idRistorante, -1, null, null);
    }

    /**
     * Restituisce l'email dell'utente autore della recensione.
     * @return email utente
     */
    public String getEmail() { return email; }

    /**
     * Restituisce l'identificativo del ristorante.
     * @return id ristorante
     */
    public int getIdRistorante() { return idRistorante; }

    /**
     * Restituisce la valutazione assegnata.
     * @return voto (1-5)
     */
    public int getValutazione() { return valutazione; }

    /**
     * Restituisce il testo della recensione.
     * @return recensione
     */
    public String getRecensione() { return recensione; }

    /**
     * Restituisce l'eventuale risposta del ristoratore.
     * @return risposta oppure null
     */
    public String getRisposta() { return risposta; }

    /**
     * Imposta la valutazione della recensione.
     * <p>
     * Il valore deve essere compreso tra 1 e 5, altrimenti viene lanciata un'eccezione.
     * </p>
     *
     * @param valutazione voto da assegnare (1-5)
     * @throws IllegalArgumentException se il valore non è valido
     */
    public void setValutazione(int valutazione) {
        if (valutazione < 1 || valutazione > 5) {
            throw new IllegalArgumentException("La valutazione deve essere compresa tra 1 e 5.");
        }

        this.valutazione = valutazione;
    }

    public void setRisposta(String risposta) {
        this.risposta = risposta;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setRecensione(String recensione) {
        this.recensione = recensione;
    }
}
