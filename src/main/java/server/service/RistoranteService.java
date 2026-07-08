package main.java.server.service;

import main.java.server.dao.RistoranteDAO;
import main.java.shared.domain.Ristorante;
import main.java.shared.enums.CampoRistorante;
import main.java.shared.dto.FiltroRicercaDTO;
import main.java.shared.dto.RistoranteDTO;

import java.util.List;

public class RistoranteService {

    private final RistoranteDAO ristoranteDAO;

    public RistoranteService(RistoranteDAO ristoranteDAO) {
        this.ristoranteDAO = ristoranteDAO;
    }

    // MAPPING DTO -> ENTITY (creazione: idRistorante ignorato, lo assegna il DB)
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

    // MAPPING ENTITY -> DTO (lettura: include l'ID assegnato dal DB)
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

    public boolean aggiornaRistorante(RistoranteDTO dto){
        return ristoranteDAO.aggiornaRistorante(dto);
    }

    // CREATE
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

    // UPDATE CAMPO SINGOLO
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

    // DELETE (per ID, univoco anche con più ristoranti per ristoratore)
    public boolean cancellaRistorante(int idRistorante) {
        if (idRistorante <= 0) {
            throw new IllegalArgumentException("ID non valido");
        }
        return ristoranteDAO.rimuoviRistorante(idRistorante);
    }

    // READ ALL
    public List<RistoranteDTO> getTuttiRistoranti() {
        return ristoranteDAO.trovaTutti().stream()
                .map(this::toDTO)
                .toList();
    }

    // READ per ristoratore (utile per "i miei ristoranti")
    public List<RistoranteDTO> getRistorantiDiRistoratore(String email) {
        if (!ValidationUtils.isNotBlank(email)) {
            throw new IllegalArgumentException("Email non valida");
        }
        return ristoranteDAO.trovaPerRistoratore(email).stream()
                .map(this::toDTO)
                .toList();
    }

    // SEARCH (filtro multi-criterio, usato dalla ricerca guest)
    public List<RistoranteDTO> cercaRistoranti(FiltroRicercaDTO filtro) {
        if (filtro == null) {
            throw new IllegalArgumentException("Filtro di ricerca nullo");
        }
        return ristoranteDAO.cercaConFiltro(filtro).stream()
                .map(this::toDTO)
                .toList();
    }

    // READ per ID singolo (usato per risolvere i preferiti in RistoranteDTO completi)
    public RistoranteDTO getRistorante(int idRistorante) {
        if (idRistorante <= 0) {
            throw new IllegalArgumentException("ID non valido");
        }
        return getTuttiRistoranti().stream()
                .filter(r -> r.getIdRistorante() == idRistorante)
                .findFirst()
                .orElse(null);
    }

    // EXISTS (nome + email, coerente col controllo duplicati in creazione)
    public boolean esiste(String nomeRistorante, String email) {
        if (!ValidationUtils.isValidName(nomeRistorante) || !ValidationUtils.isNotBlank(email)) {
            return false;
        }
        return ristoranteDAO.esisteRistorante(nomeRistorante, email);
    }
}