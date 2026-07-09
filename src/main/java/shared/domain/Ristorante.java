package main.java.shared.domain;

import java.io.Serializable;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Classe per gestione della generazione dell'elemento Ristorante.
 */
public class Ristorante implements Serializable{

    private int idRistorante;
    private String nomeRistorante;
    private String email;
    private String nazione;
    private String citta;
    private String via;
    private int numeroCivico;
    private int fasciaPrezzo;
    private boolean delivery;
    private boolean prenotazioneOnline;
    private int idTipoCucina;

    /**
     * Costruttore della classe Ristorante che inizializza un nuovo ristorante
     * necessario per il funzionamento del DAO.
     */
    public Ristorante() {}

    /**
     * Costruttore della classe Ristorante che inizializza un nuovo ristorante
     * con le informazioni specificate. I valori di tipo String vengono trimmati
     * per rimuovere eventuali spazi bianchi iniziali e finali.
     *
     * @param idristorante       l'id del ristorante
     * @param nome               il nome del ristorante
     * @param email              l'email del ristoratore responsabile
     * @param citta              la città in cui si trova il ristorante
     * @param nazione            la nazione in cui si trova il ristorante
     * @param via                l'indirizzo del ristorante
     * @param numeroCivico       il numero civico del ristorante
     * @param fasciaPrezzo       la fascia di prezzo del ristorante (es. media, alta, ecc.)
     * @param delivery           true se il ristorante offre servizio di consegna a domicilio
     * @param prenotazioneOnline true se il ristorante offre servizio di prenotazione online
     * @param idTipoCucina       il tipo di cucina offerta dal ristorante (es. italiana, giapponese, ecc.)
     */
    public Ristorante(int idristorante, String nome, String email, String citta,String nazione, String via, int numeroCivico, int fasciaPrezzo, boolean delivery, boolean prenotazioneOnline, int idTipoCucina){
        this.idRistorante = idristorante;
        this.nomeRistorante = nome.trim();
        this.email = email.trim();
        this.citta = citta.trim();
        this.nazione = nazione.trim();
        this.via = via.trim();
        this.numeroCivico = numeroCivico;
        this.fasciaPrezzo = fasciaPrezzo;
        this.delivery = delivery;
        this.prenotazioneOnline = prenotazioneOnline;
        this.idTipoCucina = idTipoCucina;
    }

    /**
     * Restituisce l'id del Ristorante.
     * @return l'id del Ristorante
     */
    public int getIdRistorante(){
        return this.idRistorante;
    }

    /**
     * Restituisce il nome del ristorante.
     * @return il nome del ristorante
     */
    public String getNomeRistorante(){
        return this.nomeRistorante;
    }

    /**
     * Restituisce l'email del ristoratore associato al ristorante.
     * @return l'email del ristoratore
     */
    public String getEmail() {
        return email;
    }

    /**
     * Restituisce la nazione in cui si trova il ristorante.
     * @return la nazione del ristorante
     */
    public String getNazione(){
        return this.nazione;
    }

    /**
     * Restituisce la città in cui si trova il ristorante.
     * @return la città del ristorante
     */
    public String getCitta(){
        return this.citta;
    }

    /**
     * Restituisce la via del ristorante.
     * @return l'indirizzo del ristorante
     */
    public String getVia(){
        return this.via;
    }

    /**
     * Restituisce il numero civico del ristorante.
     * @return l'indirizzo del ristorante
     */
    public int getNumeroCivico(){
        return this.numeroCivico;
    }

    /**
     * Restituisce la fascia di prezzo del ristorante.
     * @return la fascia di prezzo (int)
     */
    public int getFasciaPrezzo(){
        return this.fasciaPrezzo;
    }

    /**
     * Indica se il ristorante offre il servizio di delivery.
     * @return true se è attivo il servizio di delivery, false altrimenti
     */
    public boolean getDelivery(){
        return this.delivery;
    }

    /**
     * Indica se il ristorante offre il servizio di prenotazione online.
     * @return true se è attivo il servizio di prenotazione online, false altrimenti
     */
    public boolean getPrenotazioneOnline(){
        return this.prenotazioneOnline;
    }

    /**
     * Restituisce il tipo di cucina offerta dal ristorante.
     * @return l'id del tipo di cucina (es. italiana, cinese, ecc.)
     */
    public int getIdTipoCucina(){
        return this.idTipoCucina;
    }

    /**
     * Imposta il nome del ristorante.
     * @param nome il nuovo nome del ristorante
     */
    public void setNomeRistorante(String nome){
        this.nomeRistorante = nome;
    }

    /**
     * Imposta il nome del ristorante.
     * @param email il nuovo nome del ristorante
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * Imposta la nazione in cui si trova il ristorante.
     * @param nazione la nuova nazione del ristorante
     */
    public void setNazione(String nazione){
        this.nazione = nazione;
    }

    /**
     * Imposta la città in cui si trova il ristorante.
     * @param citta la nuova città del ristorante
     */
    public void setCitta(String citta){
        this.citta = citta;
    }

    /**
     * Imposta la via del ristorante.
     * @param via la nuova via del ristorante
     */
    public void setVia(String via){
        this.via = via;
    }

    /**
     * Imposta il numerocivico del ristorante.
     * @param numeroCivico il nuovo indirizzo del ristorante
     */
    public void setNumeroCivico (int numeroCivico){
        this.numeroCivico = numeroCivico;
    }

    /**
     * Imposta la fascia di prezzo del ristorante.
     * @param fasciaPrezzo la nuova fascia di prezzo
     */
    public void setFasciaPrezzo(int fasciaPrezzo){
        this.fasciaPrezzo = fasciaPrezzo;
    }

    /**
     * Imposta se il servizio di delivery è attivo o meno.
     * @param delivery true per attivare il servizio, false per disattivarlo
     */
    public void setDelivery(boolean delivery){
        this.delivery = delivery;
    }

    /**
     * Imposta se il servizio di prenotazione online è attivo o meno.
     * @param prenotazioneOnline true per attivare il servizio, false per disattivarlo
     */
    public void setPrenotazioneOnline(boolean prenotazioneOnline){
        this.prenotazioneOnline = prenotazioneOnline;
    }

    /**
     * Imposta il tipo di cucina offerta dal ristorante.
     * @param idTipoCucina il nuovo tipo di cucina
     */
    public void setTipoCucina(int idTipoCucina){
        this.idTipoCucina = idTipoCucina;
    }

    /**
     * Restituisce una rappresentazione testuale dell'oggetto Ristorante,
     * con tutti i suoi campi separati da una virgola.
     * @return una stringa che rappresenta il ristorante con tutti i suoi attributi
     */
    @Override
    public String toString() {
        return idRistorante + "," +
                nomeRistorante + "," +
                email + "," +
                citta + "," +
                nazione + "," +
                via + "," +
                numeroCivico + "," +
                fasciaPrezzo + "," +
                delivery + "," +
                prenotazioneOnline + "," +
                idTipoCucina;
    }
}
