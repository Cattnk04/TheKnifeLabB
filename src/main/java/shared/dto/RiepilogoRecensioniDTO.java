package main.java.shared.dto;

import java.io.Serializable;

public class RiepilogoRecensioniDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int numeroRecensioni;
    private final double mediaValutazioni;

    public RiepilogoRecensioniDTO(int numeroRecensioni, double mediaValutazioni) {
        this.numeroRecensioni = numeroRecensioni;
        this.mediaValutazioni = mediaValutazioni;
    }

    public int getNumeroRecensioni() {
        return numeroRecensioni;
    }

    public double getMediaValutazioni() {
        return mediaValutazioni;
    }
}