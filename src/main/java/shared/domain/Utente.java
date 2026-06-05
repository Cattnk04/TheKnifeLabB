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
    /* public static final String FILE_UTENTI = "Data/Utenti.txt";
    private static final Scanner scanner = new Scanner(System.in);*/
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
    public Utente(String email, String nomeUtente, String cognomeUtente, String hashpwd, String nazione, String citta, boolean ristoratore) {
        this.email = email.trim().toLowerCase();
        this.nomeUtente = nomeUtente.trim();
        this.cognomeUtente = cognomeUtente.trim();
        this.hashpwd = hashpwd;
        this.nazione = nazione.trim().toLowerCase();
        this.citta = citta.trim().toLowerCase();
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
    //Metodo to String
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

    /**
     * Costruttore per la registrazione di un nuovo utente tramite input da console.
     * Richiede all'utente di inserire nome, cognome, nazione, città, stato di ristoratore,
     * email e password. Effettua controlli di validità su nome, cognome, email e password.
     * La password inserita viene crittografata tramite SHA-256 e salvata in forma hashata.
     *
     * @throws RuntimeException se il digest SHA-256 non è disponibile
     */
    /*
    //Metodo per la registrazione
    public Utente() {
        System.out.println("\n\n=== Registrazione ===");
        do{
            System.out.print("Inserisci il tuo nome: ");
            this.nome = scanner.nextLine().trim();
            if(this.nome.length()<=1)
                System.out.println("Nome non valido");
        }while (this.nome.length()<=1);
        do{
            System.out.print("Inserisci il tuo cognome: ");
            this.cognome = scanner.nextLine().trim();
            if(this.cognome.length()<=1){
                System.out.println("Cognome non valido");
            }
        } while (this.cognome.length()<=1);

        System.out.print("Inserisci la Nazione: ");
        this.nazione = scanner.nextLine().trim();

        System.out.print("Inserisci la provincia di domicilio: ");
        this.citta = scanner.nextLine().trim();

        boolean valido = false;
        do{
            valido = true;
            System.out.print("Sei proprietario di un ristorante? [s/n]: ");
            String risposta = scanner.nextLine().trim().toLowerCase(); // Salva l'input in una variabile
            if(risposta.equals("s"))
                this.ristoratore = true;
            else if (risposta.equals("n"))
                this.ristoratore = false;
            else
                valido = false;

        } while (!valido);

        String email = "";
        do{
            valido = true;
            System.out.print("Inserisci la tua email: ");
            email = scanner.nextLine().trim().toLowerCase();
            if(!email.contains("@") || !email.contains(".")){
                valido = false;
                System.out.println("Email non valida");
            }
        } while (!valido);
        this.email = email;
        String password = "";
        do{
            valido = true;
            System.out.print("Inserisci la tua password: ");
            password = scanner.nextLine();
            if(password.length()<8){
                valido = false;
                System.out.println("Password troppo corta, inserirne una più lunga.");
            }
        } while (!valido);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Converti i byte in una stringa esadecimale
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            this.hashpwd = hexString.toString().trim();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }*/
}