package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.shared.domain.Preferito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PreferitiDAO {

    /**
     *
     * @param email
     * @param idRistorante
     * @return
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
     *
      * @param email
     * @param idRistorante
     * @return
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
     *
      * @param email
     * @param idRistorante
     * @return
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
     *
      * @param email
     * @return
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