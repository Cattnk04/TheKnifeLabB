package main.java.server.service;

import main.java.shared.domain.Recensione;
import main.java.server.dao.RecensioneDAO;
import main.java.shared.dto.RiepilogoRecensioniDTO;
import main.java.shared.enums.CampoRecensione;

import java.util.List;

/**
 *
 */
public class RecensioneService {

    private final RecensioneDAO dao;

    /* --------
    UTENTE
    -------- */
    /**
     *
     * @param dao
     */
    public RecensioneService(RecensioneDAO dao) {
        this.dao = dao;
    }

    /**
     *
      * @param r
     * @return
     */
    public boolean creaRecensione(Recensione r) {

        if (r == null) return false;

        if(r.getValutazione() < 1 || r.getValutazione() > 5){
            throw new IllegalArgumentException("La valutazione deve essere compresa tra 1 e 5");
        }

        return dao.salvaRecensioni(r);
    }

    /**
     *
      * @param idRistorante
     * @param email
     * @param testo
     * @return
     */
    public boolean modificaRecensione(int idRistorante, String email, String testo) {
        return dao.aggiornaRecensione(idRistorante, email, CampoRecensione.RECENSIONE, testo);
    }

    /**
     *
     * @param idRistorante
     * @param email
     * @param valutazione
     * @return
     */
    public boolean modificaValutazione(int idRistorante, String email, int valutazione) {
        return dao.aggiornaRecensione(idRistorante, email, CampoRecensione.VALUTAZIONE, valutazione);
    }

    /**
     *
      * @param idRistorante
     * @param email
     * @return
     */
    public boolean cancellaRecensione(int idRistorante, String email) {
        return dao.cancellaRecensioni(idRistorante, email);
    }

    /**
     *
      * @param email
     * @return
     */
    public List<Recensione> getRecensioniUtente(String email) {
        return dao.getRecensioniByEmail(email);
    }

    /**
     *
      * @param idRistorante
     * @return
     */
    public List<Recensione> getRecensioniRistorante(int idRistorante) {
        return dao.getRecensioniByRistorante(idRistorante);
    }

    /* --------
    RISTORATORE
    -------- */
    /**
     *
      * @param idRistorante
     * @return
     */
    public List<Recensione> getRecensioni (int idRistorante) {
        return dao.getRecensioniByRistorante(idRistorante);
    }

    /**
     *
      * @param idRistorante
     * @param email
     * @param risposta
     * @return
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
     *
      * @param idRistorante
     * @param email
     * @param risposta
     * @return
     */
    public boolean aggiornaRisposta(int idRistorante, String email, String risposta) {
        return dao.aggiornaRecensione(idRistorante, email,
                CampoRecensione.RISPOSTA,
                risposta);
    }

    /**
     *
      * @param idRistorante
     * @return
     */
    public RiepilogoRecensioniDTO getRiepilogo(int idRistorante) {
        return dao.getRiepilogo(idRistorante);
    }
}
