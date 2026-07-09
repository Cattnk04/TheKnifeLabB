package main.java.server.exception;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Eccezione non controllata utilizzata per segnalare violazioni delle
 * regole di business relative alle recensioni (es. valutazione fuori
 * dall'intervallo consentito, recensione duplicata, dati non validi).
 */
public class RecensioniException extends RuntimeException {

    /**
     * Costruisce una nuova eccezione con il messaggio specificato.
     *
     * @param message messaggio descrittivo dell'errore
     */
    public RecensioniException(String message) {
        super(message);
    }

    /**
     * Costruisce una nuova eccezione con il messaggio e la causa specificati.
     *
     * @param message messaggio descrittivo dell'errore
     * @param cause eccezione originaria che ha causato l'errore
     */
    public RecensioniException(String message, Throwable cause) {
        super(message, cause);
    }
}

