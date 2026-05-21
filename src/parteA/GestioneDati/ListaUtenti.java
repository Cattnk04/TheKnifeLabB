package parteA.GestioneDati;

import Dominio.Utente;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.0
 *
 * Classe che racchiude l'insieme dei metodi per la gestione degli utenti.
 * Nello specifico per la registrazione, il login
 * e i controlli su di essi.
 */

public class ListaUtenti {

    public static List<Utente> listaUtenti = new ArrayList<Utente>();

    /**
     * Costruttore di {@code ListaUtenti}.
     * Se la lista degli utenti è vuota, inizializza la lista caricando gli utenti da un file CSV.
     */
    //Costruttore
    public ListaUtenti(){
        if (listaUtenti.isEmpty())
            ricavaUtentiDaCSV();
    }

    //Metodo Get
    public List<Utente> getListaUtenti(){
        return listaUtenti;
    }

    /**
     * Salva la lista degli utenti su un file CSV.
     * <p>
     * Il file viene creato nella posizione specificata da {@code Utente.FILE_UTENTI}.
     * Se le directory del percorso non esistono, vengono create automaticamente.
     * Ogni utente nella lista viene scritto nel file come una riga tramite il metodo {@code toString()}.
     * In caso di errore durante la scrittura, viene stampato un messaggio di errore e lo stack trace.
     */
    //Metodo per salvare gli utenti sul CSV
    public void salvaUtentiSuCSV(){
        File file = new File(Utente.FILE_UTENTI);
        file.getParentFile().mkdirs(); // Crea le directory se non esistono
    
        try (FileWriter writer = new FileWriter(file)) {  // Uso del try-with-resources
            for(Utente utente : listaUtenti){
                writer.append(utente.toString()).append("\n");
            }
        } catch (IOException e){
            System.out.println("Errore durante il salvataggio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carica gli utenti da un file CSV e li aggiunge alla lista degli utenti.
     * <p>
     * Il file letto è quello specificato da {@code Utente.FILE_UTENTI}.
     * Ogni riga del file viene divisa in campi separati da virgola.
     * Si assume che ogni riga contenga almeno 7 campi nell'ordine:
     * email, nome, cognome, password, nazione, città e flag ristoratore (booleano).
     * <p>
     * Vengono ignorate le righe vuote o con campi insufficienti, con messaggio di avviso.
     * In caso di file non trovato, viene mostrato un messaggio informativo.
     * In caso di errore di lettura, viene stampato un messaggio di errore.
     */
    //Metodo per ricavare dal CSV
    private void ricavaUtentiDaCSV() {
        try {
            FileReader reader = new FileReader(Utente.FILE_UTENTI);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String riga;
            while ((riga = bufferedReader.readLine()) != null) {
                if (!riga.trim().isEmpty()) {  // Verifica che la riga non sia vuota
                    String[] dati = riga.split(",");
                    if (dati.length >= 7) {  // Verifica che ci siano tutti i campi necessari
                        String email = dati[0].trim();
                        String nome = dati[1].trim();
                        String cognome = dati[2].trim();
                        String password = dati[3].trim();
                        String nazione = dati[4].trim();
                        String citta = dati[5].trim();
                        boolean ristoratore = Boolean.parseBoolean(dati[6].trim());
                        Utente utente = new Utente(email, nome, cognome, password, nazione, citta, ristoratore);
                        this.listaUtenti.add(utente);
                    } else {
                        System.out.println("Avviso: Riga del file non valida (campi insufficienti): " + riga);
                    }
                }
            }
            bufferedReader.close();
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Utenti.txt non trovato. Verrà creata una nuova lista utenti.");
        } catch (IOException e) {
            System.out.println("Errore durante la lettura del file: " + e.getMessage());
        }
    }

    /**
     * Aggiunge un nuovo utente alla lista se non è duplicato.
     * <p>
     * Controlla che l'utente passato non sia nullo e non sia già presente nella lista.
     * Se l'utente è valido, viene aggiunto alla lista e la lista viene salvata su file CSV.
     *
     * @param nuovoUtente l'oggetto {@code Utente} da aggiungere
     * @return l'utente aggiunto se l'operazione ha successo, {@code null} altrimenti
     */
    //Metodo per aggiungere utente
    public Utente aggiungiUtente(Utente nuovoUtente){
        if(nuovoUtente != null && !utenteDuplicato(nuovoUtente)){
            listaUtenti.add(nuovoUtente);
            salvaUtentiSuCSV();
            return nuovoUtente;
        } else
            System.out.println("L'utente esiste già, registrati con un altro indirizzo email.");
            return null;
    }

    /**
     * Gestisce il processo di login dell'utente tramite console.
     * <p>
     * Richiede l'inserimento di email e password.
     * La password viene convertita in hash SHA-256 per la verifica.
     * Se l'utente è trovato, viene restituito l'oggetto {@code Utente} corrispondente.
     * In caso di credenziali errate, offre la possibilità di riprovare o uscire.
     *
     * @return l'utente autenticato se login riuscito, {@code null} se l'utente sceglie di non riprovare o in caso di fallimento
     * @throws RuntimeException se l'algoritmo SHA-256 non è supportato (evento raro)
     */
    //Metodo per il login
    public Utente loginUtente(){
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Login ===");
            System.out.print("Inserisci la tua e-mail: ");
            String email = scanner.nextLine().trim();
            System.out.print("Inserisci la tua password: ");
            String password = scanner.nextLine().trim();

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
                password = hexString.toString().trim();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

            Utente utente = trovaUtente(email, password);

            if (utente != null) {
                return utente;  // Ritorniamo l'utente se trovato
            } else {
                System.out.println("Email o password errati!");
                System.out.print("Vuoi riprovare? (sì/no): ");
                String risposta = scanner.nextLine().trim();

                if (risposta.equalsIgnoreCase("no")) {
                    System.out.println("Grazie per aver usato il nostro servizio!");
                    return null;
                }
            }
        }
    }

    /**
     * Cerca e restituisce un utente nella lista in base a email e password.
     * <p>
     * Confronta l'email e la password (entrambe devono corrispondere esattamente).
     * Se l'email è trovata ma la password è errata, stampa un messaggio di errore e ritorna {@code null}.
     * Se nessun utente con l'email specificata è trovato, stampa un messaggio e ritorna {@code null}.
     *
     * @param email la email dell'utente da cercare
     * @param password la password (hashata) dell'utente da cercare
     * @return l'oggetto {@code Utente} corrispondente se trovato e password corretta, {@code null} altrimenti
     */
    //Metodo per cercare l'utente nel sistema
    public Utente trovaUtente(String email, String password) {
    
    for(Utente utente : listaUtenti) {
        if(utente.getEmail().equals(email)) {
            if(utente.getPassword().equals(password)) {
                return utente;
            } else {
                System.out.println("Password non corretta per l'utente: " + email);
                return null;
            }
        }
    }
    System.out.println("Nessun utente trovato con email: " + email);
    return null;
    }

    /**
     * Verifica se un utente con la stessa email è già presente nella lista utenti.
     * <p>
     * Scorre la lista degli utenti e confronta le email.
     *
     * @param nuovoUtente l'utente da verificare
     * @return {@code true} se esiste già un utente con la stessa email, {@code false} altrimenti
     */
    //Controllo del duplicato
    public boolean utenteDuplicato(Utente nuovoUtente){
        //scorrere la lista e verificare se esistono altri utenti con la stessa email del nuovo utente e in caso tornare true
        for(Utente utente : listaUtenti){
            if(utente.getEmail().equals(nuovoUtente.getEmail())){
                return true;
            }
        }
        return false;
    }
}