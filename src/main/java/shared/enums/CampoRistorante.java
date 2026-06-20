package main.java.shared.enums;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Enum che rappresenta i campi principali di un ristorante.
 * <p>
 * Viene utilizzato nei layer DAO/Service per identificare in modo tipizzato
 * i campi del ristorante, evitando l'uso di stringhe "magiche"
 * nelle query o nelle operazioni di aggiornamento e ricerca.
 * </p>
 */
public enum CampoRistorante {
    NOME,
    CITTA,
    NAZIONE,
    VIA,
    FASCIA_PREZZO
}