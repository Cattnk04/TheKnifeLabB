package main.java.server.service;

import main.java.server.dao.PreferitiDAO;
import main.java.shared.domain.Preferito;

import java.util.List;

public class PreferitiService {

    private final PreferitiDAO preferitiDAO = new PreferitiDAO();

    //Aggiungi preferito con controllo duplicati
    public boolean aggiungiPreferito(String email, int idRistorante){
        if (preferitiDAO.esistePreferito(email, idRistorante)) {
            return false;
        }
        return preferitiDAO.salvaPreferiti(email, idRistorante);
    }

    //Rimuovi preferito
    public boolean rimuoviPreferito(String email, int idRistorante){
        return preferitiDAO.cancellaPreferiti(email, idRistorante);
    }

    //Toggler preferito
    public boolean togglePreferito(String email, int idRistorante) {

        if (preferitiDAO.esistePreferito(email, idRistorante)) {
            return preferitiDAO.cancellaPreferiti(email, idRistorante);
        } else {
            return preferitiDAO.salvaPreferiti(email, idRistorante);
        }
    }

    //Check esistenza preferito
    public boolean esistePreferito(String email, int idRistorante) {
        return preferitiDAO.esistePreferito(email, idRistorante);
    }

    //Lista preferiti Utente
    public List<Preferito> getPreferitiUtente(String email) {
        return preferitiDAO.getPreferitiByEmail(email);
    }
}

