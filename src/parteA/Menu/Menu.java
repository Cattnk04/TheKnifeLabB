package parteA.Menu;


import main.java.shared.domain.Utente;
import parteA.GestioneDati.ListaRistoranti;
import parteA.GestioneDati.ListaUtenti;

import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.0
 *
 * La classe {@code parteA.Menu} rappresenta il menu principale dell'applicazione.
 * Gestisce la logica per utenti ospiti, registrazione, login e accesso ai sottomenu
 * per ristoratori o utenti registrati.
 *
 * Il menu offre le seguenti opzioni:
 * <ul>
 *   <li>Registrazione nuovo utente</li>
 *   <li>Login utente esistente</li>
 *   <li>Ricerca ristorante</li>
 *   <li>Uscita dall'applicazione</li>
 * </ul>
 *
 */

public class Menu {

    private static final Scanner scanner = new Scanner(System.in);
    private MenuRistoratore menuRistoratore;
    private MenuUtenteLog menuUtenteLog;

    /**
     * Costruttore della classe {@code parteA.Menu}.
     * <p>
     * Inizializza le liste degli utenti e dei ristoranti.
     * Presenta un menu interattivo per l'utente guest con le seguenti opzioni:
     * <ul>
     *   <li>Registrazione di un nuovo utente</li>
     *   <li>Login di un utente esistente</li>
     *   <li>Ricerca di un ristorante</li>
     *   <li>Uscita dal programma</li>
     * </ul>
     * <p>
     * Gestisce le eccezioni relative all'input errato o altri errori imprevisti.
     * Se un utente si registra o fa login con successo, viene aperto il menu dedicato
     * in base al tipo di utente (ristoratore o utente normale).
     */
    public Menu(){
    //Creazione delle diverse liste per l'accesso ai dati
    ListaUtenti listaUtenti = new ListaUtenti();
    ListaRistoranti listaRistoranti = new ListaRistoranti();
    int scelta;
    Utente utenteCorrente;
    
        do {
            try {
                scelta = menuGuest();
                switch(scelta){
                    case 1:
                        utenteCorrente = registraUtente(listaUtenti);
                        if(utenteCorrente != null){
                            System.out.println("Registrazione avvenuta con successo!");
                            if(utenteCorrente.getRistoratore()) {
                                menuRistoratore = new MenuRistoratore(utenteCorrente);
                            } else {
                                menuUtenteLog = new MenuUtenteLog(utenteCorrente);
                            }
                        }
                        break;
                    case 2:
                        utenteCorrente = loginUtente(listaUtenti);
                        if(utenteCorrente != null){
                            System.out.println("Login avvenuto con successo!");
                            if(utenteCorrente.getRistoratore()) {
                                menuRistoratore = new MenuRistoratore(utenteCorrente);
                            } else {
                                menuUtenteLog = new MenuUtenteLog(utenteCorrente);
                            }
                        }
                        break;
                    case 3:
                        listaRistoranti.cercaRistorante();
                        break;
                    case 0:
                        System.out.println("Grazie per aver usato il nostro servizio!");
                        break;
                    default:
                        System.out.println("Scelta non valida!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Inserire un numero valido!");
                scanner.nextLine(); // Pulizia del buffer
                scelta = -1;
            } catch (Exception e) {
                System.out.println("Errore imprevisto: " + e.getMessage());
                scelta = -1;
            }
        } while (scelta != 0);
    }
    /**
     * Mostra il menu principale per un utente ospite e acquisisce la scelta da tastiera.
     *  * <p>
     *  * Le opzioni presentate sono:
     *  * <ul>
     *  *   <li>1 - Registrati</li>
     *  *   <li>2 - Accedi</li>
     *  *   <li>3 - Cerca ristorante</li>
     *  *   <li>0 - Logout</li>
     *  * </ul>
     *  * <p>
     *  * Gestisce l'input non numerico segnalando un errore e pulendo il buffer di input.
     *  *
     *  * @return un intero corrispondente alla scelta dell'utente, o -1 se l'input non è valido
     */
    //Metodo per la scelta fatta dell'utente ospite
    public static int menuGuest() {
        int choice = -1;
        System.out.println("\nBenvenuto nella schermata home ospite!");
        System.out.println("Scegli un'opzione:");
        System.out.println("1. Registrati");
        System.out.println("2. Accedi");
        System.out.println("3. Cerca ristorante");
        System.out.println("0. Logout");
        System.out.print("La tua scelta: ");

        try {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                throw new InputMismatchException("Input non numerico.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Input non valido! Inserisci un numero.");
        } finally {
            scanner.nextLine(); // pulizia del buffer
        }
        scanner.nextLine(); // pulizia della linea
        return choice;
    }

    /**
     * Registra un nuovo utente creando un oggetto {@code Utente} e aggiungendolo alla lista utenti.
     * <p>
     * Viene creato un nuovo utente (presumibilmente con dati raccolti nel costruttore di {@code Utente}).
     * Se l'aggiunta alla lista ha successo, restituisce l'utente appena creato.
     * In caso di errore durante la creazione o l'aggiunta, stampa un messaggio e ritorna {@code null}.
     *
     * @param listaUtenti la lista degli utenti in cui aggiungere il nuovo utente
     * @return il nuovo utente aggiunto se la registrazione ha successo, {@code null} altrimenti
     */
    //Metodo per la registrazione
    public static Utente registraUtente(ListaUtenti listaUtenti){
        try {
            Utente nuovoUtente = new Utente();
            return listaUtenti.aggiungiUtente(nuovoUtente);
        } catch (Exception e) {
            System.out.println("Errore nella creazione dell'utente: " + e.getMessage());
            return null;
        }
    }

    /**
     * Esegue il processo di login delegando la richiesta alla lista utenti.
     * <p>
     * Chiama il metodo {@code loginUtente()} della classe {@code ListaUtenti} per gestire
     * l'interazione con l'utente e l'autenticazione.
     * In caso di eccezioni, stampa un messaggio di errore e restituisce {@code null}.
     *
     * @param listaUtenti la lista degli utenti su cui effettuare il login
     * @return l'utente autenticato se il login ha successo, {@code null} in caso di errore o fallimento
     */
    //Metodo per il login
    public static Utente loginUtente(ListaUtenti listaUtenti) {
        try {
            return listaUtenti.loginUtente();
        } catch (Exception e) {
            System.out.println("Errore durante il login: " + e.getMessage());
            return null;
        }
    }
}