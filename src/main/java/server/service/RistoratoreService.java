package main.java.server.service;

import main.java.server.dao.*;
import main.java.shared.domain.*;
import main.java.shared.enums.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Service che raggruppa le operazioni disponibili per un utente con ruolo
 * ristoratore: gestione del proprio account, gestione dei propri ristoranti
 * e consultazione delle recensioni ricevute. Delega l'accesso ai dati a
 * {@link UtenteDAO}, {@link RistoranteDAO} e {@link RecensioneDAO}.
 */
public class RistoratoreService {

    private final UtenteDAO utenteDAO;
    private final RistoranteDAO ristoranteDAO;
    private final RecensioneDAO recensioneDAO;

    /**
     * Costruisce un nuovo service per la gestione delle operazioni del ristoratore.
     *
     * @param ristoranteDAO il DAO da utilizzare per l'accesso ai dati dei ristoranti
     * @param utenteDAO il DAO da utilizzare per l'accesso ai dati dell'utente/ristoratore
     * @param recensioneDAO il DAO da utilizzare per l'accesso ai dati delle recensioni
     */
    public RistoratoreService(RistoranteDAO ristoranteDAO, UtenteDAO utenteDAO, RecensioneDAO recensioneDAO) {
        this.ristoranteDAO = ristoranteDAO;
        this.utenteDAO = utenteDAO;
        this.recensioneDAO = recensioneDAO;
    }

    /**
     * Recupera i dati dell'account di un ristoratore a partire dalla sua email.
     *
     * @param email email del ristoratore
     * @return un {@link Optional} contenente l'{@link Utente} trovato, oppure {@link Optional#empty()}
     * se non esiste
     */
    public Optional<Utente> getAccountRistoratore(String email) {
        return utenteDAO.trovaUtente(email);
    }

    /**
     * Aggiorna un singolo campo del profilo di un ristoratore.
     *
     * @param email email del ristoratore da aggiornare
     * @param campo il campo del profilo da aggiornare
     * @param valore il nuovo valore da impostare
     * @return true se l'aggiornamento va a buon fine, false altrimenti
     */
    public boolean aggiornaProfilo(String email, CampoUtente campo, Object valore) {
        return utenteDAO.aggiornamentoUtente(email, campo, valore);
    }

    /**
     * Elimina l'account di un ristoratore.
     *
     * @param email email del ristoratore il cui account deve essere eliminato
     * @return true se l'eliminazione va a buon fine, false altrimenti
     */
    public boolean eliminaAccount(String email) {
        return utenteDAO.cancellaUtente(email);
    }

    /**
     * Verifica se esiste un utente registrato con la data email.
     *
     * @param email email da verificare
     * @return true se l'utente esiste, false altrimenti
     */
    public boolean esisteUtente(String email) {
        return utenteDAO.esisteUtente(email);
    }

    /**
     * Recupera tutti i ristoranti presenti nel sistema.
     *
     * @return la lista di tutti i ristoranti
     */
    public List<Ristorante> getTuttiRistoranti() {
        return ristoranteDAO.trovaTutti();
    }

    /**
     * Inserisce un nuovo ristorante appartenente al ristoratore.
     *
     * @param ristorante l'entità {@link Ristorante} da inserire
     * @return true se l'inserimento va a buon fine, false altrimenti
     */
    public boolean inserisciRistorante(Ristorante ristorante) {
        return ristoranteDAO.inserisciRistorante(ristorante);
    }

    /**
     * Aggiorna un singolo campo di un ristorante appartenente al ristoratore.
     *
     * @param idRistorante id del ristorante da aggiornare
     * @param campo il campo da aggiornare
     * @param valore il nuovo valore da impostare
     * @return true se l'aggiornamento va a buon fine, false altrimenti
     */
    public boolean aggiornaCampoRistorante(int idRistorante, CampoRistorante campo, Object valore) {
        return ristoranteDAO.aggiornaCampo(idRistorante, campo, valore);
    }

    /**
     * Elimina un ristorante appartenente al ristoratore.
     *
     * @param idRistorante id del ristorante da eliminare
     * @return true se l'eliminazione va a buon fine, false altrimenti
     */
    public boolean eliminaRistorante(int idRistorante) {
        return ristoranteDAO.rimuoviRistorante(idRistorante);
    }

    /**
     * Recupera tutte le recensioni ricevute da un ristorante del ristoratore.
     *
     * @param idRistorante id del ristorante di cui recuperare le recensioni
     * @return la lista delle recensioni del ristorante
     */
    public List<?> getRecensioniRistorante(int idRistorante) {
        return recensioneDAO.getRecensioniByRistorante(idRistorante);
    }

    /**
     * Calcola la media delle valutazioni ricevute da un ristorante.
     *
     * @param idRistorante id del ristorante di cui calcolare la media
     * @return la media delle valutazioni, oppure 0.0 se il ristorante non ha recensioni
     */
    public double mediaVotoRistorante(int idRistorante) {
        List<?> recensioni = recensioneDAO.getRecensioniByRistorante(idRistorante);

        if (recensioni.isEmpty()) return 0.0;

        return recensioni.stream()
                .mapToInt(r -> ((main.java.shared.domain.Recensione) r).getValutazione())
                .average()
                .orElse(0.0);
    }

    /**
     * Conta il numero di recensioni ricevute da un ristorante.
     *
     * @param idRistorante id del ristorante di cui contare le recensioni
     * @return il numero di recensioni del ristorante
     */
    public int numeroRecensioni(int idRistorante) {
        return recensioneDAO.getRecensioniByRistorante(idRistorante).size();
    }
}
