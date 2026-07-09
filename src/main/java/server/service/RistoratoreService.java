package main.java.server.service;

import main.java.server.dao.*;
import main.java.shared.domain.*;
import main.java.shared.enums.*;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public class RistoratoreService {

    private final UtenteDAO utenteDAO;
    private final RistoranteDAO ristoranteDAO;
    private final RecensioneDAO recensioneDAO;

    /**
     *
     * @param ristoranteDAO
     * @param utenteDAO
     * @param recensioneDAO
     */
    public RistoratoreService(RistoranteDAO ristoranteDAO, UtenteDAO utenteDAO, RecensioneDAO recensioneDAO) {
        this.ristoranteDAO = ristoranteDAO;
        this.utenteDAO = utenteDAO;
        this.recensioneDAO = recensioneDAO;
    }

    /**
     *
      * @param email
     * @return
     */
    public Optional<Utente> getAccountRistoratore(String email) {
        return utenteDAO.trovaUtente(email);
    }

    /**
     *
     * @param email
     * @param campo
     * @param valore
     * @return
     */
    public boolean aggiornaProfilo(String email, CampoUtente campo, Object valore) {
        return utenteDAO.aggiornamentoUtente(email, campo, valore);
    }

    /**
     *
     * @param email
     * @return
     */
    public boolean eliminaAccount(String email) {
        return utenteDAO.cancellaUtente(email);
    }

    /**
     *
     * @param email
     * @return
     */
    public boolean esisteUtente(String email) {
        return utenteDAO.esisteUtente(email);
    }

    /**
     *
      * @return
     */
    public List<Ristorante> getTuttiRistoranti() {
        return ristoranteDAO.trovaTutti();
    }

    /**
     *
     * @param ristorante
     * @return
     */
    public boolean inserisciRistorante(Ristorante ristorante) {
        return ristoranteDAO.inserisciRistorante(ristorante);
    }

    /**
     *
     * @param idRistorante
     * @param campo
     * @param valore
     * @return
     */
    public boolean aggiornaCampoRistorante(int idRistorante, CampoRistorante campo, Object valore) {
        return ristoranteDAO.aggiornaCampo(idRistorante, campo, valore);
    }

    /**
     *
     * @param idRistorante
     * @return
     */
    public boolean eliminaRistorante(int idRistorante) {
        return ristoranteDAO.rimuoviRistorante(idRistorante);
    }

    /**
     *
      * @param idRistorante
     * @return
     */
    public List<?> getRecensioniRistorante(int idRistorante) {
        return recensioneDAO.getRecensioniByRistorante(idRistorante);
    }

    /**
     *
     * @param idRistorante
     * @return
     */
    public double mediaVotoRistorante(int idRistorante) {
        List<?> recensioni = recensioneDAO.getRecensioniByRistorante(idRistorante);

        if (recensioni.isEmpty()) return 0.0;

        return recensioni.stream()
                .mapToInt(r -> ((main.java.shared.domain.Recensione) r).getValutazione())
                .average()
                .orElse(0.0);
    }

    /**
     *
     * @param idRistorante
     * @return
     */
    public int numeroRecensioni(int idRistorante) {
        return recensioneDAO.getRecensioniByRistorante(idRistorante).size();
    }
}
