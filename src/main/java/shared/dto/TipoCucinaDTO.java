package main.java.shared.dto;

import java.io.Serializable;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * DTO utilizzato per rappresentare un tipo di cucina offerto da un ristorante.
 * <p>
 * Contiene l'identificativo e il nome del tipo di cucina, utilizzati per il
 * trasferimento dati tra client e server e per la visualizzazione nelle
 * interfacce grafiche (es. combo box di selezione).
 * </p>
 */
public class TipoCucinaDTO implements Serializable {
    private int idTipoCucina;
    private String tipoCucina;

    /**
     * Costruisce un nuovo DTO di tipo cucina.
     *
     * @param idTipoCucina identificativo del tipo di cucina
     * @param tipoCucina nome del tipo di cucina
     */
    public TipoCucinaDTO(int idTipoCucina, String tipoCucina) {
        this.idTipoCucina = idTipoCucina;
        this.tipoCucina = tipoCucina;
    }

    /**
     * Restituisce l'identificativo del tipo di cucina.
     *
     * @return l'id del tipo di cucina
     */
    public int getIdTipoCucina() {
        return idTipoCucina;
    }

    /**
     * Restituisce il nome del tipo di cucina.
     *
     * @return il nome del tipo di cucina
     */
    public String getTipoCucina() { return tipoCucina; }

    /**
     * Restituisce una rappresentazione testuale del tipo di cucina,
     * corrispondente al suo nome. Utile per la visualizzazione diretta
     * dell'oggetto nei componenti grafici (es. JComboBox).
     *
     * @return il nome del tipo di cucina
     */
    @Override
    public String toString() {
        return tipoCucina;
    }

    /**
     * Restituisce l'identificativo del tipo di cucina.
     *
     * @return l'id del tipo di cucina
     */
    public int getId() {
        return idTipoCucina;
    }
}
