package main.java.shared.dto;

import java.io.Serializable;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * DTO utilizzato per rappresentare il riepilogo delle recensioni di un ristorante.
 * <p>
 * Contiene informazioni aggregate come il numero totale di recensioni
 * e la media delle valutazioni assegnate dagli utenti.
 * Viene utilizzato per fornire una sintesi delle recensioni senza
 * includere il dettaglio delle singole valutazioni.
 * </p>
 */
public class RiepilogoRecensioniDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int numeroRecensioni;
    private final double mediaValutazioni;

    /**
     * Costruisce un nuovo DTO di riepilogo recensioni.
     *
     * @param numeroRecensioni numero totale di recensioni
     * @param mediaValutazioni media delle valutazioni (es. da 1.0 a 5.0)
     */
    public RiepilogoRecensioniDTO(int numeroRecensioni, double mediaValutazioni) {
        this.numeroRecensioni = numeroRecensioni;
        this.mediaValutazioni = mediaValutazioni;
    }

    /**
     * Restituisce il numero totale di recensioni.
     * @return numero recensioni
     */
    public int getNumeroRecensioni() {return numeroRecensioni;}

    /**
     * Restituisce la media delle valutazioni.
     * @return media valutazioni
     */
    public double getMediaValutazioni() {
        return mediaValutazioni;
    }
}