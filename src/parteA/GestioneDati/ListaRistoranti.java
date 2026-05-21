package parteA.GestioneDati;

import Dominio.Recensione;
import Dominio.Ristorante;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.0
 *
 * Classe che racchiude l'insieme dei metodi per la gestione dei ristoranti.
 * Nello specifico per l'aggiunta, rimozione, la modifica e
 * la ricerca di essi.
 */

public class ListaRistoranti {

    public List<Ristorante> listaRistoranti = new ArrayList<>();

    /**
     * Costruttore della classe {@code ListaRistoranti}.
     * <p>
     * Se la lista dei ristoranti è vuota, carica i dati dal file CSV
     * tramite il metodo {@code ricavaRistorantiDaCSV()}.
     * </p>
     */
    //Costruttore
    public ListaRistoranti(){
        if(listaRistoranti.isEmpty())
            ricavaRistorantiDaCSV();
    }

    //Metodo Get
    public List<Ristorante> getListaRistoranti() {
        return this.listaRistoranti;
    }

    /**
     * Salva la lista dei ristoranti su un file CSV.
     * <p>
     * Scrive ogni ristorante presente nella lista {@code listaRistoranti}
     * nel file specificato da {@code Ristorante.FILE_RISTORANTI}.
     * </p>
     * <p>
     * In caso di errore di scrittura, stampa un messaggio di errore sulla console.
     * </p>
     */
    //Metodo per salvare sul CSV
    public void salvaRistorantiSuCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Ristorante.FILE_RISTORANTI))) {
            for (Ristorante r : listaRistoranti) {
                writer.write(r.toString());
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio dei ristoranti: " + e.getMessage());
        }
    }

    /**
     * Carica la lista dei ristoranti da un file CSV.
     * <p>
     * Legge il file specificato da {@code Ristorante.FILE_RISTORANTI},
     * crea oggetti {@code Ristorante} dai dati letti e li aggiunge a {@code listaRistoranti}.
     * Prima di caricare i dati, svuota la lista corrente.
     * </p>
     * <p>
     * Se si verifica un errore di lettura, stampa un messaggio di errore sulla console.
     * </p>
     */
    //Metodo per ricavare il ristorante dal CSV
    public void ricavaRistorantiDaCSV(){
        //lettura del file Ristoranti.txt e salvataggio nella lista listaRistoranti
        listaRistoranti.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(Ristorante.FILE_RISTORANTI))){
            String line;
            while((line = reader.readLine()) != null){
                String[] dati = line.split(",");
                Ristorante r = new Ristorante(
                        dati[0],    // nome
                        dati[1],    // email ristoratore
                        dati[2],    // nazione
                        dati[3],    //città
                        dati[4],    //indirizzo
                        Double.parseDouble(dati[5]),  //fascia prezzo
                        Boolean.parseBoolean(dati[6]),  //delivery
                        Boolean.parseBoolean(dati[7]),  //prenotazione online
                        dati[8]     // tipo cucina
                );
                listaRistoranti.add(r);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Ristoranti.txt non trovato. Verrà creata una nuova lista ristoranti.");
        }
        catch (IOException e) {
            System.out.println("Errore nel caricamento dei ristoranti: " + e.getMessage());
        }
    }

    /**
     * Inserisce un nuovo ristorante nella lista, se non è già presente.
     * <p>
     * Verifica che il ristorante non sia nullo e che non esista già un ristorante
     * con lo stesso nome (case insensitive) nella lista. In caso contrario,
     * stampa un messaggio di errore.
     * </p>
     *
     * @param ristorante Il ristorante da aggiungere alla lista.
     */
    //Metodo per inserire un ristorante
    public void inserisciRistorante(Ristorante ristorante) {
        //funzione per l'inserimento di un nuovo ristorante nella lista
        if(ristorante != null && !ristoranteDuplicato(ristorante))
            listaRistoranti.add(ristorante);
        else
            System.out.println("impossibile aggiungere il ristorante");
    }

    /**
     * Verifica se un ristorante è già presente nella lista.
     * <p>
     * Controlla se esiste un ristorante nella lista con lo stesso nome del ristorante fornito,
     * ignorando le differenze tra maiuscole e minuscole.
     * </p>
     *
     * @param ristorante Il ristorante da controllare.
     * @return {@code true} se un ristorante con lo stesso nome è già presente, {@code false} altrimenti.
     */
    private boolean ristoranteDuplicato(Ristorante ristorante) {
        for (Ristorante r : listaRistoranti) {
            if(r.getNome().equalsIgnoreCase(ristorante.getNome()))
                return true;
        }
        return false;
    }

    /**
     * Cerca un ristorante nella lista in base al nome fornito dall'utente.
     * <p>
     * Chiede all'utente di inserire il nome del ristorante tramite messaggio,
     * effettua la ricerca ignorando maiuscole e spazi iniziali/finali.
     * Se il ristorante viene trovato, lo restituisce; altrimenti stampa un messaggio
     * di errore e restituisce {@code null}.
     * </p>
     *
     * @param messaggio Il messaggio mostrato all'utente per richiedere l'inserimento.
     * @return Il {@code Ristorante} trovato, oppure {@code null} se non esiste.
     */
    //Cerca ristorante con filtri
    public Ristorante cercaPerNome(String messaggio){
        //Funzione per cercare un ristorante in base al suo nome
        Scanner scanner = new Scanner(System.in);
        System.out.print(messaggio);
        String nomeRistorante = scanner.nextLine().toLowerCase().trim();
        for (Ristorante r : listaRistoranti) {
            if(nomeRistorante.equals(r.getNome().toLowerCase().trim())){
                return r;
            }
        }
        System.out.print("Il nome del ristorante che hai inserito non esiste!");
        return null;
    }

    /**
     * Stampa una lista di ristoranti filtrati e permette di visualizzarne i dettagli.
     * <p>
     * Se la lista è vuota, mostra un messaggio informativo.
     * Altrimenti, elenca i ristoranti numerati e consente all'utente di selezionarne uno
     * per vedere i dettagli o di uscire.
     * Gestisce input errati e loop finché l'utente non decide di uscire.
     * </p>
     *
     * @param ristoranti La lista di ristoranti da mostrare.
     */
    private void stampaRistorantiFiltrati(List<Ristorante> ristoranti) {
        if (ristoranti.isEmpty()) {
            System.out.println("\nNessun ristorante trovato con i criteri specificati.");
            return;
        }

        boolean continua = true;
        while (continua) {
            System.out.println("\n=== Ristoranti trovati ===");
            for (int i = 0; i < ristoranti.size(); i++) {
                System.out.println((i + 1) + ". " + ristoranti.get(i).getNome());
            }
            System.out.println("0. Esci");

            Scanner scanner = new Scanner(System.in);
            System.out.print("\nInserisci il numero del ristorante per visualizzare più dettagli (0 per uscire): ");
        
            try {
                int scelta = Integer.parseInt(scanner.nextLine().trim());
                if (scelta == 0) {
                    continua = false;
                } else if (scelta >= 1 && scelta <= ristoranti.size()) {
                    if (mostraDettagliRistorante(ristoranti.get(scelta - 1))) {
                        return; // Ritorna al menu principale
                    }
                } else {
                    System.out.println("Numero non valido!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input non valido! Devi inserire un numero.");
            }
        }
    }

    /**
     * Mostra i dettagli completi di un ristorante e offre opzioni interattive all'utente.
     * <p>
     * Visualizza nome, città, indirizzo, fascia di prezzo, tipo di cucina,
     * disponibilità di delivery e prenotazione online.
     * Permette all'utente di:
     * <ul>
     *   <li>Visualizzare le recensioni del ristorante</li>
     *   <li>Tornare alla lista dei ristoranti</li>
     *   <li>Tornare al menu principale</li>
     * </ul>
     * La funzione ritorna {@code true} se l'utente vuole tornare al menu principale,
     * {@code false} se desidera tornare alla lista dei ristoranti.
     * </p>
     *
     * @param ristorante Il ristorante di cui mostrare i dettagli.
     * @return {@code true} per tornare al menu principale, {@code false} per tornare alla lista.
     */
    private boolean mostraDettagliRistorante(Ristorante ristorante) {
        // Mostra i dettagli del ristorante solo la prima volta
        System.out.println("\n=== Dettagli del ristorante ===");
        System.out.println("Nome: " + ristorante.getNome());
        System.out.println("Città: " + ristorante.getCitta());
        System.out.println("Indirizzo: " + ristorante.getIndirizzo());
        System.out.println("Fascia di prezzo: " + ristorante.getFasciaPrezzo());
        System.out.println("Tipo di cucina: " + ristorante.getTipoCucina());
        System.out.println("Servizio delivery: " + (ristorante.getServizioDelivery() ? "Sì" : "No"));
        System.out.println("Prenotazione online: " + (ristorante.getServizioPrenotazioneOnline() ? "Sì" : "No"));
        System.out.println("----------------------------------------");

        boolean continua = true;
        Scanner scanner = new Scanner(System.in);
        
        while (continua) {
            System.out.print("\nVuoi: \n1. Visualizzare le recensioni del ristorante\n2. Tornare alla lista dei ristoranti\n3. Tornare al menu principale\nScelta: ");
            String scelta = scanner.nextLine().trim();
            switch (scelta) {
                case "1":
                    ListaRecensioni listaRecensioni = new ListaRecensioni();
                    List<Recensione> recensioniRistorante = listaRecensioni.recensioniRistorante(ristorante.getNome());
                    if (recensioniRistorante != null) {
                        listaRecensioni.mostraRecensioniRistorante(ristorante);
                    } else {
                        System.out.println("----------------------------------------");
                        System.out.println("Non ci sono ancora recensioni per questo ristorante.");
                        System.out.println("----------------------------------------");
                    }
                    break;
                case "2":
                    return false; // Torna alla lista dei ristoranti
                case "3":
                    return true;  // Torna al menu principale
                default:
                    System.out.println("Scelta non valida!");
            }
        }
        return false;
    }

    /**
     * Permette all'utente di cercare ristoranti applicando filtri multipli.
     * <p>
     * Il filtro sulla città è obbligatorio; successivamente l'utente può scegliere
     * se applicare filtri su fascia di prezzo, delivery, prenotazione online e tipo di cucina.
     * I risultati filtrati vengono mostrati e l'utente può selezionare un ristorante
     * per vedere maggiori dettagli.
     * </p>
     *
     * @return Lista di ristoranti filtrati in base alle scelte dell'utente.
     */
    //Metodo per la ricerca dei ristoranti
    public List<Ristorante> cercaRistorante(){
        List<Ristorante> filtrati = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        String sn;

        // Filtro per città (obbligatorio)
        boolean cittaTrovata = false;
        while (!cittaTrovata) {
            System.out.print("\nInserici la città in cui vuoi cercare il ristorante: ");
            String citta = scanner.nextLine().trim().toLowerCase();
            filtrati.clear(); // Puliamo la lista prima di ogni nuovo tentativo

            if (filtraPerCitta(filtrati, citta) == null) {
                System.out.println("Nessun ristorante trovato in questa città.");
                System.out.print("Vuoi cercare in un'altra città? [s/n]: ");
                String risposta;
                do {
                    risposta = scanner.nextLine().trim().toLowerCase();
                } while (!risposta.equals("s") && !risposta.equals("n"));

                if (risposta.equals("n")) {
                    return new ArrayList<>(); // Ritorna una lista vuota se l'utente non vuole continuare
                }
                // Se l'utente risponde 's', il ciclo continua e chiede una nuova città
            } else {
                cittaTrovata = true;
            }
        }
        // Filtro per prezzo
        System.out.print("Vuoi cercare per fascia di prezzo? [s/n]: ");
        do{
            sn = scanner.nextLine().trim().toLowerCase();
        }while(!sn.equals("s") && !sn.equals("n"));

        if(sn.equals("s")){
            double prezzoMin, prezzoMax;
            try {
                do {
                    System.out.print("Inserisci il prezzo minimo: ");
                    prezzoMin = scanner.nextDouble();
                    System.out.print("Inserisci il prezzo massimo: ");
                    prezzoMax = scanner.nextDouble();
                    if (prezzoMin > prezzoMax) {
                        System.out.println("Il prezzo minimo non può essere maggiore del prezzo massimo.");
                    }
                } while (prezzoMax < prezzoMin);
                filtraPerPrezzo(filtrati, prezzoMax, prezzoMin);
                scanner.nextLine(); // Consumare il newline rimasto
            } catch (NumberFormatException e) {
                System.out.println("Inserisci un numero valido!");
            }
        }
        // Filtro per delivery
        System.out.print("Vuoi cercare solo i ristoranti con servizio delivery? [s/n]: ");
        do{
            sn = scanner.nextLine().trim().toLowerCase();
        }while(!sn.equals("s") && !sn.equals("n"));
        if(sn.equals("s")){  // Rimosso il controllo !filtrati.isEmpty()
            filtraPerDelivery(filtrati);
        }
        // Filtro per prenotazione online
        System.out.print("Vuoi cercare ristoranti con solo prenotazione online? [s/n]: ");
        do{
            sn = scanner.nextLine().trim().toLowerCase();
        }while(!sn.equals("s") && !sn.equals("n"));
        if(sn.equals("s")){  // Rimosso il controllo !filtrati.isEmpty()
            filtraPerPrenotazioneOnline(filtrati);
        }
        // Filtro per tipo cucina
        System.out.print("Vuoi cercare solo i ristoranti con un tipo di cucina specifico? [s/n]: ");
        do{
            sn = scanner.nextLine().trim().toLowerCase();
        }while(!sn.equals("s") && !sn.equals("n"));
        if(sn.equals("s")){  // Rimosso il controllo !filtrati.isEmpty()
            System.out.print("Inserisci il tipo di cucina specifico: ");
            String tipoCucina = scanner.nextLine().trim().toLowerCase();
            filtraPerTipoCucina(filtrati, tipoCucina);
        }

        stampaRistorantiFiltrati(filtrati);
        return filtrati;
    }

    /**
     * Filtra i ristoranti per città, aggiungendo alla lista filtrati
     * solo quelli che corrispondono alla città specificata.
     *
     * @param filtrati lista di ristoranti su cui applicare il filtro (verrà modificata)
     * @param citta la città in cui cercare i ristoranti (case-insensitive)
     * @return la lista filtrati aggiornata se contiene elementi, {@code null} se nessun ristorante trovato
     */
    private List<Ristorante> filtraPerCitta(List<Ristorante> filtrati, String citta){
        for(Ristorante r : listaRistoranti){
            if(r.getCitta().toLowerCase().equalsIgnoreCase(citta)){
                filtrati.add(r);
            }
        }
        System.out.println("Filtro per città inserito");
        if(filtrati.isEmpty()) {
            return null;
        } else {
            return filtrati;
        }
    }
    /**
     * Filtra i ristoranti mantenendo solo quelli con fascia prezzo compresa
     * tra prezzoMin e prezzoMax (inclusi).
     *
     * @param filtrati lista di ristoranti su cui applicare il filtro (verrà modificata)
     * @param prezzoMax valore massimo della fascia prezzo
     * @param prezzoMin valore minimo della fascia prezzo
     */
    private void filtraPerPrezzo(List<Ristorante> filtrati, double prezzoMax, double prezzoMin){
        filtrati.removeIf(r -> r.getFasciaPrezzo() < prezzoMin || r.getFasciaPrezzo() > prezzoMax);
        System.out.println("Filtro per prezzo medio inserito.");
    }
    /**
     * Filtra i ristoranti mantenendo solo quelli che offrono servizio di delivery.
     *
     * @param filtrati lista di ristoranti su cui applicare il filtro (verrà modificata)
     */
    private void filtraPerDelivery(List<Ristorante> filtrati){
        filtrati.removeIf(r -> !r.getServizioDelivery());
        System.out.println("Filtro per delivery medio inserito.");
    }
    /**
     * Filtra i ristoranti mantenendo solo quelli che offrono prenotazione online.
     *
     * @param filtrati lista di ristoranti su cui applicare il filtro (verrà modificata)
     */
    private void filtraPerPrenotazioneOnline(List<Ristorante> filtrati){
        filtrati.removeIf(r -> !r.getServizioPrenotazioneOnline());
        System.out.println("Filtro per prenotazione online inserito.");
    }
    /**
     * Filtra i ristoranti mantenendo solo quelli che hanno un tipo di cucina
     * uguale a quello specificato (case-insensitive).
     *
     * @param filtrati lista di ristoranti su cui applicare il filtro (verrà modificata)
     * @param tipoCucina il tipo di cucina da filtrare
     */
    private void filtraPerTipoCucina(List<Ristorante> filtrati, String tipoCucina){
        filtrati.removeIf(r -> !r.getTipoCucina().equalsIgnoreCase(tipoCucina));
        System.out.println("Filtro per tipo cucina inserito.");
    }

}