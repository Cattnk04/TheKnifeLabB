package main.java.shared.dto;

public class RistoranteDTO {
    public String nomeRistorante;
    public String citta;
    public String nazione;
    public int fasciaPrezzo;
    public boolean delivery;
    public boolean prenotazioneOnline;
    public String tipoCucina;
    public RistoranteDTO(String nomeRistorante, String citta, String nazione, int fasciaPrezzo, boolean delivery, boolean prenotazioneOnline, String tipoCucina) {
        this.nomeRistorante = nomeRistorante;
        this.citta = citta;
        this.nazione = nazione;
        this.fasciaPrezzo = fasciaPrezzo;
        this.delivery = delivery;
        this.prenotazioneOnline = prenotazioneOnline;
        this.tipoCucina = tipoCucina;
    }
}
