package main.java.shared.dto;


public class RistoranteDTO {

    private final String nomeRistorante;
    private final String citta;
    private final String nazione;
    private final String via;
    private final int numeroCivico;
    private final int fasciaPrezzo;
    private final boolean delivery;
    private final boolean prenotazioneOnline;
    private final String tipoCucina;

    public RistoranteDTO(String nomeRistorante, String citta, String nazione,
                         String via, int numeroCivico, int fasciaPrezzo,
                         boolean delivery, boolean prenotazioneOnline,
                         String tipoCucina) {

        this.nomeRistorante = nomeRistorante;
        this.citta = citta;
        this.nazione = nazione;
        this.via = via;
        this.numeroCivico = numeroCivico;
        this.fasciaPrezzo = fasciaPrezzo;
        this.delivery = delivery;
        this.prenotazioneOnline = prenotazioneOnline;
        this.tipoCucina = tipoCucina;
    }

    public String getNomeRistorante() { return nomeRistorante; }
    public String getCitta() { return citta; }
    public String getNazione() { return nazione; }
    public String getVia() { return via; }
    public int getNumeroCivico() { return numeroCivico; }
    public int getFasciaPrezzo() { return fasciaPrezzo; }
    public boolean isDelivery() { return delivery; }
    public boolean isPrenotazioneOnline() { return prenotazioneOnline; }
    public String getTipoCucina() { return tipoCucina; }
}
