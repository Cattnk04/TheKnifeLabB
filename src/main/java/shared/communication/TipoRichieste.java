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

    GET_UTENTE,

    GET_RISTORANTE,
    CREA_RISTORANTE,
    AGGIORNA_RISTORANTE,
    ELIMINA_RISTORANTE,
    CERCA_RISTORANTE,
    ESISTE_RISTORANTE,

    GET_RECENSIONI_RISTORANTE,
    GET_RECENSIONI_UTENTE,
    GET_RECENSIONI_BYEMAIL,
    SCRIVI_RECENSIONE,
    MODIFICA_RECENSIONE,
    ELIMINA_RECENSIONE,
    RISPONDI_RECENSIONE,
    MODIFICA_RISPOSTA,
    RIEPILOGO_RECENSIONE,

    GET_PREFERITI,
    AGGIUNGI_PREFERITO,
    RIMUOVI_PREFERITO,
    ESISTE_PREFERITO,
    TOGGLE_PREFERITO,

    GET_TIPO_CUCINA,

    // Richiesta per terminare il server
    SHUTDOWN_SERVER
}