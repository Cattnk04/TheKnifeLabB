package main.java.shared.domain;

import java.io.Serializable;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.0
 *
 * Classe per gestione della generazione dell'elemento Preferito.
 */

public class Preferito implements Serializable {

    String email;
    int idRistorante;
    //public static final String FILE_PREFERITI = "Data/Preferiti.txt";

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
     * Costruttore della classe Preferito che crea un'istanza
     * utilizzando un oggetto Utente e una ListaRistoranti.
     * Viene richiesto all'utente di inserire il nome del ristorante
     * da aggiungere alla lista dei preferiti.
     *
     * @param utente l'oggetto Utente che aggiunge il preferito
     * @param listaRistoranti la lista di ristoranti da cui cercare il preferito
     */
    /*
    public Preferito(Utente utente, ListaRistoranti listaRistoranti) {
        this.email = utente.getEmail();
        Ristorante ristorante = listaRistoranti.cercaPerNome("\nInserisci il nome del ristorante che vuoi aggiungere alla tua lista dei preferiti: ");
        if(ristorante != null)
            this.idRistorante = ristorante.getNomeRistorante();
        else
            this.idRistorante = null;
    }*/

    //Metodi Get
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

    public String setEmail(String email){
        return this.email = email;
    }

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
    //Metodo to string
    @Override
    public String toString(){
        return this.email+ "," + idRistorante;
    }
}
