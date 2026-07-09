package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.shared.domain.Preferito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * DAO (Data Access Object) per l'accesso ai dati dei preferiti degli utenti,
 * memorizzati nella tabella {@code preferiti} del database.
 * Ogni operazione apre e chiude una propria connessione al database
 * (connection-per-operation).
 */
public class PreferitiDAO {

    /**
     * Inserisce un nuovo preferito nel database, associando un utente a un ristorante.
     *
     * @param email email dell'utente che aggiunge il preferito
     * @param idRistorante id del ristorante da aggiungere ai preferiti
     * @return true se l'inserimento va a buon fine, false in caso di errore
     */
    public boolean salvaPreferiti(String email, int idRistorante) {

        String sql = """
            INSERT INTO preferiti (email, idristorante)
            VALUES (?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setInt(2, idRistorante);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Errore inserimento preferito: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un preferito esistente dal database.
     *
     * @param email email dell'utente proprietario del preferito
     * @param idRistorante id del ristorante da rimuovere dai preferiti
     * @return true se la cancellazione va a buon fine, false in caso di errore
     */
    public boolean cancellaPreferiti(String email, int idRistorante) {

        String sql = """
            DELETE FROM preferiti
            WHERE email = ? AND idristorante = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setInt(2, idRistorante);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Errore cancellazione preferito: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica se un dato ristorante è già presente tra i preferiti di un utente.
     *
     * @param email email dell'utente
     * @param idRistorante id del ristorante da verificare
     * @return true se il preferito esiste, false altrimenti o in caso di errore
     */
    public boolean esistePreferito(String email, int idRistorante) {

        String sql = """
            SELECT 1
            FROM preferiti
            WHERE email = ? AND idristorante = ?
            LIMIT 1
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setInt(2, idRistorante);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Errore check preferito: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera tutti i preferiti associati a un utente, a partire dalla sua email.
     *
     * @param email email dell'utente di cui recuperare i preferiti
     * @return la lista dei preferiti dell'utente; una lista vuota se non ce ne sono o in caso di errore
     */
    public List<Preferito> getPreferitiByEmail(String email) {

        String sql = """
            SELECT email, idristorante
            FROM preferiti
            WHERE email = ?
        """;

        List<Preferito> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(new Preferito(
                            rs.getString("email"),
                            rs.getInt("idristorante")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Errore recupero preferiti: " + e.getMessage());
        }

        return list;
    }
}