package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.server.exception.UtenteException;
import main.java.shared.domain.Utente;
import main.java.shared.enums.CampoUtente;

import java.sql.*;
import java.util.*;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 * DAO (Data Access Object) per l'accesso ai dati degli utenti,
 * memorizzati nella tabella {@code utente} del database.
 * Ogni operazione apre e chiude una propria connessione al database
 * (connection-per-operation).
 */
public class UtenteDAO {

    /**
     * Converte la riga corrente di un {@link ResultSet} in un oggetto {@link Utente}.
     *
     * @param rs il ResultSet posizionato sulla riga da convertire
     * @return l'{@link Utente} corrispondente alla riga corrente
     * @throws SQLException se si verifica un errore durante la lettura del ResultSet
     */
    private Utente map(ResultSet rs) throws SQLException {
        return new Utente(
                rs.getString("email"),
                rs.getString("nomeutente"),
                rs.getString("cognomeutente"),
                rs.getString("hashpwd"),
                rs.getString("citta"),
                rs.getString("nazione"),
                rs.getBoolean("ristoratore")
        );
    }

    /**
     * Inserisce un nuovo utente nel database, dopo aver verificato che i campi
     * obbligatori (email e password) siano valorizzati.
     *
     * @param utente l'oggetto {@link Utente} da registrare
     * @return true se l'inserimento va a buon fine, false se l'operazione non modifica alcuna riga
     * @throws UtenteException se {@code utente} è {@code null}, se l'email o la password non
     * sono valorizzate, oppure se si verifica un errore durante il salvataggio
     */
    public boolean registrazione(Utente utente) {

        if (utente == null) {
            throw new UtenteException("L'utente non può essere null.");
        }

        if (utente.getEmail() == null || utente.getEmail().isBlank()) {
            throw new UtenteException("L'email dell'utente è obbligatoria.");
        }

        if (utente.getHashpwd() == null || utente.getHashpwd().isBlank()) {
            throw new UtenteException("La password dell'utente è obbligatoria.");
        }

        String sql = """
            INSERT INTO utente (
                email, nomeutente, cognomeutente, hashpwd,
                citta, nazione, ristoratore
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, utente.getEmail());
            statement.setString(2, utente.getNomeUtente());
            statement.setString(3, utente.getCognomeUtente());
            statement.setString(4, utente.getHashpwd());
            statement.setString(5, utente.getCitta());
            statement.setString(6, utente.getNazione());
            statement.setBoolean(7, utente.getRistoratore());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new UtenteException("Errore durante il salvataggio dell'utente.", e);
        }
    }

    /**
     * Cerca un utente nel database a partire dalla sua email.
     *
     * @param email email dell'utente da cercare
     * @return un {@link Optional} contenente l'{@link Utente} trovato, oppure {@link Optional#empty()}
     * se non esiste o si verifica un errore
     */
    public Optional<Utente> trovaUtente(String email) {
        String sql = """
            SELECT email, nomeutente, cognomeutente, hashpwd, nazione, citta, ristoratore
            FROM utente
            WHERE email = ?
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Errore ricerca utente: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Aggiorna l'hash della password di un utente, identificato dalla sua email.
     * Utilizzato anche per la migrazione degli hash legacy a BCrypt.
     *
     * @param email email dell'utente di cui aggiornare la password
     * @param hashpwd il nuovo hash della password
     * @return true se l'aggiornamento va a buon fine, false in caso di errore
     */
    public boolean aggiornaPasswordHash(String email, String hashpwd) {
        String sql = """
            UPDATE utente
            SET hashpwd = ?
            WHERE email = ?
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hashpwd);
            ps.setString(2, email);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Errore aggiornamento password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un utente esistente dal database, a partire dalla sua email.
     *
     * @param email email dell'utente da eliminare
     * @return true se la cancellazione va a buon fine, false in caso di errore
     */
    public boolean cancellaUtente (String email){
        String sql = """
                DELETE FROM utente
                WHERE email = ?
                """;
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, email);

            return statement.executeUpdate() > 0;
        }
        catch (SQLException e){
            System.out.println("Errore durante la cancellazione dell'utente");
            return false;
        }
    }

    /**
     * Verifica se esiste già un utente registrato con la data email.
     *
     * @param email email da verificare
     * @return true se l'utente esiste, false altrimenti o in caso di errore
     */
    public boolean esisteUtente(String email) {
        String sql = """
                SELECT 1
                FROM utente
                WHERE email = ?
                LIMIT 1
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Errore controllo utente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera tutti gli utenti presenti nel database.
     *
     * @return la lista di tutti gli utenti; una lista vuota se non ce ne sono o in caso di errore
     */
    public List<Utente> trovaTutti() {

        List<Utente> lista = new ArrayList<>();

        String sql = """
                    SELECT email, nomeutente, cognomeutente, hashpwd, nazione, citta, ristoratore 
                    FROM utente
                    """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                lista.add(map(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore lista utenti: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Aggiorna un singolo campo di un utente esistente, identificato dalla sua email.
     * Il campo da aggiornare viene selezionato dinamicamente tramite {@link CampoUtente}.
     *
     * @param email email dell'utente da aggiornare
     * @param campo il campo da aggiornare ({@code NOMEUTENTE}, {@code COGNOMEUTENTE}, {@code CITTA},
     * {@code NAZIONE}, {@code PASSWORD} o {@code RISTORATORE})
     * @param valore il nuovo valore da impostare per il campo indicato
     * @return true se l'aggiornamento va a buon fine, false in caso di errore
     * @throws IllegalArgumentException se il campo indicato non è valido
     */
    public boolean aggiornamentoUtente(String email, CampoUtente campo, Object valore){
        String sql = switch (campo){
            case NOMEUTENTE -> "UPDATE utente SET nomeutente = ? WHERE email = ?";
            case COGNOMEUTENTE -> "UPDATE utente SET cognomeutente = ? WHERE email = ?";
            case CITTA -> "UPDATE utente SET citta = ? WHERE email = ?";
            case NAZIONE -> "UPDATE  utente SET nazione = ? WHERE email = ?";
            case PASSWORD -> "UPDATE utente SET hashpwd = ? where email = ?";
            case RISTORATORE -> "UPDATE utente SET ristoratore = ? where email = ?";
            default -> throw new IllegalArgumentException("Campo non valido");

        };

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setObject(1, valore);
            statement.setString(2, email);

            return statement.executeUpdate() > 0;

        } catch (SQLException e){
            System.err.println("Errore durante l'aggiornamento dell'utente: " + e.getMessage());
            return false;
        }
    }

}
