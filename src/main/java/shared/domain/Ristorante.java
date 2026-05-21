package main.java.shared.domain;

import java.util.Scanner;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.0
 *
 * Classe per gestione della generazione dell'elemento Ristorante.
 */

public class Ristorante {

    private String nome;
    private String emailRistoratore;
    private String nazione;
    private String citta;
    private String indirizzo;
    private double fasciaPrezzo;
    private boolean servizioDelivery;
    private boolean servizioPrenotazioneOnline;
    private String tipoCucina;
    public static final String FILE_RISTORANTI = "Data/Ristoranti.txt";

    /**
     * Costruttore della classe Ristorante che inizializza un nuovo ristorante
     * con le informazioni specificate. I valori di tipo String vengono trimmati
     * per rimuovere eventuali spazi bianchi iniziali e finali.
     *
     * @param nome il nome del ristorante
     * @param emailRistoratore l'email del ristoratore responsabile
     * @param nazione la nazione in cui si trova il ristorante
     * @param citta la città in cui si trova il ristorante
     * @param indirizzo l'indirizzo completo del ristorante
     * @param fasciaPrezzo la fascia di prezzo del ristorante (es. media, alta, ecc.)
     * @param servizioDelivery true se il ristorante offre servizio di consegna a domicilio
     * @param servizioPrenotazioneOnline true se il ristorante offre servizio di prenotazione online
     * @param tipoCucina il tipo di cucina offerta dal ristorante (es. italiana, giapponese, ecc.)
     */
    public Ristorante(String nome, String emailRistoratore, String nazione, String citta, String indirizzo, double fasciaPrezzo, boolean servizioDelivery, boolean servizioPrenotazioneOnline, String tipoCucina){
        this.nome = nome.trim();
        this.emailRistoratore = emailRistoratore.trim();
        this.nazione = nazione.trim();
        this.citta = citta.trim();
        this.indirizzo = indirizzo.trim();
        this.fasciaPrezzo = fasciaPrezzo;
        this.servizioDelivery = servizioDelivery;
        this.servizioPrenotazioneOnline = servizioPrenotazioneOnline;
        this.tipoCucina = tipoCucina.trim();
    }

    /**
     * Costruttore della classe Ristorante che inizializza un nuovo ristorante
     * associandolo all'email di un utente ristoratore e richiedendo l'inserimento
     * interattivo dei dati del ristorante tramite il metodo inserisciDatiRistorante().
     *
     * @param utenteRistoratore l'utente che rappresenta il ristoratore associato al ristorante
     */
    public Ristorante(Utente utenteRistoratore){
        this.emailRistoratore = utenteRistoratore.getEmail();
        inserisciDatiRistorante();
    }

    /**
     * Restituisce il nome del ristorante.
     *
     * @return il nome del ristorante
     */
    public String getNome(){
        return this.nome;
    }

    /**
     * Restituisce la nazione in cui si trova il ristorante.
     *
     * @return la nazione del ristorante
     */
    public String getNazione(){
        return this.nazione;
    }

    /**
     * Restituisce la città in cui si trova il ristorante.
     *
     * @return la città del ristorante
     */
    public String getCitta(){
        return this.citta;
    }

    /**
     * Restituisce l'indirizzo completo del ristorante.
     *
     * @return l'indirizzo del ristorante
     */
    public String getIndirizzo(){
        return this.indirizzo;
    }

    /**
     * Restituisce la fascia di prezzo del ristorante.
     *
     * @return la fascia di prezzo (double)
     */
    public double getFasciaPrezzo(){
        return this.fasciaPrezzo;
    }

    /**
     * Indica se il ristorante offre il servizio di delivery.
     *
     * @return true se è attivo il servizio di delivery, false altrimenti
     */
    public boolean getServizioDelivery(){
        return this.servizioDelivery;
    }

    /**
     * Indica se il ristorante offre il servizio di prenotazione online.
     *
     * @return true se è attivo il servizio di prenotazione online, false altrimenti
     */
    public boolean getServizioPrenotazioneOnline(){
        return this.servizioPrenotazioneOnline;
    }

    /**
     * Restituisce il tipo di cucina offerta dal ristorante.
     *
     * @return il tipo di cucina (es. italiana, cinese, ecc.)
     */
    public String getTipoCucina(){
        return this.tipoCucina;
    }

    /**
     * Restituisce l'email del ristoratore associato al ristorante.
     *
     * @return l'email del ristoratore
     */
    public Object getEmailRistoratore() {
        return emailRistoratore;
    }

    /**
     * Imposta il nome del ristorante.
     *
     * @param nome il nuovo nome del ristorante
     */
    public void setNome(String nome){
        this.nome = nome;
    }

