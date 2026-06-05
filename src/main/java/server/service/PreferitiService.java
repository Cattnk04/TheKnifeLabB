package main.java.server.service;

import main.java.server.dao.PreferitiDAO;
import main.java.shared.domain.Preferito;
import main.java.shared.dto.PreferitiDTO;

import java.util.List;

public class PreferitiService {

    private final PreferitiDAO preferitiDAO;

    public PreferitiService(PreferitiDAO preferitiDAO) {
        this.preferitiDAO = preferitiDAO;
    }

    //Aggiungi preferito con controllo duplicati
    public boolean aggiungiPreferito(PreferitiDTO dto) {

        if (dto == null) return false;

        String email = dto.getEmail();
        int idRistorante = dto.getIdRistorante();

        if (preferitiDAO.esistePreferito(email, idRistorante)) {
            return false;
        }

        return preferitiDAO.salvaPreferiti(email, idRistorante);
    }

    //Rimuovi preferito
    public boolean rimuoviPreferito(PreferitiDTO dto) {

        if (dto == null) return false;

        return preferitiDAO.cancellaPreferiti(
                dto.getEmail(),
                dto.getIdRistorante()
        );
    }

    //Toggler preferito
    public boolean togglePreferito(PreferitiDTO dto) {

        if (dto == null) return false;

        String email = dto.getEmail();
        int idRistorante = dto.getIdRistorante();

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

