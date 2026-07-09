package main.java.server.service;

import main.java.server.dao.PreferitiDAO;
import main.java.shared.domain.Preferito;
import main.java.shared.dto.PreferitiDTO;

import java.util.List;

/**
 *
 */
public class PreferitiService {

    private final PreferitiDAO preferitiDAO;

    /**
     *
     * @param preferitiDAO
     */
    public PreferitiService(PreferitiDAO preferitiDAO) {
        this.preferitiDAO = preferitiDAO;
    }

    /**
     *
      * @param dto
     * @return
     */
    public boolean aggiungiPreferito(PreferitiDTO dto) {

        if (dto == null) return false;

        String email = dto.getEmail();
        int idRistorante = dto.getIdRistorante();

        if (preferitiDAO.esistePreferito(email, idRistorante)) {
            return false;
        }

        return preferitiDAO.salvaPreferiti(email, idRistorante);
    }

    /**
     *
      * @param dto
     * @return
     */
    public boolean rimuoviPreferito(PreferitiDTO dto) {

        if (dto == null) return false;

        return preferitiDAO.cancellaPreferiti(
                dto.getEmail(),
                dto.getIdRistorante()
        );
    }

    /**
     *
      * @param dto
     * @return
     */
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

    /**
     *
      * @param email
     * @param idRistorante
     * @return
     */
    public boolean esistePreferito(String email, int idRistorante) {
        return preferitiDAO.esistePreferito(email, idRistorante);
    }

    /**
     *
      * @param email
     * @return
     */
    public List<Preferito> getPreferitiUtente(String email) {
        return preferitiDAO.getPreferitiByEmail(email);
    }
}

