package main.java.shared.dto;

import java.io.Serializable;

/**
 *
 */
public class TipoCucinaDTO implements Serializable {
    private int idTipoCucina;
    private String tipoCucina;

    /**
     *
     * @param idTipoCucina
     * @param tipoCucina
     */
    public TipoCucinaDTO(int idTipoCucina, String tipoCucina) {
        this.idTipoCucina = idTipoCucina;
        this.tipoCucina = tipoCucina;
    }

    /**
     *
     * @return
     */
    public int getIdTipoCucina() {
        return idTipoCucina;
    }

    /**
     *
     * @return
     */
    public String getTipoCucina() { return tipoCucina; }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return tipoCucina;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return idTipoCucina;
    }
}
