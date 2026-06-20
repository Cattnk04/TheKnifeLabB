package main.java.shared.communication;

import java.io.Serializable;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Classe utilizzata per rappresentare una richiesta inviata dal client al server.
 * <p>
 * Contiene il tipo della richiesta e il contenuto associato, che può variare
 * a seconda dell'operazione richiesta (DTO o altri oggetti serializzabili).
 * </p>
 */
public class Richiesta implements Serializable {

    private static final long serialVersionUID = 1L;

    public TipoRichieste tipoRichiesta;
    public Object contenuto;

    /**
     * Costruisce una nuova richiesta.
     *
     * @param tipoRichiesta tipo dell'operazione richiesta
     * @param contenuto oggetto contenente i dati della richiesta (DTO o altro)
     */
    public Richiesta(TipoRichieste tipoRichiesta, Object contenuto) {
        this.tipoRichiesta = tipoRichiesta;
        this.contenuto = contenuto;
    }

    /**
     * Restituisce il tipo della richiesta.
     * @return tipo della richiesta
     */
    public TipoRichieste getTipoRichiesta() {
        return tipoRichiesta;
    }

    /**
     * Restituisce il contenuto della richiesta.
     * @return oggetto contenuto nella richiesta
     */
    public Object getContenuto() {
        return contenuto;
    }
}
