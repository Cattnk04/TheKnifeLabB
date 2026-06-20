package main.java.shared.enums;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Enum che rappresenta i campi modificabili o consultabili
 * di una recensione.
 * <p>
 * Viene utilizzato principalmente nei layer DAO/Service
 * per identificare in modo tipizzato un campo della recensione
 * senza utilizzare stringhe "magiche".
 * </p>
 */
public enum CampoRecensione{
    RECENSIONE,
    VALUTAZIONE,
    RISPOSTA
}
