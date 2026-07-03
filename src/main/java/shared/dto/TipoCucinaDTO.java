package main.java.shared.dto;

import java.io.Serializable;

public class TipoCucinaDTO implements Serializable {
    private int idTipoCucina;
    private String tipoCucina;

    public TipoCucinaDTO(int idTipoCucina, String tipoCucina) {
        this.idTipoCucina = idTipoCucina;
        this.tipoCucina = tipoCucina;
    }

    public int getIdTipoCucina() {
        return idTipoCucina;
    }

    public String getTipoCucina() { return tipoCucina; }

    @Override
    public String toString() {
        return tipoCucina;
    }

    public int getId() {
        return 0;
    }
}
