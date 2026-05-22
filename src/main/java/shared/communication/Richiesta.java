package main.java.shared.communication;

import java.io.Serializable;

public class Richiesta implements Serializable{
    public TipoRichieste tipoRichiesta;
    public Object contenuto;
    public Richiesta(TipoRichieste tipoRichieste, Object contenuto) {
        this.tipoRichiesta = tipoRichieste;
        this.contenuto = contenuto;
    }
    public TipoRichieste getTipoRichiesta() {
        return tipoRichiesta;
    }
    public Object getContenuto() {
        return contenuto;
    }
}
