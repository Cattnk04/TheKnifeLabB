package main.java.shared.dto;

import java.io.Serializable;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.0
 *
 * DTO utilizzato per rappresentare i criteri di ricerca di un ristorante.
 * <p>
 * A differenza di {@link RistoranteDTOprova}, ogni campo è opzionale: un valore
 * {@code null} indica che il relativo criterio non è stato specificato
 * dall'utente e quindi non deve essere applicato come filtro.
 * </p>
 */
public class FiltroRicercaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nomeRistorante;
    private final String citta;
    private final String nazione;
    private final Integer fasciaPrezzoMassima;
    private final Integer delivery;           // 1 = sì, 0 = no, null = indifferente
    private final Integer prenotazioneOnline;  // 1 = sì, 0 = no, null = indifferente
    private final Integer tipoCucina;          // null = qualsiasi

    /**
     * Costruisce un nuovo filtro di ricerca.
     *
     * @param nomeRistorante nome (anche parziale) del ristorante da cercare, o {@code null}
     * @param citta città in cui cercare, o {@code null}
     * @param nazione nazione in cui cercare, o {@code null}
     * @param fasciaPrezzoMassima prezzo massimo desiderato, o {@code null} per qualsiasi prezzo
     * @param delivery preferenza sul delivery (1 = sì, 0 = no, {@code null} = indifferente)
     * @param prenotazioneOnline preferenza sulla prenotazione online (1 = sì, 0 = no, {@code null} = indifferente)
     * @param tipoCucina identificativo del tipo di cucina desiderato, o {@code null} per qualsiasi tipo
     */
    public FiltroRicercaDTO(String nomeRistorante, String citta, String nazione,
                            Integer fasciaPrezzoMassima, Integer delivery,
                            Integer prenotazioneOnline, Integer tipoCucina) {
        this.nomeRistorante = nomeRistorante;
        this.citta = citta;
        this.nazione = nazione;
        this.fasciaPrezzoMassima = fasciaPrezzoMassima;
        this.delivery = delivery;
        this.prenotazioneOnline = prenotazioneOnline;
        this.tipoCucina = tipoCucina;
    }

    public String getNomeRistorante() { return nomeRistorante; }
    public String getCitta() { return citta; }
    public String getNazione() { return nazione; }
    public Integer getFasciaPrezzoMassima() { return fasciaPrezzoMassima; }
    public Integer getDelivery() { return delivery; }
    public Integer getPrenotazioneOnline() { return prenotazioneOnline; }
    public Integer getTipoCucina() { return tipoCucina; }
}