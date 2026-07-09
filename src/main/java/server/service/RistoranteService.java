package main.java.server.service;

import main.java.server.dao.RistoranteDAO;
import main.java.shared.domain.Ristorante;
import main.java.shared.enums.CampoRistorante;
import main.java.shared.dto.FiltroRicercaDTO;
import main.java.shared.dto.RistoranteDTO;

import java.util.List;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Service che espone la logica applicativa relativa ai ristoranti: creazione,
 * modifica, cancellazione, ricerca e conversione tra le entità di dominio
 * ({@link Ristorante}) e i DTO ({@link RistoranteDTO}) utilizzati per la
 * comunicazione con il client. Effettua inoltre la validazione dei dati
 * tramite {@link ValidationUtils} prima di delegare l'accesso ai dati al
 * {@link RistoranteDAO}.
 */
public class RistoranteService {

    private final RistoranteDAO ristoranteDAO;

    /**
     * Costruisce un nuovo service per la gestione dei ristoranti.
     *
     * @param ristoranteDAO il DAO da utilizzare per l'accesso ai dati dei ristoranti
     */
    public RistoranteService(RistoranteDAO ristoranteDAO) {
        this.ristoranteDAO = ristoranteDAO;
    }

    /**
     * Converte un {@link RistoranteDTO} nella corrispondente entità di dominio {@link Ristorante}.
     *
     * @param dto il DTO da convertire
     * @return l'entità {@link Ristorante} corrispondente
     */
    private Ristorante toEntity(RistoranteDTO dto) {
        Ristorante r = new Ristorante();

        r.setNomeRistorante(dto.getNomeRistorante());
        r.setEmail(dto.getEmail());
        r.setCitta(dto.getCitta());
        r.setNazione(dto.getNazione());
        r.setVia(dto.getVia());
        r.setNumeroCivico(dto.getNumeroCivico());
        r.setFasciaPrezzo(dto.getFasciaPrezzo());
        r.setDelivery(dto.isDelivery());
        r.setPrenotazioneOnline(dto.isPrenotazioneOnline());
        r.setTipoCucina(dto.getTipoCucina());

        return r;
    }

    /**
     * Converte un'entità di dominio {@link Ristorante} nel corrispondente {@link RistoranteDTO}.
     *
     * @param r l'entità da convertire
     * @return il {@link RistoranteDTO} corrispondente
     */
    private RistoranteDTO toDTO(Ristorante r) {
        return new RistoranteDTO(
                r.getIdRistorante(),
                r.getNomeRistorante(),
                r.getEmail(),
                r.getCitta(),
                r.getNazione(),
                r.getVia(),
                r.getNumeroCivico(),
                r.getFasciaPrezzo(),
                r.getDelivery(),
                r.getPrenotazioneOnline(),
                r.getIdTipoCucina()
        );
    }

    /**
     * Aggiorna tutti i dati di un ristorante esistente.
     *
     * @param dto il {@link RistoranteDTO} con i nuovi dati del ristorante (deve contenere l'id)
     * @return true se l'aggiornamento va a buon fine, false altrimenti
     */
    public boolean aggiornaRistorante(RistoranteDTO dto){
        return ristoranteDAO.aggiornaRistorante(dto);
    }

    /**
     * Crea un nuovo ristorante, dopo aver validato nome, città, nazione, via e numero
     * civico, e aver verificato che il ristoratore non abbia già un ristorante con lo
     * stesso nome.
     *
     * @param dto il {@link RistoranteDTO} con i dati del nuovo ristorante
     * @return true se la creazione va a buon fine, false altrimenti
     * @throws IllegalArgumentException se {@code dto} è {@code null}, se i dati non superano
     * la validazione, oppure se il ristoratore ha già un ristorante con lo stesso nome
     */
    public boolean creaRistorante(RistoranteDTO dto) {

        if (dto == null) {throw new IllegalArgumentException("DTO nullo");}
        if (!ValidationUtils.isValidName(dto.getNomeRistorante())) {
            throw new IllegalArgumentException("Nome non valido");
        }
        if (!ValidationUtils.isValidCitta(dto.getCitta())) {
            throw new IllegalArgumentException("Città non valida");
        }
        if (!ValidationUtils.isValidNazione(dto.getNazione())) {
            throw new IllegalArgumentException("Nazione non valida");
        }
        if (!ValidationUtils.isValidVia(dto.getVia())) {
            throw new IllegalArgumentException("Via non valida");
        }
        if (!ValidationUtils.isValidNumeroCivico(dto.getNumeroCivico())) {
            throw new IllegalArgumentException("Numero civico non valido");
        }

        // Controllo duplicato: stesso ristoratore (email) con lo stesso nome ristorante
        if (ristoranteDAO.esisteRistorante(dto.getNomeRistorante(), dto.getEmail())) {
            throw new IllegalArgumentException("Hai già un ristorante con questo nome");
        }

        Ristorante entity = toEntity(dto);
        return ristoranteDAO.inserisciRistorante(entity);
    }

