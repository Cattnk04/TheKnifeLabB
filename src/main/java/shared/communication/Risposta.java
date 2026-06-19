package main.java.shared.communication;

import java.io.Serializable;

public class Risposta implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean successo = false;
    private Object contenuto;
    private String msg;

    public Risposta(boolean successo, Object contenuto, String msg) {
        this.successo = successo;
        this.contenuto = contenuto;
        this.msg = msg;
    }

    public Object getContenuto() {
        return contenuto;
    }

    public String getMsg() {
        return msg;
    }

    public boolean getSuccesso() {
        return successo;
    }
}