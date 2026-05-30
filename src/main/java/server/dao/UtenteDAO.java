package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.server.security.PasswordUtils;
import main.java.shared.domain.Utente;

import java.sql.*;
import java.util.*;


public class UtenteDAO {

    //Mapper Utente
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

    //Registrazione dell'Utente
    public boolean registrazione(Utente utente) {

        String sql = """
        INSERT INTO utente (
            email,
            nomeutente,
            cognomeutente,
            hashpwd,
            citta,
            nazione,
            ristoratore
        )
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String hashBCrypt =
                    PasswordUtils.hashBCrypt(
                            utente.getHashpwd()
                    );

            statement.setString(1, utente.getEmail());
            statement.setString(2, utente.getNomeUtente());
            statement.setString(3, utente.getCognomeUtente());
            statement.setString(4, hashBCrypt);
            statement.setString(5, utente.getCitta());
            statement.setString(6, utente.getNazione());
            statement.setBoolean(7, utente.getRistoratore());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante il salvataggio dell'utente: "
                    + e.getMessage());
            return false;
        }
    }

    //Login (spostato in AutoService.java)

    //Ricerca dell'utente
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


    //Cancellazione dell'Utente
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

    //Aggiornamento utente
    public boolean aggiornamentoUtente(String email, String nome, String cognome, String nazione, String citta){
        String sql = """
                UPDATE utente 
                SET nomeutente = ?,
                    cognomeutente = ?,
                    citta = ?,
                    nazione = ?
                WHERE email = ?
                """;
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, nome);
            statement.setString(2, cognome);
            statement.setString(3, citta);
            statement.setString(4, nazione);
            statement.setString(5, email);

            return statement.executeUpdate() > 0;

        } catch (SQLException e){
            System.out.println("Errore durante l'aggiornamento dell'utente");
            return false;
        }
    }

    //Trova tutti gli utenti
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

    //Controllo esistenza utente
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
        }
        return false;
    }

    //Recupero hash password
    public String getPasswordHashByEmail(String email) {
        String sql = """
            SELECT hashpwd
            FROM utente
            WHERE email = ?
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("hashpwd");
                }
            }

        } catch (SQLException e) {
            System.out.println("Errore recupero password: " + e.getMessage());
        }

        return null;
    }

    //Aggiornamento hash password
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
}
