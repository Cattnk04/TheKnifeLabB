package main.java.server.service;

import main.java.server.dao.RistoranteDAO;
import main.java.shared.domain.Ristorante;
import main.java.shared.dto.RistoranteDTO;
import main.java.shared.enums.CampoRistorante;

import java.util.List;

public class RistoranteService {

    private final RistoranteDAO ristoranteDAO;

    public RistoranteService() {
        this.ristoranteDAO = new RistoranteDAO();
    }

    // MAPPING DTO -> ENTITY
    private Ristorante toEntity(RistoranteDTO dto) {
        Ristorante r = new Ristorante();

        r.setNomeRistorante(dto.getNomeRistorante());
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

    // CREATE
    public boolean creaRistorante(RistoranteDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("DTO nullo");
        }

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

        Ristorante entity = toEntity(dto);

        // NOTA: non hai email nel DTO → controllo duplicato su nome
        if (ristoranteDAO.esisteRistorante(entity.getNomeRistorante())) {
            throw new IllegalArgumentException("Ristorante già esistente");
        }

        return ristoranteDAO.inserisciRistorante(entity);
    }

    // UPDATE CAMPO SINGOLO
    public boolean aggiornaCampo(int idRistorante, CampoRistorante campo, Object valore) {

        if (idRistorante <= 0) {
            throw new IllegalArgumentException("ID non valido");
        }

        if (campo == null) {
            throw new IllegalArgumentException("Campo non valido");
        }

        if (valore == null) {
            throw new IllegalArgumentException("Valore nullo");
        }

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

    // DELETE
    public boolean cancellaRistorante(int idRistorante) {
        if (idRistorante <= 0) {
            throw new IllegalArgumentException("ID non valido");
        }
        return ristoranteDAO.rimuoviRistorante(idRistorante);
    }

    // READ ALL
    public List<Ristorante> getTuttiRistoranti() {
        return ristoranteDAO.trovaTutti();
    }

    // SEARCH
    public List<Ristorante> cercaRistoranti(CampoRistorante campo, String valore) {

        if (campo == null) {
            throw new IllegalArgumentException("Campo nullo");
        }

        if (!ValidationUtils.isNotBlank(valore)) {
            throw new IllegalArgumentException("Valore ricerca non valido");
        }

        return ristoranteDAO.cercaPerCampo(campo, valore);
    }

    // EXISTS
    public boolean esiste(String nome) {

        if (!ValidationUtils.isValidName(nome)) {
            return false;
        }

        return ristoranteDAO.esisteRistorante(nome);
    }
}