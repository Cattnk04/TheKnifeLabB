package main.java.shared.communication;

import java.io.Serializable;

public class Richiesta implements Serializable {

    private static final long serialVersionUID = 1L;

    public TipoRichieste tipoRichiesta;
    public Object contenuto;

    public Richiesta(TipoRichieste tipoRichiesta, Object contenuto) {
        this.tipoRichiesta = tipoRichiesta;
        this.contenuto = contenuto;
    }

    public TipoRichieste getTipoRichiesta() {
        return tipoRichiesta;
    }

    public Object getContenuto() {
        return contenuto;
    }
}
