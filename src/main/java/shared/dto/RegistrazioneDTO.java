package main.java.shared.dto;

import java.io.Serializable;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * DTO utilizzato per rappresentare i dati necessari alla registrazione di un utente.
 * <p>
 * Contiene le informazioni anagrafiche e le credenziali dell'utente,
 * oltre all'indicazione se si tratta di un utente ristoratore.
 * Viene utilizzato nella fase di creazione di un nuovo account.
 * </p>
 */
public class RegistrazioneDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nome;
    private final String cognome;
    private final String email;
    private final String password;
    private final String citta;
    private final String nazione;
    private final boolean ristoratore;

    /**
     * Costruisce un nuovo DTO di registrazione.
     *
     * @param nome nome dell'utente
     * @param cognome cognome dell'utente
     * @param email email dell'utente
     * @param password password dell'account
     * @param citta città di residenza
     * @param nazione nazione di residenza
     * @param ristoratore true se l'utente è un ristoratore, false altrimenti
     */
    public RegistrazioneDTO(String nome, String cognome, String email, String password,
                            String citta, String nazione, boolean ristoratore) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.citta = citta;
        this.nazione = nazione;
        this.ristoratore = ristoratore;
    }

    /**
     * Restituisce il nome dell'utente.
     * @return nome
     */
    public String getNome() {return nome;}

    /**
     * Restituisce il cognome dell'utente.
     * @return cognome
     */
    public String getCognome() {return cognome;}

    /**
     * Restituisce l'email dell'utente.
     * @return email
     */
    public String getEmail() {return email;}

    /**
     * Restituisce la password dell'account.
     * @return password
     */
    public String getPassword() {return password;}

    /**
     * Restituisce la città di residenza.
     * @return città
     */
    public String getCitta() {return citta;}

    /**
     * Restituisce la nazione di residenza.
     * @return nazione
     */
    public String getNazione() {return nazione;}

    /**
     * Indica se l'utente è un ristoratore.
     * @return true se ristoratore, false altrimenti
     */
    public boolean isRistoratore() {return ristoratore;}
}