    /**
     * Aggiorna un singolo campo di un ristorante esistente, dopo averne validato il
     * nuovo valore in base al tipo di campo indicato.
     *
     * @param idRistorante id del ristorante da aggiornare
     * @param campo il campo da aggiornare ({@code NOME}, {@code CITTA}, {@code NAZIONE}, {@code VIA} o {@code FASCIA_PREZZO})
     * @param valore il nuovo valore da impostare per il campo indicato
     * @return true se l'aggiornamento va a buon fine, false altrimenti
     * @throws IllegalArgumentException se l'id non è valido, se il campo o il valore sono
     * {@code null}, oppure se il valore non supera la validazione prevista per il campo
     */
    public boolean aggiornaCampo(int idRistorante, CampoRistorante campo, Object valore) {

        if (idRistorante <= 0) {throw new IllegalArgumentException("ID non valido");}
        if (campo == null) {throw new IllegalArgumentException("Campo non valido");}
        if (valore == null) {throw new IllegalArgumentException("Valore nullo");}

        switch (campo) {
            case NOME -> {
                if (!ValidationUtils.isValidName(valore.toString())) {
                    throw new IllegalArgumentException("Nome non valido");
                }
            }
            case CITTA -> {
                if (!ValidationUtils.isValidCitta(valore.toString())) {
                    throw new IllegalArgumentException("Città non valida");
                }
            }
            case NAZIONE -> {
                if (!ValidationUtils.isValidNazione(valore.toString())) {
                    throw new IllegalArgumentException("Nazione non valida");
                }
            }
            case VIA -> {
                if (!ValidationUtils.isValidVia(valore.toString())) {
                    throw new IllegalArgumentException("Via non valida");
                }
            }
        }
        return ristoranteDAO.aggiornaCampo(idRistorante, campo, valore);
    }

    /**
     * Elimina un ristorante esistente.
     *
     * @param idRistorante id del ristorante da eliminare
     * @return true se l'eliminazione va a buon fine, false altrimenti
     * @throws IllegalArgumentException se l'id non è valido (minore o uguale a 0)
     */
    public boolean cancellaRistorante(int idRistorante) {
        if (idRistorante <= 0) {
            throw new IllegalArgumentException("ID non valido");
        }
        return ristoranteDAO.rimuoviRistorante(idRistorante);
    }

    /**
     * Recupera tutti i ristoranti presenti nel sistema, convertiti in {@link RistoranteDTO}.
     *
     * @return la lista di tutti i ristoranti
     */
    public List<RistoranteDTO> getTuttiRistoranti() {
        return ristoranteDAO.trovaTutti().stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Recupera tutti i ristoranti associati a un ristoratore, a partire dalla sua email.
     *
     * @param email email del ristoratore
     * @return la lista dei ristoranti del ristoratore
     * @throws IllegalArgumentException se l'email non è valida
     */
    public List<RistoranteDTO> getRistorantiDiRistoratore(String email) {
        if (!ValidationUtils.isNotBlank(email)) {
            throw new IllegalArgumentException("Email non valida");
        }
        return ristoranteDAO.trovaPerRistoratore(email).stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Ricerca i ristoranti che soddisfano i criteri specificati nel filtro.
     *
     * @param filtro il {@link FiltroRicercaDTO} con i criteri di ricerca
     * @return la lista dei ristoranti che soddisfano i criteri
     * @throws IllegalArgumentException se {@code filtro} è {@code null}
     */
    public List<RistoranteDTO> cercaRistoranti(FiltroRicercaDTO filtro) {
        if (filtro == null) {
            throw new IllegalArgumentException("Filtro di ricerca nullo");
        }
        return ristoranteDAO.cercaConFiltro(filtro).stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Recupera un singolo ristorante a partire dal suo id.
     *
     * @param idRistorante id del ristorante da cercare
     * @return il {@link RistoranteDTO} corrispondente, oppure {@code null} se non trovato
     * @throws IllegalArgumentException se l'id non è valido (minore o uguale a 0)
     */
    public RistoranteDTO getRistorante(int idRistorante) {
        if (idRistorante <= 0) {
            throw new IllegalArgumentException("ID non valido");
        }
        return getTuttiRistoranti().stream()
                .filter(r -> r.getIdRistorante() == idRistorante)
                .findFirst()
                .orElse(null);
    }

    /**
     * Verifica se esiste già un ristorante con lo stesso nome associato allo stesso
     * ristoratore (email).
     *
     * @param nomeRistorante nome del ristorante da verificare
     * @param email email del ristoratore proprietario
     * @return true se il ristorante esiste, false se non esiste o se i parametri non sono validi
     */
    public boolean esiste(String nomeRistorante, String email) {
        if (!ValidationUtils.isValidName(nomeRistorante) || !ValidationUtils.isNotBlank(email)) {
            return false;
        }
        return ristoranteDAO.esisteRistorante(nomeRistorante, email);
    }
}