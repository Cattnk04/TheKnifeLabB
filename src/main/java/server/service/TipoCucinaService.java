package main.java.server.service;

import main.java.server.dao.TipoCucinaDAO;
import main.java.shared.dto.TipoCucinaDTO;

import java.util.List;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Service che espone la logica applicativa relativa ai tipi di cucina,
 * delegando l'accesso ai dati al {@link TipoCucinaDAO}.
 */
public class TipoCucinaService {
    private final TipoCucinaDAO tipoCucinaDAO;

    /**
     * Costruisce un nuovo service per la gestione dei tipi di cucina.
     *
     * @param tipoCucinaDAO il DAO da utilizzare per l'accesso ai dati dei tipi di cucina
     */
    public TipoCucinaService(TipoCucinaDAO tipoCucinaDAO) {
        this.tipoCucinaDAO = tipoCucinaDAO;
    }

    /**
     * Recupera l'elenco di tutti i tipi di cucina disponibili nel sistema.
     *
     * @return la lista dei tipi di cucina
     */
    public List<TipoCucinaDTO> getTipoCucina() {
        return tipoCucinaDAO.getTipoCucina();
    }
}
