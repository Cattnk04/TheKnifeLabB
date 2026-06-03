package main.java.shared.dto;

public class RiepilogoRecensioniDTO {

    private int numeroRecensioni;
    private double mediaValutazioni;

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