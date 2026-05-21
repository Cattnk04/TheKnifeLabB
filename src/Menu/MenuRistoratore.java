package Menu;

import Dominio.Recensione;
import Dominio.Ristorante;
import Dominio.Utente;
import GestioneDati.ListaRecensioni;
import GestioneDati.ListaRistoranti;

import java.util.*;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.0
 *
 * Classe che rappresenta il menu specifico per i ristoratori,
 * permettendo di gestire ristoranti, visualizzare e rispondere alle recensioni.
 */
public class MenuRistoratore {

    private Utente utenteCorrente;
    private ListaRistoranti listaRistoranti;
    private ListaRecensioni listaRecensioni;
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Costruttore della classe {@code MenuRistoratore}.
     * <p>
     * Inizializza il menu dedicato all'utente con ruolo di ristoratore.
     * Imposta l'utente corrente e crea nuove istanze delle liste di ristoranti e recensioni.
     * Alla fine, avvia la visualizzazione del menu specifico per il ristoratore.
     *
     * @param utente l'utente corrente con ruolo di ristoratore
     */
    public MenuRistoratore(Utente utente) {
        this.utenteCorrente = utente;
        this.listaRistoranti = new ListaRistoranti();
        this.listaRecensioni = new ListaRecensioni();
        mostraMenuRistoratore();
    }

