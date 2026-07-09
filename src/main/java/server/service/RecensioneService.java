package main.java.server.service;

import main.java.shared.domain.Recensione;
import main.java.server.dao.RecensioneDAO;
import main.java.shared.dto.RiepilogoRecensioniDTO;
import main.java.shared.enums.CampoRecensione;

import java.util.List;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Service che espone la logica applicativa relativa alle recensioni,
 * sia dal punto di vista dell'utente che le scrive sia da quello del
 * ristoratore che vi risponde. Delega l'accesso ai dati al {@link RecensioneDAO}.
 */
public class RecensioneService {

    private final RecensioneDAO dao;

    /* --------
    UTENTE
    -------- */
    /**
     * Costruisce un nuovo service per la gestione delle recensioni.
     *
     * @param dao il DAO da utilizzare per l'accesso ai dati delle recensioni
     */
    public RecensioneService(RecensioneDAO dao) {
        this.dao = dao;
    }

    /**
     * Crea una nuova recensione, dopo aver verificato che la valutazione sia
     * compresa nell'intervallo consentito (1-5).
     *
     * @param r la {@link Recensione} da creare
     * @return true se la recensione viene creata con successo, false se {@code r} è {@code null}
     * o se il salvataggio fallisce
     * @throws IllegalArgumentException se la valutazione non è compresa tra 1 e 5
     */
    public boolean creaRecensione(Recensione r) {

        if (r == null) return false;

        if(r.getValutazione() < 1 || r.getValutazione() > 5){
            throw new IllegalArgumentException("La valutazione deve essere compresa tra 1 e 5");
        }

        return dao.salvaRecensioni(r);
    }

    /**
     * Modifica il testo di una recensione esistente.
     *
     * @param idRistorante id del ristorante recensito
     * @param email email dell'autore della recensione
     * @param testo il nuovo testo della recensione
     * @return true se la modifica va a buon fine, false altrimenti
     */
    public boolean modificaRecensione(int idRistorante, String email, String testo) {
        return dao.aggiornaRecensione(idRistorante, email, CampoRecensione.RECENSIONE, testo);
    }

    /**
     * Modifica la valutazione di una recensione esistente.
     *
     * @param idRistorante id del ristorante recensito
     * @param email email dell'autore della recensione
     * @param valutazione il nuovo punteggio da assegnare
     * @return true se la modifica va a buon fine, false altrimenti
     */
    public boolean modificaValutazione(int idRistorante, String email, int valutazione) {
        return dao.aggiornaRecensione(idRistorante, email, CampoRecensione.VALUTAZIONE, valutazione);
    }

    /**
     * Elimina una recensione esistente.
     *
     * @param idRistorante id del ristorante recensito
     * @param email email dell'autore della recensione da eliminare
     * @return true se l'eliminazione va a buon fine, false altrimenti
     */
    public boolean cancellaRecensione(int idRistorante, String email) {
        return dao.cancellaRecensioni(idRistorante, email);
    }

    /**
     * Recupera tutte le recensioni scritte da un determinato utente.
     *
     * @param email email dell'utente autore delle recensioni
     * @return la lista delle recensioni scritte dall'utente
     */
    public List<Recensione> getRecensioniUtente(String email) {
        return dao.getRecensioniByEmail(email);
    }

    /**
     * Recupera tutte le recensioni relative a un determinato ristorante.
     *
     * @param idRistorante id del ristorante di cui recuperare le recensioni
     * @return la lista delle recensioni del ristorante
     */
    public List<Recensione> getRecensioniRistorante(int idRistorante) {
        return dao.getRecensioniByRistorante(idRistorante);
    }

    /* --------
    RISTORATORE
    -------- */
    /**
     * Recupera tutte le recensioni relative a un determinato ristorante, per la
     * visualizzazione lato ristoratore. Equivalente a {@link #getRecensioniRistorante(int)}.
     *
     * @param idRistorante id del ristorante di cui recuperare le recensioni
     * @return la lista delle recensioni del ristorante
     */
    public List<Recensione> getRecensioni (int idRistorante) {
        return dao.getRecensioniByRistorante(idRistorante);
    }

    /**
     * Inserisce la risposta del ristoratore a una recensione, dopo aver verificato
     * che il testo non sia vuoto e non superi la lunghezza massima consentita (255 caratteri).
     *
     * @param idRistorante id del ristorante recensito
     * @param email email dell'autore della recensione
     * @param risposta testo della risposta da inserire
     * @return true se la risposta viene inserita con successo, false altrimenti
     * @throws IllegalArgumentException se la risposta è vuota o supera i 255 caratteri
     */
    public boolean rispostaRecensione(int idRistorante, String email, String risposta) {

        System.out.println("id"+ idRistorante);
        System.out.println("email"+ email);
        System.out.println("risposta"+ risposta);

        if (risposta == null || risposta.isBlank()) {
            throw new IllegalArgumentException("Risposta vuota");
        }

        if (risposta.length() > 255) {
            throw new IllegalArgumentException("Risposta troppo lunga");
        }

        return dao.rispostaRecensione(idRistorante, email, risposta);
    }

    /**
     * Aggiorna il testo di una risposta già esistente a una recensione.
     *
     * @param idRistorante id del ristorante recensito
     * @param email email dell'autore della recensione
     * @param risposta il nuovo testo della risposta
     * @return true se l'aggiornamento va a buon fine, false altrimenti
     */
    public boolean aggiornaRisposta(int idRistorante, String email, String risposta) {
        return dao.aggiornaRecensione(idRistorante, email,
                CampoRecensione.RISPOSTA,
                risposta);
    }

    /**
     * Recupera il riepilogo delle recensioni di un ristorante (numero totale e
     * media delle valutazioni).
     *
     * @param idRistorante id del ristorante di cui calcolare il riepilogo
     * @return il {@link RiepilogoRecensioniDTO} calcolato per il ristorante
     */
    public RiepilogoRecensioniDTO getRiepilogo(int idRistorante) {
        return dao.getRiepilogo(idRistorante);
    }
}
