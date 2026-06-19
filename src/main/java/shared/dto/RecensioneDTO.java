package main.java.shared.dto;

import java.io.Serializable;

public class RecensioneDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private int idRistorante;
    private int valutazione;
    private String recensione;
    private String risposta;

    public RecensioneDTO(String email, int idRistorante, int valutazione, String recensione, String risposta) {
        this.email = email;
        this.idRistorante = idRistorante;
        this.valutazione = valutazione;
        this.recensione = recensione;
        this.risposta = risposta;
    }
    public RecensioneDTO(String email, int idRistorante, int valutazione, String recensione) {
        this(email, idRistorante, valutazione, recensione, null);
    }
    public RecensioneDTO(String email, int idRistorante) {
        this(email, idRistorante, -1, null, null);
    }

    public String getEmail() { return email; }
    public int getIdRistorante() { return idRistorante; }
    public int getValutazione() { return valutazione; }
    public String getRecensione() { return recensione; }
    public String getRisposta() { return risposta; }
    public void setValutazione(int valutazione) {
        if (valutazione < 1 || valutazione > 5) {
            throw new IllegalArgumentException("La valutazione deve essere compresa tra 1 e 5.");
        }

        this.valutazione = valutazione;
    }
}
