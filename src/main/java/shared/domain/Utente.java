package main.java.shared.domain;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Classe per gestione della generazione dell'elemento Utente.
 */
public class Utente implements Serializable{

    private String email;
    private String nomeUtente;
    private String cognomeUtente;
    private String hashpwd;
    private String nazione;
    private String citta;
    private boolean ristoratore;

    /**
     * Costruttore della classe Utente che inizializza un nuovo utente con le informazioni fornite.
     * I valori di tipo String vengono trimmati per rimuovere eventuali spazi bianchi iniziali e finali.
     *
     * @param email l'email dell'utente
     * @param nomeUtente il nome dell'utente
     * @param cognomeUtente il cognome dell'utente
     * @param hashpwd  la password dell'utente
     * @param nazione la nazione di residenza dell'utente
     * @param citta la città di residenza dell'utente
     * @param ristoratore indica se l'utente è un ristoratore (true) o un cliente (false)
     * @throws RuntimeException se i dati forniti non rispettano qualche vincolo (da dettagliare)
     */
    public Utente(String email, String nomeUtente, String cognomeUtente, String hashpwd, String citta, String nazione, boolean ristoratore) {
        this.email = email.trim().toLowerCase();
        this.nomeUtente = nomeUtente.trim();
        this.cognomeUtente = cognomeUtente.trim();
        this.hashpwd = hashpwd;
        this.citta = citta.trim().toLowerCase();
        this.nazione = nazione.trim().toLowerCase();
        this.ristoratore = ristoratore;
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return il nome dell'utente
     */
    public String getNomeUtente(){
        return this.nomeUtente;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return il cognome dell'utente
     */
    public String getCognomeUtente(){
        return this.cognomeUtente;
    }

    /**
     * Restituisce l'email dell'utente.
     *
     * @return l'email dell'utente
     */
    public String getEmail(){
        return this.email;
    }

    /**
     * Restituisce la nazione di residenza dell'utente.
     *
     * @return la nazione dell'utente
     */
    public String getNazione(){
        return this.nazione;
    }

    /**
     * Restituisce la città di residenza dell'utente.
     *
     * @return la città dell'utente
     */
    public String getCitta(){
        return this.citta;
    }

    /**
     * Indica se l'utente è un ristoratore.
     *
     * @return true se l'utente è ristoratore, false altrimenti
     */
    public boolean getRistoratore(){
        return this.ristoratore;
    }

    /**
     * Restituisce la password dell'utente.
     *
     * @return la password dell'utente
     */
    public String getHashpwd(){
        return this.hashpwd;
    }

    /**
     * Imposta il nome dell'utente.
     *
     * @param nomeUtente il nuovo nome da impostare
     */
    public void setNomeUtente (String nomeUtente){
        this.nomeUtente = nomeUtente;
    }

    /**
     * Imposta il cognome dell'utente.
     *
     * @param cognomeUtente il nuovo cognome da impostare
     */
    public void setCognome(String cognomeUtente){
        this.cognomeUtente = cognomeUtente;
    }

    /**
     * Imposta la nazione di residenza dell'utente.
     *
     * @param nazione la nuova nazione da impostare
     */
    public void setNazione(String nazione){
        this.nazione = nazione;
    }

    /**
     * Imposta la città di residenza dell'utente.
     *
     * @param citta la nuova città da impostare
     */
    public void setCitta(String citta){
        this.citta = citta;
    }

    /**
     * Restituisce una rappresentazione testuale dell'utente, con i campi
     * email, nome, cognome, password, nazione, città e se è ristoratore,
     * separati da virgole. Le stringhe email, nazione e città vengono convertite
     * in minuscolo e trimmate per rimuovere spazi bianchi.
     *
     * @return una stringa formattata che rappresenta l'utente
     */
    @Override
    public String toString(){
        return email.trim().toLowerCase() + ","
                + nomeUtente.trim() + ","
                + cognomeUtente.trim() + ","
                + hashpwd.trim() + ","
                + nazione.trim().toLowerCase() + ","
                + citta.trim().toLowerCase() + ","
                + ristoratore;
    }
}