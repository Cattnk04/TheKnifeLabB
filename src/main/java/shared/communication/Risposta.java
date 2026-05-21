package main.java.shared.communication;

import java.io.Serializable;

public class Risposta implements Serializable {
    public TipoRichieste tipoRichiesta;
    public Object contenuto;
    public String msg;
    public Risposta() {}
}
