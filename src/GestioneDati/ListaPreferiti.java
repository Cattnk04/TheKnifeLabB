package GestioneDati;

import Dominio.Preferito;
import Dominio.Ristorante;
import Dominio.Utente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.0
 *
 * Classe che racchiude l'insieme dei metodi per la gestione dei preferiti.
 * Nello specifico per l'aggiunta, rimozione e la visualizzazione di essi.
 */

public class ListaPreferiti {
    private static List<Preferito> listaPreferiti;

    /**
     * Costruttore della classe {@code ListaPreferiti}.
     * <p>
     * Inizializza la lista dei preferiti come una nuova {@code ArrayList} vuota
     * e carica eventuali preferiti salvati precedentemente richiamando il metodo
     * {@code ricavaPreferitiDaCSV()}, che legge i dati da un file CSV.
     * </p>
     */
    public ListaPreferiti() {
        this.listaPreferiti = new ArrayList<>();
        ricavaPreferitiDaCSV();
    }

    /**
     * Legge i dati dei ristoranti preferiti dal file CSV specificato nella costante {@code Preferito.FILE_PREFERITI}
     * e li aggiunge alla lista dei preferiti dell'applicazione.
     * <p>
     * Ogni riga del file deve contenere esattamente due campi separati da virgola: l'email dell'utente e il nome del ristorante.
     * Le righe non valide (vuote o con un numero errato di campi) vengono ignorate e segnalate con un messaggio di avviso.
     * </p>
     * <p>
     * In caso di assenza del file, viene mostrato un messaggio e viene creata una nuova lista vuota.
     * In caso di errori di I/O, viene stampato un messaggio descrittivo.
     * </p>
     *
     * @throws SecurityException se i permessi di accesso al file sono negati (non gestito direttamente ma possibile)
     */
    //Metodo per leggere i preferiti da CSV
    private void ricavaPreferitiDaCSV(){
        try {
            FileReader reader = new FileReader(Preferito.FILE_PREFERITI);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String riga;
            while ((riga = bufferedReader.readLine()) != null) {
                if (!riga.trim().isEmpty()) {  // Verifica che la riga non sia vuota
                    String[] dati = riga.split(",");
                    if (dati.length == 2) {  // Verifica che ci siano tutti i campi necessari
                        String emailUtente = dati[0];
                        String nomeRistorante = dati[1];
                        listaPreferiti.add(new Preferito(emailUtente, nomeRistorante));
                    } else {
                        System.out.println("Avviso: Riga del file non valida (campi insufficienti): " + riga);
                    }
                }
            }
            bufferedReader.close();
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Preferiti.txt non trovato. Verrà creata una nuova lista preferiti.");
        } catch (IOException e) {
            System.out.println("Errore durante la lettura del file: " + e.getMessage());
        }
    }

