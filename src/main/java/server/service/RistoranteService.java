package main.java.server.service;

import main.java.server.dao.RistoranteDAO;
import main.java.shared.domain.Ristorante;
import main.java.shared.enums.CampoRistorante;

import java.util.List;

public class RistoranteService {

    private final RistoranteDAO ristoranteDAO;

    public RistoranteService() {
        this.ristoranteDAO = new RistoranteDAO();
    }

    //Creazione
    public boolean creaRistorante(Ristorante ristorante){
        if (ristorante == null) {
            throw new IllegalArgumentException("Ristorante nullo");
        }
        if (!ValidationUtils.isValidName(ristorante.getNomeRistorante())) {
            throw new IllegalArgumentException("Nome ristorante non valido");
        }

        if (!ValidationUtils.isValidEmail(ristorante.getEmail())) {
            throw new IllegalArgumentException("Email non valida");
        }

        if (!ValidationUtils.isValidCitta(ristorante.getCitta())) {
            throw new IllegalArgumentException("Città non valida");
        }

        if (!ValidationUtils.isValidNazione(ristorante.getNazione())) {
            throw new IllegalArgumentException("Nazione non valida");
        }

        if (!ValidationUtils.isValidVia(ristorante.getVia())) {
            throw new IllegalArgumentException("Via non valida");
        }

        if (!ValidationUtils.isValidNumeroCivico(ristorante.getNumeroCivico())) {
            throw new IllegalArgumentException("Numero civico non valido");
        }

        if (ristoranteDAO.esisteRistorante(ristorante.getEmail())) {
            throw new IllegalArgumentException("Ristorante già esistente");
        }

        return ristoranteDAO.inserisciRistorante(ristorante);
    }

    // Update campo singolo
    public boolean aggiornaCampo(int idRistorante, CampoRistorante campo, Object valore) {
        if (idRistorante <= 0) {
            throw new IllegalArgumentException("ID non valido");
        }

        if (campo == null) {
            throw new IllegalArgumentException("Campo non valido");
        }

        if (valore == null) {
            throw new IllegalArgumentException("Valore nullo");
        }

        // validazioni specifiche per campo
        switch (campo) {
            case NOME -> {
                if (!ValidationUtils.isValidName(valore.toString())) {
                    throw new IllegalArgumentException("Nome non valido");
                }
            }
            case CITTA -> {
                if (!ValidationUtils.isValidCitta(valore.toString())) {
                    throw new IllegalArgumentException("Città non valida");
                }
            }
            case NAZIONE -> {
                if (!ValidationUtils.isValidNazione(valore.toString())) {
                    throw new IllegalArgumentException("Nazione non valida");
                }
            }
            case VIA -> {
                if (!ValidationUtils.isValidVia(valore.toString())) {
                    throw new IllegalArgumentException("Via non valida");
                }
            }

        }

        return ristoranteDAO.aggiornaCampo(idRistorante, campo, valore);
    }

    //Cancellazione
    public boolean cancellaRistorante(int idRistorante) {
        if (idRistorante <= 0) {
            throw new IllegalArgumentException("ID non valido");
        }
        return ristoranteDAO.rimuoviRistorante(idRistorante);
    }

    //Read
    public List<Ristorante> getTuttiRistoranti() {
        return ristoranteDAO.trovaTutti();
    }

    public List<Ristorante> cercaRistoranti(CampoRistorante campo, String valore) {

        if (campo == null) {
            throw new IllegalArgumentException("Campo nullo");
        }

        if (!ValidationUtils.isNotBlank(valore)) {
            throw new IllegalArgumentException("Valore ricerca non valido");
        }

        return ristoranteDAO.cercaPerCampo(campo, valore);
    }

    //Esiste
    public boolean esiste(String nome) {

        if (!ValidationUtils.isValidName(nome)) {
            return false;
        }

        return ristoranteDAO.esisteRistorante(nome);
    }
}
