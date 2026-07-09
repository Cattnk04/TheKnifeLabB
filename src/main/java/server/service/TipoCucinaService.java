package main.java.server.service;

import main.java.server.dao.TipoCucinaDAO;
import main.java.shared.dto.TipoCucinaDTO;

import java.util.List;

/**
 *
 */
public class TipoCucinaService {
    private final TipoCucinaDAO tipoCucinaDAO;

    /**
     *
     * @param tipoCucinaDAO
     */
    public TipoCucinaService(TipoCucinaDAO tipoCucinaDAO) {
        this.tipoCucinaDAO = tipoCucinaDAO;
    }

    /**
     *
     * @return
     */
    public List<TipoCucinaDTO> getTipoCucina() {
        return tipoCucinaDAO.getTipoCucina();
    }
}
