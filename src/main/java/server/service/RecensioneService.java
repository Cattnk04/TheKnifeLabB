package main.java.server.service;

import main.java.shared.domain.Recensione;
import main.java.server.dao.RecensioneDAO;
import main.java.shared.dto.RiepilogoRecensioniDTO;
import main.java.shared.enums.CampoRecensione;

import java.util.List;

public class RecensioneService {

    private final RecensioneDAO dao = new RecensioneDAO();

    /* --------
    UTENTE
    -------- */

    //Crea recensione
    public boolean creaRecensione(Recensione r) {

        if(r.getValutazione() < 1 || r.getValutazione() > 5){
            throw new IllegalArgumentException("La valutazione deve essere compresa tra 1 e 5");
        }

        return dao.salvaRecensioni(r);
    }

    //Modifica recensione
    public boolean modificaRecensione(int idRistorante, String email, String testo) {
        return dao.aggiornaRecensione(idRistorante, email, CampoRecensione.RECENSIONE, testo);
    }

    public boolean modificaValutazione(int idRistorante, String email, int valutazione) {
        return dao.aggiornaRecensione(idRistorante, email, CampoRecensione.VALUTAZIONE, valutazione);
    }

    //Cancella recensione
    public boolean cancellaRecensione(int idRistorante, String email) {
        return dao.cancellaRecensioni(idRistorante, email);
    }

    //Lista recensioni utente
    public List<Recensione> getRecensioniUtente(String email) {
        return dao.getRecensioniByEmail(email);
    }

    //Lista recensioni ristorante (quelli che visualliza l'utente)
    public List<Recensione> getRecensioniRistorante(int idRistorante) {
        return dao.getRecensioniByRistorante(idRistorante);
    }

    /* --------
    RISTORATORE
    -------- */
    //Vedere tutte le recensioni
    public List<Recensione> getRecensioni (int idRistorante) {
        return dao.getRecensioniByRistorante(idRistorante);
    }

    //Risposta recensioni
    public boolean rispostaRecensione (int idRistorante, String email, String risposta){

        if(risposta == null || risposta.isBlank()) {
            throw new IllegalArgumentException("Risposta vuota");
        }
        if(risposta.length() > 255) {
            throw new IllegalArgumentException("Risposta troppo lunga");
        }

        return dao.rispostaRecensione(idRistorante, email, risposta);
    }

    //Aggiorna risposta (overide)
    public boolean aggiornaRisposta(int idRistorante, String email, String risposta) {
        return dao.aggiornaRecensione(idRistorante, email,
                CampoRecensione.RISPOSTA,
                risposta);
    }

    //Riepilogo recensioni
    public RiepilogoRecensioniDTO getRiepilogo(int idRistorante) {
        return dao.getRiepilogo(idRistorante);
    }
}
