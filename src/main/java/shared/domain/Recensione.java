package main.java.shared.domain;

import main.java.server.exception.RecensioniException;

import java.io.Serializable;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Classe per gestione della generazione dell'elemento Recensione.
 */
public class Recensione implements Serializable{

    private String email;
    private int idRistorante;
    private int valutazione;
    private String recensione;
    private String risposta;

    /**
     * Costruttore della classe Recensione che crea un'istanza
     * utilizzando direttamente tutti i dati della recensione.
     *
     * @param email l'email dell'utente che ha lasciato la recensione
     * @param idRistorante il nome del ristorante recensito
     * @param valutazione il punteggio dato al ristorante
     * @param recensione il testo della recensione
     * @param risposta la risposta alla recensione (può essere null)
     */
    public Recensione(int idRistorante, String email, int valutazione, String recensione, String risposta) {
        this.idRistorante = idRistorante;
        this.email = email;
        this.valutazione = valutazione;
        this.recensione = recensione;
        this.risposta = risposta;
    }

    /**
     * Restituisce l'email dell'utente che ha lasciato la recensione.
     *
     * @return la email dell'utente
     */
    public String getEmail(){
        return email;
    }

    /**
     * Restituisce il nome del ristorante recensito.
     *
     * @return il nome del ristorante
     */
    public int getIdRistorante(){
        return idRistorante;
    }

    /**
     * Restituisce la valutazione data al ristorante.
     *
     * @return la valutazione (intero)
     */
    public int getValutazione(){
        return valutazione;
    }

    /**
     * Restituisce il testo della recensione.
     *
     * @return la recensione scritta dall'utente
     */
    public String getRecensione(){
        return recensione;
    }

    /**
     * Restituisce la risposta data alla recensione.
     *
     * @return la risposta alla recensione, può essere null se non presente
     */
    public String getRisposta(){
        return risposta;
    }

    /**
     * Imposta o aggiorna il testo della recensione.
     *
     * @param recensione il nuovo testo della recensione
     */
    public void setRecensione(String recensione){
        this.recensione = recensione;
    }

    /**
     * Imposta o aggiorna la valutazione del ristorante.
     *
     * @param valutazione il nuovo punteggio
     * @throws RecensioniException se la valutazione non è compresa tra 1 e 5
     */
    public void setValutazione(int valutazione) {
        if (valutazione < 1 || valutazione > 5) {
            throw new RecensioniException("La valutazione deve essere compresa tra 1 e 5.");
        }

        this.valutazione = valutazione;
    }

    /**
     * Restituisce una rappresentazione testuale compatta dell'oggetto Recensione,
     * con i campi separati da asterischi '*'.
     *
     * @return la stringa rappresentativa dell'oggetto Recensione
     */
    @Override
    public String toString(){
        return this.email + '*' + this.idRistorante + '*' + this.valutazione + '*' + this.recensione + '*' + this.risposta;
    }

}