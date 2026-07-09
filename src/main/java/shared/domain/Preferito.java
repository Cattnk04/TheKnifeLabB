package main.java.shared.domain;

import java.io.Serializable;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Classe per gestione della generazione dell'elemento Preferito.
 */
public class Preferito implements Serializable {

    String email;
    int idRistorante;

    /**
     * Costruttore della classe Preferito che crea un'istanza vuota necessario per il funzionamento del DAO.
     */
    public Preferito() {
    }

    /**
     * Costruttore della classe Preferito che crea un'istanza
     * utilizzando direttamente l'email dell'utente e il nome del ristorante.
     *
     * @param email la email dell'utente che aggiunge il preferito
     * @param idRistorante il nome del ristorante da aggiungere ai preferiti
     */
    public Preferito(String email, int idRistorante) {
        this.email = email;
        this.idRistorante = idRistorante;
    }

    /**
     * Restituisce l'email dell'utente associato al preferito.
     *
     * @return la email dell'utente
     */
    public String getEmail() {
        return email;
    }
    /**
     * Restituisce il nome del ristorante preferito.
     *
     * @return il nome del ristorante
     */
    public int getIdRistorante() {
        return idRistorante;
    }

    /**
     *
     * @param email
     * @return
     */
    public String setEmail(String email){
        return this.email = email;
    }

    /**
     *
     * @param IdRistorante
     * @return
     */
    public int setIdRistorante(int IdRistorante){
        return this.idRistorante = IdRistorante;
    }


    /**
     * Restituisce una rappresentazione testuale dell'oggetto Preferito,
     * composta dall'email dell'utente e dal nome del ristorante,
     * separati da una virgola.
     *
     * @return la stringa rappresentativa dell'oggetto Preferito
     */
    @Override
    public String toString(){
        return this.email+ "," + idRistorante;
    }
}
