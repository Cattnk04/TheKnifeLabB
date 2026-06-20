package main.java.shared.dto;

import java.io.Serializable;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * DTO utilizzato per rappresentare le informazioni di un ristorante.
 * <p>
 * Contiene i dati principali relativi al ristorante, inclusa la posizione,
 * le caratteristiche del servizio e il tipo di cucina.
 * Viene utilizzato per il trasferimento dati tra client e server.
 * </p>
 */
public class RistoranteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nomeRistorante;
    private final String citta;
    private final String nazione;
    private final String via;
    private final int numeroCivico;
    private final int fasciaPrezzo;
    private final boolean delivery;
    private final boolean prenotazioneOnline;
    private final int tipoCucina;

    /**
     * Costruisce un nuovo DTO di ristorante.
     *
     * @param nomeRistorante nome del ristorante
     * @param citta città in cui si trova il ristorante
     * @param nazione nazione in cui si trova il ristorante
     * @param via via dell'indirizzo del ristorante
     * @param numeroCivico numero civico del ristorante
     * @param fasciaPrezzo fascia di prezzo del ristorante
     * @param delivery indica se il ristorante offre servizio di delivery
     * @param prenotazioneOnline indica se è possibile prenotare online
     * @param tipoCucina identificativo del tipo di cucina offerta
     */
    public RistoranteDTO(String nomeRistorante, String citta, String nazione,
                         String via, int numeroCivico, int fasciaPrezzo,
                         boolean delivery, boolean prenotazioneOnline,
                         int tipoCucina) {

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

    /**
     * Restituisce il nome del ristorante.
     * @return nome ristorante
     */
    public String getNomeRistorante() {return nomeRistorante;}

    /**
     * Restituisce la città del ristorante.
     * @return città
     */
    public String getCitta() {return citta;}

    /**
     * Restituisce la nazione del ristorante.
     * @return nazione
     */
    public String getNazione() {return nazione;}

    /**
     * Restituisce la via dell'indirizzo del ristorante.
     * @return via
     */
    public String getVia() {return via;}

    /**
     * Restituisce il numero civico del ristorante.
     * @return numero civico
     */
    public int getNumeroCivico() {return numeroCivico;}

    /**
     * Restituisce la fascia di prezzo del ristorante.
     * @return fascia prezzo
     */
    public int getFasciaPrezzo() {return fasciaPrezzo;}

    /**
     * Indica se il ristorante offre il servizio di delivery.
     * @return true se disponibile delivery, false altrimenti
     */
    public boolean isDelivery() {return delivery;}

    /**
     * Indica se è possibile effettuare prenotazioni online.
     * @return true se prenotazione online disponibile
     */
    public boolean isPrenotazioneOnline() {return prenotazioneOnline;}

    /**
     * Restituisce il tipo di cucina del ristorante.
     * @return identificativo tipo cucina
     */
    public int getTipoCucina() {return tipoCucina;}
}