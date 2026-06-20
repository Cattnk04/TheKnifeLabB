package main.java.shared.communication;

import java.io.Serializable;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Classe utilizzata per rappresentare una risposta inviata dal server al client.
 * <p>
 * Contiene l'esito dell'operazione, un eventuale contenuto restituito
 * (DTO o altri oggetti) e un messaggio descrittivo.
 * </p>
 */
public class Risposta implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean successo = false;
    private Object contenuto;
    private String msg;

    /**
     * Costruisce una nuova risposta del server.
     *
     * @param successo indica se l'operazione è andata a buon fine
     * @param contenuto eventuale oggetto restituito (DTO o dati)
     * @param msg messaggio informativo o di errore
     */
    public Risposta(boolean successo, Object contenuto, String msg) {
        this.successo = successo;
        this.contenuto = contenuto;
        this.msg = msg;
    }

    /**
     * Restituisce il contenuto della risposta.
     * @return oggetto contenuto (può essere null)
     */
    public Object getContenuto() {return contenuto;}

    /**
     * Restituisce il messaggio associato alla risposta.
     * @return messaggio informativo o di errore
     */
    public String getMsg() {return msg;}

    /**
     * Indica se l'operazione è andata a buon fine.
     * @return true se successo, false altrimenti
     */
    public boolean getSuccesso() {return successo;}
}