package main.java.shared.communication;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Enum che rappresenta i tipi di richieste possibili
 * inviate dal client al server.
 * <p>
 * Ogni valore identifica un'operazione supportata dal sistema,
 * come autenticazione, gestione ristoranti, recensioni e preferiti.
 * </p>
 */
public enum TipoRichieste {
    LOGIN,
    REGISTER,
    LOGOUT,

    GET_RISTORANTE,
    CREA_RISTORANTE,

    GET_RECENSIONI_RISTORANTE,
    SCRIVI_RECENSIONE,
    MODIFICA_RECENSIONE,
    ELIMINA_RECENSIONE,
    RISPONDI_RECENSIONE,

    AGGIUNGI_PREFERITO,
    RIMUOVI_PREFERITO,

    GET_TIPO_CUCINA,
    // Richiesta per terminare il server
    SHUTDOWN_SERVER
}