/*package parteA.GestioneDati;

import main.java.shared.domain.Recensione;
import main.java.shared.domain.Ristorante;
import main.java.shared.domain.Utente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.0
 *
 * Classe che racchiude l'insieme dei metodi per la gestione delle recensioni.
 * Nello specifico per l'aggiunta, rimozione, la modifica e
 * la visualizzazione di esse.
 */

/*public class ListaRecensioni {

    private List<Recensione> listaRecensioni = new ArrayList<>();

    /**
     * Costruttore della classe {@code ListaRecensioni}.
     * <p>
     * Inizializza l'istanza verificando se la lista delle recensioni è vuota.
     * Se lo è, richiama il metodo {@code ricavaRecensioniDaCSV()} per
     * caricare i dati da un file CSV.
     * </p>
     */
/* public ListaRecensioni() {
     if (listaRecensioni.isEmpty()){
         ricavaRecensioniDaCSV();
     }
 }

 //Metodi Get e Set
 /**
  * Restituisce la lista delle recensioni memorizzate.
  *
  * @return La lista di oggetti {@code Recensione}.
  */
   /* public List<Recensione> getListaRecensione() {
        return listaRecensioni;
    }
    /**
     * Imposta la lista delle recensioni.
     *
     * @param listaRecensione La nuova lista di oggetti {@code Recensione} da assegnare.
     */
   /* public void setListaRecensione(List<Recensione> listaRecensione) {
        this.listaRecensioni = listaRecensione;
    }

    /**
     * Salva la lista delle recensioni su un file CSV.
     * <p>
     * Il file di destinazione è definito dalla costante {@code Recensione.FILE_RECENSIONI}.
     * Ogni oggetto {@code Recensione} presente nella lista {@code listaRecensioni}
     * viene scritto su una nuova riga nel file, utilizzando il metodo {@code toString()}.
     * </p>
     * In caso di errore durante la scrittura, viene stampato un messaggio di errore su console.
     */
    //Metodo per salvare su CSV
  /*  public void salvaRecensioniSuCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Recensione.FILE_RECENSIONI))){
            for (Recensione r :listaRecensioni){
                writer.write(r.toString());
                writer.newLine();
            }
    } catch(IOException e){
            System.err.println("Errore durante il salvataggio della recensione: " + e.getMessage());
        }
    }

    /**
     * Carica le recensioni da un file CSV e le aggiunge alla lista delle recensioni.
     * <p>
     * Il file di origine è definito dalla costante {@code Recensione.FILE_RECENSIONI}.
     * Ogni riga del file deve contenere almeno cinque campi separati dal carattere '*':
     * email, nome del ristorante, valutazione (intero), testo della recensione e risposta.
     * Le righe vuote vengono ignorate.
     * </p>
     * In caso di errori di I/O o di parsing della valutazione, viene stampato un messaggio di errore su console.
     */
    //Metodo per ricavare da CSV
   /* public void ricavaRecensioniDaCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(Recensione.FILE_RECENSIONI))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {  // Ignora le righe vuote
                    String[] riga = line.split("\\*");  // Usa \\* come separatore
                    if (riga.length >= 5) {
                        String email = riga[0];
                        String nomeRistorante = riga[1];
                        Integer valutazione = Integer.parseInt(riga[2]);
                        String recensione = riga[3];
                        String risposta = riga[4];
                        Recensione r = new Recensione(email, nomeRistorante, valutazione, recensione, risposta);
                        listaRecensioni.add(r);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nel caricamento delle recensioni: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Errore nel parsing della valutazione: " + e.getMessage());
        }
    }

    /**
     * Inserisce una nuova recensione da parte dell'utente per un ristorante selezionato.
     * <p>
     * Crea un oggetto {@code Recensione} usando i dati forniti dall'utente e dalla lista dei ristoranti.
     * Se esiste già una recensione dello stesso utente per lo stesso ristorante,
     * stampa un messaggio di avviso e non aggiunge la recensione.
     * Altrimenti, aggiunge la recensione alla lista, salva la lista aggiornata su file CSV
     * e notifica l'utente del successo.
     * </p>
     *
     * @param utente          L'utente che vuole inserire la recensione.
     * @param listaRistoranti La lista dei ristoranti tra cui scegliere.
     *
     * @throws RuntimeException se si verifica un errore durante la creazione o l'inserimento della recensione.
     */
    //Metodo per aggiungere una recensione al file di recensioni
    /*public void inserisciRecensione(Utente utente, ListaRistoranti listaRistoranti){
        try {
            Recensione recensione = new Recensione(utente, listaRistoranti);
            if (recensioneDuplicato(recensione)){
                System.out.println("Hai già lasciato una recensione a questo ristorante!");
            } else {
                listaRecensioni.add(recensione);
                salvaRecensioniSuCSV();
                System.out.println("Recensione aggiunta con successo!");
            }
        } catch (Exception e) {
            System.err.println("Errore durante l'inserimento della recensione: " + e.getMessage());
        }
    }

    /**
     * Verifica se una recensione identica è già presente nella lista delle recensioni.
     * <p>
     * Il controllo di duplicato avviene confrontando l'email dell'utente e il nome del ristorante
     * della recensione fornita con quelli già presenti nella lista {@code listaRecensioni}.
     * </p>
     *
     * @param recensione La recensione da verificare per eventuali duplicati.
     * @return {@code true} se una recensione dello stesso utente per lo stesso ristorante esiste già, {@code false} altrimenti.
     */
    //Metodo per il controllo della duplicazione delle recensioni
   /* public boolean recensioneDuplicato(Recensione recensione){
        for(Recensione r : listaRecensioni){
            if(r.getEmail().equals(recensione.getEmail()) && r.getNomeRistorante().equals(recensione.getNomeRistorante())){
                return true;
            }
        }
        return false;
    }

    /**
     * Permette all'utente di modificare una recensione esistente per un ristorante specifico.
     * <p>
     * Il metodo richiede in input da console:
     * <ul>
     *   <li>Il nome del ristorante della recensione da modificare.</li>
     *   <li>Il nuovo testo della recensione.</li>
     *   <li>Il nuovo punteggio in stelle (da 1 a 5).</li>
     * </ul>
     * Se la recensione corrispondente all'utente e al ristorante è trovata,
     * aggiorna il testo e la valutazione, salva le modifiche su file CSV e notifica l’utente.
     * Se la recensione non viene trovata o il punteggio è fuori dal range valido,
     * mostra un messaggio di errore appropriato.
     * </p>
     *
     * @param utente L'utente che vuole modificare la propria recensione.
     */
    //Metodo per modificare una recensione
   /* public void modificaRecensione(Utente utente) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il nome del ristorante da modificare: ");
        String nomeRistorante = scanner.nextLine();
        System.out.print("Inserisci il nuovo testo della recensione: ");
        String nuovoTesto = scanner.nextLine();
        System.out.print("Inserisci il nuovo numero di stelle (1-5): ");
        int nuoveStelle = scanner.nextInt();
        scanner.nextLine(); // Pulizia buffer
        String emailUtente = utente.getEmail();

        try {
            if (nuoveStelle < 1 || nuoveStelle > 5) {
                System.out.print("Il numero di stelle deve essere tra 1 e 5\n");
                return;
            }

            boolean recensioneTrovata = false;
            for (Recensione r : listaRecensioni) {
                if (r.getEmail().equals(emailUtente) && r.getNomeRistorante().equals(nomeRistorante)) {
                    r.setRecensione(nuovoTesto);
                    r.setValutazione(nuoveStelle);
                    recensioneTrovata = true;
                    break;
                }
            }

            if (!recensioneTrovata) {
                System.out.print("Recensione non trovata\n");
                return;
            }

            salvaRecensioniSuCSV();
            System.out.print("Recensione modificata con successo\n");
        } catch (IllegalArgumentException e) {
            System.out.print("Errore: " + e.getMessage());
        }
    }

    /**
     * Elimina la recensione di un utente per un ristorante specifico.
     * <p>
     * Il metodo richiede in input da console il nome del ristorante per cui
     * l'utente vuole rimuovere la propria recensione. Se la recensione esiste,
     * viene rimossa dalla lista e la modifica viene salvata su file CSV.
     * In caso contrario, viene mostrato un messaggio che indica che la recensione non è stata trovata.
     * </p>
     *
     * @param utente L'utente che vuole eliminare la propria recensione.
     */
    //Metodo per eliminare una recensione
   /* public void eliminaRecensione(Utente utente) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il nome del ristorante di cui eliminare la recensione: ");
        String nomeRistorante = scanner.nextLine();
        String email = utente.getEmail();

        boolean recensioneTrovata = false;

        try {
            for (Recensione r : new ArrayList<>(listaRecensioni)) {
                if (r.getEmail().equals(email) && r.getNomeRistorante().equals(nomeRistorante)) {
                    listaRecensioni.remove(r);
                    recensioneTrovata = true;
                    break;
                }
            }

            if (recensioneTrovata) {
                salvaRecensioniSuCSV();
                System.out.println("Recensione eliminata con successo\n");
            } else {
                System.out.println("Recensione non trovata\n");
            }
        } catch (Exception e) {
            System.err.println("Errore durante l'eliminazione: " + e.getMessage());
        }
    }

    /**
     * Mostra tutte le recensioni associate a un ristorante specifico.
     * <p>
     * Recupera la lista delle recensioni relative al ristorante indicato
     * e le stampa su console. Se non ci sono recensioni, informa l'utente.
     * </p>
     *
     * @param ristorante Il ristorante di cui mostrare le recensioni.
     */
    //Metodo per visuallizare le recensioni del ristorante selezionato dopo il cerca RiSTORANTE
   /* public void mostraRecensioniRistorante(Ristorante ristorante) {
        List<Recensione> recensioniRistorante = recensioniRistorante(ristorante.getNome());

        if (recensioniRistorante == null || recensioniRistorante.isEmpty()) {
            System.out.println("\nNon ci sono ancora recensioni per questo ristorante.");
            return;
        }

        System.out.println("\n=== Recensioni del ristorante ===");
        for (Recensione recensione : recensioniRistorante) {
            System.out.println("\n" + recensione.stampaRecensione());
            System.out.println("----------------------------------------");
        }
    }

    /**
     * Mostra tutte le recensioni scritte dall'utente specificato.
     * <p>
     * Recupera la lista delle recensioni associate all'utente e le stampa su console.
     * Se l'utente non ha scritto recensioni, mostra un messaggio informativo.
     * </p>
     *
     * @param utente L'utente di cui mostrare le recensioni.
     */
    //Metodo per la stampa delle recensioni per il menu utente log
   /* public void mostraRecensioniUtente(Utente utente) {
        List<Recensione> recensioni = recensioniUtente(utente);
        if (recensioni == null || recensioni.isEmpty()) {
            System.out.println("\nNon hai ancora scritto recensioni.");
        } else {
            System.out.println("\n=== Le tue recensioni ===");
            for (Recensione r : recensioni) {
                System.out.println("\nRistorante: " + r.getNomeRistorante());
                System.out.println(r.stampaRecensione());
                System.out.println("------------------------");
            }
        }
    }

    /**
     * Restituisce la lista delle recensioni associate a un ristorante specifico.
     * <p>
     * Confronta il nome del ristorante fornito, ignorando maiuscole e spazi
     * iniziali/finali, con i nomi dei ristoranti nelle recensioni presenti nella lista.
     * </p>
     *
     * @param nomeRistorante Il nome del ristorante di cui si vogliono ottenere le recensioni.
     * @return Una lista di oggetti {@code Recensione} relative al ristorante specificato.
     */
    //Metodo per filtrare e restituire solo per le recensioni appartenenti al ristorante inserito
   /* public List<Recensione> recensioniRistorante(String nomeRistorante){
        List<Recensione> recensioniRistorante = new ArrayList<>();
        for (Recensione r : listaRecensioni) {
            if (r.getNomeRistorante().trim().equalsIgnoreCase(nomeRistorante.trim())) {
                recensioniRistorante.add(r);
            }
        }
        return recensioniRistorante;
    }

    /**
     * Restituisce la lista delle recensioni scritte da uno specifico utente.
     * <p>
     * Confronta l'email dell'utente fornito, ignorando maiuscole e spazi
     * iniziali/finali, con le email associate alle recensioni presenti nella lista.
     * </p>
     *
     * @param utente L'utente di cui si vogliono ottenere le recensioni.
     * @return Una lista di oggetti {@code Recensione} scritte dall'utente.
     */
    //Metodo per filtrare e restituire solo le recensioni dell'utente corrente
   /* public List<Recensione> recensioniUtente(Utente utente) {
        List<Recensione> recensioniUtente = new ArrayList<>();
        for (Recensione r : listaRecensioni) {
            if (r.getEmail().trim().equalsIgnoreCase(utente.getEmail())) {
                recensioniUtente.add(r);
            }
        }
        return recensioniUtente; // sempre lista, mai null
    }
}

    */