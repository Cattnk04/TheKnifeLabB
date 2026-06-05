package main.java.shared.dto;

public class RecensioneDTO {
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
}