    /**
     * Mostra il menu interattivo dedicato all'utente ristoratore.
     * <p>
     * Presenta diverse opzioni per gestire i ristoranti e le recensioni:
     * <ul>
     *   <li>1 - Aggiungi un nuovo ristorante</li>
     *   <li>2 - Visualizza i propri ristoranti</li>
     *   <li>3 - Visualizza un riepilogo delle recensioni di tutti i ristoranti</li>
     *   <li>4 - Visualizza i dettagli delle recensioni per un ristorante specifico</li>
     *   <li>5 - Rispondi alle recensioni</li>
     *   <li>0 - Esci dal menu (logout)</li>
     * </ul>
     * <p>
     * Gestisce l'input da tastiera e controlla la validità delle scelte, mostrando messaggi di errore in caso di input non valido o altre eccezioni.
     * Il menu rimane attivo fino a quando l'utente sceglie di uscire (opzione 0).
     */
    public void mostraMenuRistoratore() {
        int scelta = 0;
        do {
            try {
                System.out.println("\n=== Menu Ristoratore ===");
                System.out.println("1. Aggiungi ristorante");
                System.out.println("2. Visualizza i miei ristoranti");
                System.out.println("3. Visualizza riepilogo recensioni di tutti i ristoranti");
                System.out.println("4. Visualizza dettagli recensioni");
                System.out.println("5. Rispondi alle recensioni"); // Nuova opzione
                System.out.println("0. Logout");
                System.out.print("La tua scelta: ");

                scelta = scanner.nextInt();
                scanner.nextLine(); // Consuma il newline

                switch (scelta) {
                    case 1:
                        aggiungiRistorante();
                        break;
                    case 2:
                        visualizzaMieiRistoranti();
                        break;
                    case 3:
                        visualizzaRiepilogo();
                        break;
                    case 4:
                        visualizzaRecensioniRistorante();
                        break;
                    case 5:
                        rispostaRecensioni();
                        break;
                    case 0:
                        System.out.println("Logout effettuato con successo!");
                        break;
                    default:
                        System.out.println("Opzione non valida!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Inserire un numero valido!");
                scanner.nextLine(); // Pulizia buffer
                scelta = -1;
            } catch (Exception e) {
                System.out.println("Errore imprevisto: " + e.getMessage());
                scelta = -1;
            }
        } while (scelta != 0);
    }

    /**
     * Permette all'utente ristoratore di aggiungere un nuovo ristorante.
     * <p>
     * Raccoglie i dati del ristorante tramite input da tastiera, tra cui nome,
     * nazione, città, indirizzo, fascia di prezzo, servizi delivery e prenotazione online,
     * e tipo di cucina.
     * Crea un nuovo oggetto {@code Ristorante} e lo inserisce nella lista dei ristoranti,
     * salvando poi i dati su file CSV.
     * Stampa un messaggio di conferma al termine dell'aggiunta.
     */
    private void aggiungiRistorante() {
        System.out.println("\n=== Aggiungi Nuovo Ristorante ===");
        System.out.print("Nome del ristorante: ");
        String nome = scanner.nextLine();

        System.out.print("Nazione: ");
        String nazione = scanner.nextLine();

        System.out.print("Città: ");
        String citta = scanner.nextLine();

        System.out.print("Indirizzo: ");
        String indirizzo = scanner.nextLine();

        System.out.print("Fascia di prezzo: ");
        double fasciaPrezzo = scanner.nextDouble();
        scanner.nextLine();

        boolean delivery = false;
        boolean prenotazioneOnline = false;

        boolean inputValido;
        do {
            System.out.print("Servizio delivery (s/n): ");
            String rispostaDelivery = scanner.nextLine().trim().toLowerCase();
            if (rispostaDelivery.equals("s")) {
                delivery = true;
                inputValido = true;
            } else if (rispostaDelivery.equals("n")) {
                delivery = false;
                inputValido = true;
            } else {
                System.out.println("Inserire 's' per sì o 'n' per no");
                inputValido = false;
            }
        } while (!inputValido);

        do {
            System.out.print("Prenotazione online (s/n): ");
            String rispostaPrenotazione = scanner.nextLine().trim().toLowerCase();
            if (rispostaPrenotazione.equals("s")) {
                prenotazioneOnline = true;
                inputValido = true;
            } else if (rispostaPrenotazione.equals("n")) {
                prenotazioneOnline = false;
                inputValido = true;
            } else {
                System.out.println("Inserire 's' per sì o 'n' per no");
                inputValido = false;
            }
        } while (!inputValido);

        System.out.print("Tipo di cucina: ");
        String tipoCucina = scanner.nextLine();

        Ristorante ristorante = new Ristorante(nome, utenteCorrente.getEmail(), nazione, citta, indirizzo, fasciaPrezzo, delivery, prenotazioneOnline, tipoCucina);
        listaRistoranti.inserisciRistorante(ristorante);
        listaRistoranti.salvaRistorantiSuCSV();
        System.out.println("Ristorante aggiunto con successo!");
    }

    /**
     * Visualizza tutti i ristoranti registrati dall'utente ristoratore corrente.
     * <p>
     * Scorre la lista di tutti i ristoranti e stampa i dettagli di quelli associati
     * all'email dell'utente corrente.
     * Se non sono stati registrati ristoranti, informa l'utente che non ne ha ancora aggiunti.
     */
    private void visualizzaMieiRistoranti() {
        System.out.println("\n=== I Miei Ristoranti ===");
        boolean trovati = false;

        for (Ristorante r : listaRistoranti.getListaRistoranti()) {
            if (r.getEmailRistoratore().equals(utenteCorrente.getEmail())) {
                trovati = true;
                System.out.println("\nNome: " + r.getNome());
                System.out.println("Nazione: " + r.getNazione());
                System.out.println("Città: " + r.getCitta());
                System.out.println("Indirizzo: " + r.getIndirizzo());
                System.out.println("Fascia di prezzo: " + r.getFasciaPrezzo());
                System.out.println("Servizio delivery: " + (r.getServizioDelivery() ? "Sì" : "No"));
                System.out.println("Prenotazione online: " + (r.getServizioPrenotazioneOnline() ? "Sì" : "No"));
                System.out.println("Tipo di cucina: " + r.getTipoCucina());
                System.out.println("----------------------------------------");
            }
        }

        if (!trovati) {
            System.out.println("Non hai ancora registrato alcun ristorante.");
        }
    }

    /**
     * Visualizza le recensioni relative a uno dei ristoranti dell'utente ristoratore corrente.
     * <p>
     * Elenca i ristoranti dell'utente e permette di selezionare uno di essi tramite input numerico.
     * Successivamente mostra tutte le recensioni associate al ristorante selezionato,
     * oppure informa se non ci sono recensioni disponibili.
     * Gestisce input non validi o selezioni fuori range.
     */
    private void visualizzaRecensioniRistorante() {
        System.out.println("\n=== Dettaglio Recensioni ===");
        Scanner scanner = new Scanner(System.in);

        // Lista temporanea per tenere traccia dei ristoranti dell'utente
        List<Ristorante> ristorantiUtente = new ArrayList<>();

        int cont = 1;
        for (Ristorante r : listaRistoranti.getListaRistoranti()) {
            if (r.getEmailRistoratore().equals(utenteCorrente.getEmail())) {
                System.out.println(cont + ": " + r.getNome());
                ristorantiUtente.add(r);
                cont++;
            }
        }

        if (ristorantiUtente.isEmpty()) {
            System.out.println("Non hai ristoranti registrati.");
            return;
        }

        System.out.println("----------------------------------------");
        System.out.print("\nInserisci il numero del ristorante del quale vuoi vedere le recensioni: ");
        int scelta;
        try {
            scelta = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input non valido. Inserire un numero.");
            return;
        }

        if (scelta < 1 || scelta > ristorantiUtente.size()) {
            System.out.println("Numero non valido.");
            return;
        }

        Ristorante ristoranteSelezionato = ristorantiUtente.get(scelta - 1);

        // Mostra recensioni
        List<Recensione> recensioni = listaRecensioni.recensioniRistorante(ristoranteSelezionato.getNome());
        if (recensioni.isEmpty()) {
            System.out.println("\n----------------------------------------");
            System.out.println("Nessuna recensione disponibile per questo ristorante.");
            System.out.println("----------------------------------------");
        } else {
            for (Recensione rec : recensioni) {
                System.out.println(rec.stampaRecensione());
            }
        }
    }

    /**
     * Permette all'utente ristoratore di rispondere alle recensioni dei propri ristoranti.
     * <p>
     * Mostra i ristoranti dell'utente, consente di selezionare uno di essi,
     * quindi elenca le recensioni senza risposta per quel ristorante.
     * Per ogni recensione senza risposta, offre la possibilità di inserire una risposta
     * tramite input da tastiera. Salva le risposte aggiornate nel file CSV.
     * Gestisce casi di input non valido o mancanza di recensioni da rispondere.
     */
    private void rispostaRecensioni() {
        System.out.println("\n=== Rispondi alle Recensioni ===");

        // Lista dei ristoranti del proprietario corrente
        List<Ristorante> mieiRistoranti = new ArrayList<>();
        int contatore = 1;

        // Mostra la lista dei ristoranti del proprietario
        System.out.println("I tuoi ristoranti:");
        for (Ristorante r : listaRistoranti.getListaRistoranti()) {
            if (r.getEmailRistoratore().equals(utenteCorrente.getEmail())) {
                System.out.printf("%d. %s\n", contatore++, r.getNome());
                mieiRistoranti.add(r);
            }
        }

        if (mieiRistoranti.isEmpty()) {
            System.out.println("Non possiedi ancora nessun ristorante.");
            return;
        }

        System.out.print("\nInserisci il numero del ristorante: ");
        String input = scanner.nextLine();
        int sceltaNumero;
        try {
            sceltaNumero = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Input non valido, inserisci un numero.");
            return;
        }

        if (sceltaNumero < 1 || sceltaNumero > mieiRistoranti.size()) {
            System.out.println("Numero non valido.");
            return;
        }

        String nomeRistorante = mieiRistoranti.get(sceltaNumero - 1).getNome();

        // Verifica che il ristorante appartenga al proprietario (teoricamente già garantito dalla lista)
        if (!appartienePropietario(nomeRistorante)) {
            System.out.println("Il ristorante specificato non ti appartiene!");
            return;
        }

        boolean trovateRecensioni = false;

        for (Recensione recensione : listaRecensioni.getListaRecensione()) {
            if (recensione.getNomeRistorante().equals(nomeRistorante) &&
                    (recensione.getRisposta() == null || recensione.getRisposta().equals("null"))) {

                trovateRecensioni = true;
                System.out.println("\n----------------------------------------");
                System.out.printf("Recensione di: %s\n", recensione.getEmail());
                System.out.printf("Valutazione: %d/5\n", recensione.getValutazione());
                System.out.printf("Testo: %s\n", recensione.getRecensione());
                System.out.println("----------------------------------------");

                String scelta;
                do {
                    System.out.print("\nVuoi rispondere a questa recensione? (s/n): ");
                    scelta = scanner.nextLine().trim().toLowerCase();
                    if (!scelta.equals("s") && !scelta.equals("n")) {
                        System.out.println("Input non valido, inserisci 's' per sì o 'n' per no.");
                    }
                } while (!scelta.equals("s") && !scelta.equals("n"));

                if (scelta.equals("s")) {
                    System.out.print("Inserisci la tua risposta: ");
                    String risposta = scanner.nextLine();
                    recensione.risposta = risposta;
                    listaRecensioni.salvaRecensioniSuCSV();
                    System.out.println("Risposta aggiunta con successo!");
                }
            }
        }

        if (!trovateRecensioni) {
            System.out.println("Non ci sono nuove recensioni da rispondere per questo ristorante.");
        }
    }

    /**
     * Visualizza un riepilogo delle recensioni per tutti i ristoranti
     * associati all'utente ristoratore corrente.
     * <p>
     * Per ogni ristorante dell'utente, mostra il numero totale di recensioni ricevute
     * e la media delle valutazioni in stelle.
     * Se un ristorante non ha recensioni, viene indicato con un messaggio specifico.
     * Se l'utente non possiede ristoranti, viene visualizzato un messaggio appropriato.
     */
    //Metodo per visualizzare il riepilogo delle recensioni di tutti i ristoranti (la media e il numero di recensioni)
    private void visualizzaRiepilogo() {
        System.out.println("\n=== Riepilogo Recensioni ===");

        // Per ogni ristorante del proprietario
        for (Ristorante ristorante : listaRistoranti.getListaRistoranti()) {
            if (ristorante.getEmailRistoratore().equals(utenteCorrente.getEmail())) {

                // Variabili per calcolare le statistiche
                int numeroRecensioni = 0;
                double sommaStelle = 0;

                // Cerca tutte le recensioni per questo ristorante
                List<Recensione> recensioniRistorante = new ArrayList<>();
                for (Recensione recensione : listaRecensioni.getListaRecensione()) {
                    if (recensione.getNomeRistorante().equals(ristorante.getNome())) {
                        recensioniRistorante.add(recensione);
                        numeroRecensioni++;
                        sommaStelle += recensione.getValutazione();
                    }
                }

                // Stampa le statistiche del ristorante
                System.out.printf("\nRistorante: %s\n", ristorante.getNome());

                if (numeroRecensioni > 0) {
                    double mediaStelle = sommaStelle / numeroRecensioni;
                    System.out.printf("Numero recensioni: %d\n", numeroRecensioni);
                    System.out.printf("Media stelle: %.1f\n", mediaStelle);
                } else {
                    System.out.println("Nessuna recensione presente");
                }
            }
        }

        // Se non ci sono ristoranti per il proprietario
        if (!listaRistoranti.getListaRistoranti().stream()
                .anyMatch(r -> r.getEmailRistoratore().equals(utenteCorrente.getEmail()))) {
            System.out.println("Non possiedi ancora nessun ristorante.");
        }
    }

    /**
     * Verifica se un ristorante, identificato dal nome, appartiene
     * all'utente ristoratore corrente.
     *
     * @param nomeRistorante il nome del ristorante da verificare
     * @return true se il ristorante appartiene all'utente corrente,
     *         false altrimenti
     */
    private boolean appartienePropietario(String nomeRistorante) {
        return listaRistoranti.getListaRistoranti().stream()
                .anyMatch(r -> r.getNome().equals(nomeRistorante) &&
                        r.getEmailRistoratore().equals(utenteCorrente.getEmail()));
    }
}