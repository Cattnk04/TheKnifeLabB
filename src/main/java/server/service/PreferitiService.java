package main.java.server.service;

import main.java.server.dao.PreferitiDAO;
import main.java.shared.domain.Preferito;
import main.java.shared.dto.PreferitiDTO;

import java.util.List;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Service che espone la logica applicativa relativa alla gestione dei
 * ristoranti preferiti degli utenti, delegando l'accesso ai dati al
 * {@link PreferitiDAO}.
 */
public class PreferitiService {

    private final PreferitiDAO preferitiDAO;

    /**
     * Costruisce un nuovo service per la gestione dei preferiti.
     *
     * @param preferitiDAO il DAO da utilizzare per l'accesso ai dati dei preferiti
     */
    public PreferitiService(PreferitiDAO preferitiDAO) {
        this.preferitiDAO = preferitiDAO;
    }

    /**
     * Aggiunge un ristorante ai preferiti di un utente, se non è già presente.
     *
     * @param dto il {@link PreferitiDTO} con email dell'utente e id del ristorante
     * @return true se il preferito viene aggiunto con successo, false se {@code dto} è {@code null},
     * se il preferito esiste già o se il salvataggio fallisce
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
     * Rimuove un ristorante dai preferiti di un utente.
     *
     * @param dto il {@link PreferitiDTO} con email dell'utente e id del ristorante
     * @return true se la rimozione va a buon fine, false se {@code dto} è {@code null}
     * o se l'operazione fallisce
     */
    public boolean rimuoviPreferito(PreferitiDTO dto) {

        if (dto == null) return false;

        return preferitiDAO.cancellaPreferiti(
                dto.getEmail(),
                dto.getIdRistorante()
        );
    }

    /**
     * Inverte lo stato di preferito di un ristorante per un utente: se il ristorante
     * è già tra i preferiti viene rimosso, altrimenti viene aggiunto.
     *
     * @param dto il {@link PreferitiDTO} con email dell'utente e id del ristorante
     * @return true se l'operazione (aggiunta o rimozione) va a buon fine, false se {@code dto}
     * è {@code null} o se l'operazione fallisce
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
     * Verifica se un ristorante è già tra i preferiti di un utente.
     *
     * @param email email dell'utente
     * @param idRistorante id del ristorante da verificare
     * @return true se il preferito esiste, false altrimenti
     */
    public boolean esistePreferito(String email, int idRistorante) {
        return preferitiDAO.esistePreferito(email, idRistorante);
    }

    /**
     * Recupera l'elenco dei preferiti di un utente.
     *
     * @param email email dell'utente di cui recuperare i preferiti
     * @return la lista dei preferiti dell'utente
     */
    public List<Preferito> getPreferitiUtente(String email) {
        return preferitiDAO.getPreferitiByEmail(email);
    }
}