    /**
     * Imposta la nazione in cui si trova il ristorante.
     *
     * @param nazione la nuova nazione del ristorante
     */
    public void setNazione(String nazione){
        this.nazione = nazione;
    }

    /**
     * Imposta la città in cui si trova il ristorante.
     *
     * @param citta la nuova città del ristorante
     */
    public void setCitta(String citta){
        this.citta = citta;
    }

    /**
     * Imposta l'indirizzo del ristorante.
     *
     * @param indirizzo il nuovo indirizzo del ristorante
     */
    public void setIndirizzo(String indirizzo){
        this.indirizzo = indirizzo;
    }

    /**
     * Imposta la fascia di prezzo del ristorante.
     *
     * @param fasciaPrezzo la nuova fascia di prezzo
     */
    public void setFasciaPrezzo(double fasciaPrezzo){
        this.fasciaPrezzo = fasciaPrezzo;
    }

    /**
     * Imposta se il servizio di delivery è attivo o meno.
     *
     * @param servizioDelivery true per attivare il servizio, false per disattivarlo
     */
    public void setServizioDelivery(boolean servizioDelivery){
        this.servizioDelivery = servizioDelivery;
    }

    /**
     * Imposta se il servizio di prenotazione online è attivo o meno.
     *
     * @param servizioPrenotazioneOnline true per attivare il servizio, false per disattivarlo
     */
    public void setServizioPrenotazioneOnline(boolean servizioPrenotazioneOnline){
        this.servizioPrenotazioneOnline = servizioPrenotazioneOnline;
    }

    /**
     * Imposta il tipo di cucina offerta dal ristorante.
     *
     * @param tipoCucina il nuovo tipo di cucina
     */
    public void setTipoCucina(String tipoCucina){
        this.tipoCucina = tipoCucina;
    }

    /**
     * Restituisce una rappresentazione testuale dell'oggetto Ristorante,
     * con tutti i suoi campi separati da una virgola.
     *
     * @return una stringa che rappresenta il ristorante con tutti i suoi attributi
     */
    //Metodo to string
    @Override
    public String toString(){
        return this.nome + ',' + this.emailRistoratore + ',' + this.nazione + ',' + this.citta + ',' + this.indirizzo + ',' + this.fasciaPrezzo + ','+ this.servizioDelivery + ',' + this.servizioPrenotazioneOnline + ',' + this.tipoCucina;
    }

    /**
     * Metodo privato che richiede all'utente di inserire interattivamente
     * tutti i dati necessari per configurare un oggetto Ristorante.
     * Chiede nome, nazione, città, indirizzo, fascia di prezzo,
     * se offre servizio delivery, se accetta prenotazioni online e tipo di cucina.
     *
     * Valida gli input per i servizi delivery e prenotazione online, accettando
     * solo 's' (sì) o 'n' (no), e continua a richiedere finché l'input non è corretto.
     */
    //Metodo per creare un ristorante
    private void inserisciDatiRistorante(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il nome del ristorante:");
        this.setNome(scanner.nextLine());
        System.out.print("Inserisci il nazione del ristorante:");
        this.setNazione(scanner.nextLine());
        System.out.print("Inserisci la citta del ristorante:");
        this.setCitta(scanner.nextLine());
        System.out.print("Inserisci l'indirizzo del ristorante:");
        this.setIndirizzo(scanner.nextLine());
        System.out.print("Inserisci il prezzo medio del ristorante:");
        this.setFasciaPrezzo(scanner.nextDouble());
        boolean valido;
        do{
            valido = true;
            System.out.print("Il ristorante fornisce il servizio delivery? [s/n]");
            String risposta = scanner.nextLine().trim().toLowerCase(); // Salva l'input in una variabile
            if(risposta.equals("s")){
                this.setServizioDelivery(true);
            } else if (risposta.equals("n")){
                this.setServizioDelivery(false);
            } else {
                System.out.println("Devi inserire 's' o 'n'");
                valido = false;
            }
        } while (!valido);
        do{
            valido = true;
            System.out.println("Il ristorante accetta prenotazioni online? [s/n]");
            String risposta = scanner.nextLine().trim().toLowerCase();
            if(risposta.equals("s")){
                this.setServizioPrenotazioneOnline(true);
            } else if (risposta.equals("n")){
                this.setServizioPrenotazioneOnline(false);
            } else {
                System.out.println("Devi inserire 's' o 'n'");
                valido = false;
            }
        } while (!valido);
        System.out.println("Inserisci il tipo di cucina del ristorante: ");
        this.setTipoCucina(scanner.nextLine());
    }
}
