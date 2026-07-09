package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.server.exception.UtenteException;
import main.java.shared.domain.Utente;
import main.java.shared.enums.CampoUtente;

import java.sql.*;
import java.util.*;

/**
 *
 */
public class UtenteDAO {

    /**
     *
      * @param rs
     * @return
     * @throws SQLException
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
     *
      * @param utente
     * @return
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
     *
      * @param email
     * @return
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
     *
      * @param email
     * @param hashpwd
     * @return
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
     *
      * @param email
     * @return
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
     *
      * @param email
     * @return
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
     *
      * @return
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
     *
      * @param email
     * @param campo
     * @param valore
     * @return
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
