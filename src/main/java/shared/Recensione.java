package Dominio;

import GestioneDati.ListaRistoranti;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.0
 *
 * Classe per gestione della generazione dell'elemento Recensione.
 */

public class Recensione {

    public static final String FILE_RECENSIONI = "Data/Recensioni.txt";
    String email;
    String nomeRistorante;
    int valutazione;
    String recensione;
    public String risposta;


    /**
     * Costruttore della classe Recensione che crea un'istanza
     * utilizzando un oggetto Utente e una ListaRistoranti.
     * Richiede il nome del ristorante a cui lasciare la recensione,
     * assegna la valutazione e il testo della recensione tramite metodi
     * (presumibilmente input da utente) e inizializza la risposta a null.
     *
     * @param utente l'utente che lascia la recensione
     * @param ristoranti la lista di ristoranti da cui scegliere il ristorante da recensire
     */
    public Recensione(Utente utente, ListaRistoranti ristoranti) {
        this.email = utente.getEmail();
        this.nomeRistorante = ristoranti.cercaPerNome("Inserisci il nome del ristorante a cui vuoi lasciare una recensione: ").getNome();
        this.valutazione = valutazione();
        this.recensione = recensione();
        this.risposta = null;
    }

    /**
     * Costruttore della classe Recensione che crea un'istanza
     * utilizzando direttamente tutti i dati della recensione.
     *
     * @param email l'email dell'utente che ha lasciato la recensione
     * @param nomeRistorante il nome del ristorante recensito
     * @param valutazione il punteggio dato al ristorante
     * @param recensione il testo della recensione
     * @param risposta la risposta alla recensione (può essere null)
     */
    public Recensione(String email, String nomeRistorante, int valutazione, String recensione, String risposta) {
        this.email = email;
        this.nomeRistorante = nomeRistorante;
        this.valutazione = valutazione;
        this.recensione = recensione;
        this.risposta = risposta;
    }

    /**
     * Restituisce l'email dell'utente che ha lasciato la recensione.
     *
     * @return la email dell'utente
     */
    public String getEmail(){
        return email;
    }

    /**
     * Restituisce il nome del ristorante recensito.
     *
     * @return il nome del ristorante
     */
    public String getNomeRistorante(){
        return nomeRistorante;
    }

    /**
     * Restituisce la valutazione data al ristorante.
     *
     * @return la valutazione (intero)
     */
    public int getValutazione(){
        return valutazione;
    }

    /**
     * Restituisce il testo della recensione.
     *
     * @return la recensione scritta dall'utente
     */
    public String getRecensione(){
        return recensione;
    }

    /**
     * Restituisce la risposta data alla recensione.
     *
     * @return la risposta alla recensione, può essere null se non presente
     */
    public String getRisposta(){
        return risposta;
    }

    /**
     * Imposta o aggiorna il testo della recensione.
     *
     * @param recensione il nuovo testo della recensione
     */
    public void setRecensione(String recensione){
        this.recensione = recensione;
    }

    /**
     * Imposta o aggiorna la valutazione del ristorante.
     *
     * @param valutazione il nuovo punteggio (intero)
     */
    public void setValutazione(int valutazione){
        this.valutazione = valutazione;
    }

    /**
     * Richiede all'utente di inserire una valutazione intera compresa tra 1 e 5.
     * Continua a richiedere l'input finché non viene fornito un valore valido.
     * Gestisce input non validi e mostra messaggi di errore appropriati.
     *
     * @return la valutazione inserita dall'utente (intero tra 1 e 5)
     */
    //Metodo per inserire valutazione
    public int valutazione(){
        Scanner scanner = new Scanner(System.in);
        int valutazione;
        do{
            System.out.print("Puoi inserire una valutazione da 1 a 5: ");
            try {
                valutazione = scanner.nextInt();
                scanner.nextLine(); // consuma il newline
                if (valutazione >= 1 && valutazione <= 5) {
                    return valutazione;
                }
                System.out.println("Per favore inserisci un numero tra 1 e 5.");
            } catch (InputMismatchException e) {
                System.out.println("Per favore inserisci un numero valido.");
                scanner.nextLine(); // pulisce l'input non valido
                valutazione = 0;
            }
        } while (true);
    }

    /**
     * Metodo che permette all'utente di inserire una recensione testuale
     * per un ristorante tramite input da tastiera.
     *
     * @return la recensione inserita dall'utente sotto forma di stringa
     */
    //Metodo per inserire recensione
    public String recensione(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Puoi inserire una recensione a questo ristorante: ");
        String recensione = scanner.nextLine();
        return recensione;
    }

    /**
     * Restituisce una rappresentazione testuale compatta dell'oggetto Recensione,
     * con i campi separati da asterischi '*'.
     *
     * @return la stringa rappresentativa dell'oggetto Recensione
     */
    //Metodo to String
    @Override
    public String toString(){
        return this.email + '*' + this.nomeRistorante + '*' + this.valutazione + '*' + this.recensione + '*' + this.risposta;
    }

    /**
     * Restituisce una stringa formattata per stampare i dettagli della recensione,
     * inclusa la valutazione, il testo della recensione e, se presente, la risposta.
     * Stampa anche una linea separatrice sulla console.
     *
     * @return la stringa formattata con i dettagli della recensione
     */
    //Metodo per stampare le recensioni
    public String stampaRecensione(){
        System.out.println("\n----------------------------------------");
        String stringa =  "Valutazione: " + getValutazione() + "\nRecensione: " + getRecensione();
        if(getRisposta() != null){
            stringa += "\nRisposta: " + getRisposta();
        }
        return stringa;
    }

}