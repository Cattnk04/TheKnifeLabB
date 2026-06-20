package main.java.shared.enums;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Enum che rappresenta i campi principali di un utente.
 * <p>
 * Viene utilizzato nei layer DAO/Service per identificare in modo tipizzato
 * i campi dell'utente, evitando l'uso di stringhe "magiche"
 * nelle query o nelle operazioni di aggiornamento e ricerca.
 * </p>
 */
public enum CampoUtente{
    NOMEUTENTE,
    COGNOMEUTENTE,
    CITTA,
    NAZIONE,
    PASSWORD,
    RISTORATORE
}
