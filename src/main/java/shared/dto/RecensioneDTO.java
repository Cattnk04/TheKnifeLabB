package main.java.shared.dto;

public class RecensioneDTO {
    public String email;
    public int ristoranteID;
    public int valutazione;
    public String recensione;
    public String risposta;
    public RecensioneDTO(String email, int ristoranteID, int valutazione, String recensione, String risposta) {
        this.email = email;
        this.ristoranteID = ristoranteID;
        this.valutazione = valutazione;
        this.recensione = recensione;
        this.risposta = risposta;
    }
    public RecensioneDTO(String email, int ristoranteID, int valutazione, String recensione) {
        this(email, ristoranteID, valutazione, recensione, null);
    }
    public RecensioneDTO(String email, int ristoranteID) {
        this(email, ristoranteID, -1, null, null);
    }

}
