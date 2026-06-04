package main.java.server.service;

import main.java.server.dao.*;
import main.java.shared.domain.*;
import main.java.shared.enums.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



public class RistoratoreService {

    private final UtenteDAO utenteDAO;
    private final RistoranteDAO ristoranteDAO;
    private final RecensioneDAO recensioneDAO;

    public RistoratoreService(UtenteDAO utenteDAO, RistoranteDAO ristoranteDAO, RecensioneDAO recensioneDAO) {
        this.utenteDAO = utenteDAO;
        this.ristoranteDAO = ristoranteDAO;
        this.recensioneDAO = recensioneDAO;
    }

    //Account Ristoratore
    public Optional<Utente> getAccountRistoratore(String email) {
        return utenteDAO.trovaUtente(email);
    }

    public boolean aggiornaProfilo(String email, CampoUtente campo, Object valore) {
        return utenteDAO.aggiornamentoUtente(email, campo, valore);
    }

    public boolean eliminaAccount(String email) {
        return utenteDAO.cancellaUtente(email);
    }

    public boolean esisteUtente(String email) {
        return utenteDAO.esisteUtente(email);
    }

    //Ristoranti
    public List<Ristorante> getTuttiRistoranti() {
        return ristoranteDAO.trovaTutti();
    }

    public boolean inserisciRistorante(Ristorante ristorante) {
        return ristoranteDAO.inserisciRistorante(ristorante);
    }

    public boolean aggiornaCampoRistorante(int idRistorante, CampoRistorante campo, Object valore) {
        return ristoranteDAO.aggiornaCampo(idRistorante, campo, valore);
    }

    public boolean eliminaRistorante(int idRistorante) {
        return ristoranteDAO.rimuoviRistorante(idRistorante);
    }

    //Dashboard
    public List<?> getRecensioniRistorante(int idRistorante) {
        return recensioneDAO.getRecensioniByRistorante(idRistorante);
    }

    public double mediaVotoRistorante(int idRistorante) {
        List<?> recensioni = recensioneDAO.getRecensioniByRistorante(idRistorante);

        if (recensioni.isEmpty()) return 0.0;

        return recensioni.stream()
                .mapToInt(r -> ((main.java.shared.domain.Recensione) r).getValutazione())
                .average()
                .orElse(0.0);
    }

    public int numeroRecensioni(int idRistorante) {
        return recensioneDAO.getRecensioniByRistorante(idRistorante).size();
    }
}