    /**
     * Salva la lista dei preferiti su un file CSV.
     * <p>
     * Il file di destinazione è definito dalla costante {@code Preferito.FILE_PREFERITI}.
     * Se le directory parent non esistono, vengono create automaticamente.
     * Ogni oggetto {@code Preferito} presente nella lista {@code listaPreferiti}
     * viene scritto come una riga nel file, utilizzando il suo metodo {@code toString()}.
     * </p>
     *
     * @throws RuntimeException se si verifica un errore di I/O durante la scrittura del file.
     */
    //Metodo per salvare sul CSV
    public void salvaPreferitiSuCSV(){
        File file = new File(Preferito.FILE_PREFERITI);
        file.getParentFile().mkdirs(); // Crea le directory se non esistono

        try (FileWriter writer = new FileWriter(file)) {  // Uso del try-with-resources
            for(Preferito preferito : listaPreferiti){
                writer.append(preferito.toString()).append("\n");
            }
        } catch (IOException e){
            System.out.println("Errore durante il salvataggio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Aggiunge un nuovo ristorante alla lista dei preferiti per l'utente specificato.
     * <p>
     * Crea un oggetto {@code Preferito} a partire dall'utente corrente e dalla lista di ristoranti.
     * Se il preferito non è nullo, ha un nome valido e non è già presente nella lista,
     * viene aggiunto alla lista {@code listaPreferiti}, salvato su file CSV e viene notificato l’utente.
     * </p>
     *
     * @param utenteCorrente     L'utente che desidera aggiungere un ristorante ai preferiti.
     * @param listaRistoranti    La lista dei ristoranti da cui si seleziona il preferito.
     *
     * @throws RuntimeException se si verifica un errore imprevisto durante la creazione o l'aggiunta del preferito.
     */
    //Metodo per aggiungere un preferito
    public void aggiungiPreferito(Utente utenteCorrente, ListaRistoranti listaRistoranti){
        try {
            Preferito nuovoPreferito = new Preferito(utenteCorrente, listaRistoranti);
            if (nuovoPreferito != null && nuovoPreferito.getNomeRistorante() != null && !preferitoDuplicato(nuovoPreferito)) {
                listaPreferiti.add(nuovoPreferito);
                salvaPreferitiSuCSV();
                System.out.println("Ristorante aggiunto ai preferiti con successo.");
            } else {
                System.out.println("Impossibile aggiungere il ristorante ai preferiti.");
            }
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiunta del preferito: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Verifica se il preferito specificato è già presente nella lista dei preferiti.
     * <p>
     * Il controllo di duplicato avviene confrontando il nome del ristorante e
     * l'email dell'utente associati al nuovo preferito con quelli già presenti nella lista {@code listaPreferiti}.
     * Se esiste un preferito con lo stesso ristorante e la stessa email utente, il metodo restituisce {@code true}.
     * </p>
     *
     * @param nuovoPreferito Il preferito da verificare per la presenza di duplicati.
     * @return {@code true} se il preferito è già presente nella lista, {@code false} altrimenti.
     */
    //Controllo del duplicato
    public boolean preferitoDuplicato(Preferito nuovoPreferito){
        for(Preferito p : listaPreferiti){
            if(p.getNomeRistorante().equals(nuovoPreferito.getNomeRistorante()) && p.getEmailUtente().equals(nuovoPreferito.getEmailUtente())){
                return true;
            }
        }
        return false;
    }

    /**
     * Mostra a console la lista dei ristoranti preferiti dell'utente specificato.
     * <p>
     * Recupera i preferiti associati all'utente corrente e li stampa uno per riga.
     * Se l'utente non ha preferiti salvati, viene mostrato un messaggio informativo.
     * </p>
     *
     * @param utenteCorrente L'utente di cui visualizzare i ristoranti preferiti.
     * @return {@code true} se l'utente ha almeno un ristorante preferito, {@code false} altrimenti.
     */
    //Metodo per mostrare i preferiti
    public boolean mostraPreferiti(Utente utenteCorrente){
        List<Preferito> preferitiUtente = preferitiUtente(utenteCorrente);
        if (preferitiUtente.isEmpty()) {
            System.out.println("\nNon hai ancora aggiunto ristoranti ai preferiti.");
            return false;
        } else {
            System.out.println("\nI tuoi ristoranti preferiti:");
            for (Preferito p : preferitiUtente) {
                System.out.println(" - " + p.getNomeRistorante());
            }
        }
        return true;
    }

    /**
     * Rimuove un ristorante dalla lista dei preferiti dell'utente specificato.
     * <p>
     * Mostra all'utente i ristoranti attualmente presenti tra i suoi preferiti e
     * richiede l'inserimento del nome del ristorante da rimuovere.
     * Se il ristorante è presente tra i preferiti dell'utente, viene rimosso
     * e la lista aggiornata viene salvata su file CSV.
     * Se il ristorante non viene trovato nella lista o non è tra i preferiti,
     * viene notificato all'utente tramite messaggi a console.
     * </p>
     *
     * @param utente           L'utente che desidera rimuovere un ristorante dai preferiti.
     * @param listaRistoranti  La lista dei ristoranti da cui cercare il ristorante da rimuovere.
     */
    // Metodo per rimuovere i preferiti
    public void rimuoviPreferito(Utente utente, ListaRistoranti listaRistoranti){
        if(mostraPreferiti(utente)){
            Ristorante ristorante = listaRistoranti.cercaPerNome("Inserisci il nome del ristorante da rimuovere dai preferiti: ");
            if(ristorante != null){
                // Utilizziamo Iterator per evitare ConcurrentModificationException
                Iterator<Preferito> iterator = listaPreferiti.iterator();
                boolean rimosso = false;
                while(iterator.hasNext()) {
                    Preferito p = iterator.next();
                    if(p.getNomeRistorante().equals(ristorante.getNome()) &&
                       p.getEmailUtente().equals(utente.getEmail())){
                        iterator.remove();
                        rimosso = true;
                        break;
                    }
                }
                if(rimosso) {
                    System.out.println("Ristorante rimosso dai preferiti.");
                    salvaPreferitiSuCSV(); // Salva le modifiche su file
                } else {
                    System.out.println("Il ristorante non è presente nei tuoi preferiti.");
                }
            }
            else {
                System.out.println("Ristorante non trovato.");
            }
        }
    }

    /**
     * Restituisce la lista dei ristoranti preferiti associati all'utente specificato.
     * <p>
     * Filtra la lista globale {@code listaPreferiti} e seleziona solo i preferiti
     * il cui indirizzo email corrisponde a quello dell'utente corrente.
     * </p>
     *
     * @param utenteCorrente L'utente di cui recuperare i ristoranti preferiti.
     * @return Una lista di oggetti {@code Preferito} associati all'utente. La lista può essere vuota se non ci sono preferiti.
     */
    //Metodo per filtrare e restituire solo i preferiti dell'utente corrente
    public List<Preferito> preferitiUtente (Utente utenteCorrente){
        List<Preferito> preferitiUtente = new ArrayList<>();
        for (Preferito p : listaPreferiti) {
            if(p.getEmailUtente().equals(utenteCorrente.getEmail())){
                preferitiUtente.add(p);
            }
        }
        return preferitiUtente;
    }
}