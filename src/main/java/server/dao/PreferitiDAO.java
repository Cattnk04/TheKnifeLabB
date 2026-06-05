package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.shared.domain.Preferito;

import java.sql.*;
import java.util.*;

public class PreferitiDAO {

    //Salvataggio dei preferiti
    public boolean salvaPreferiti (String email, int idRistorante){
        String sql = """
                INSERT INTO preferiti (email, idristorante)
                VALUES (?, ?)
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, email);
            statement.setInt(2, idRistorante);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante la salvataggio dei preferiti: " + e.getMessage());
            return false;
        }
    }

    //Cancellazione
    public boolean cancellaPreferiti (String email, int idRistorante){
        String sql = """
                DELETE FROM preferiti
                WHERE email = ? AND idristorante = ?
                """;
        try(Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, email);
            statement.setInt(2, idRistorante);

            return statement.executeUpdate() > 0;

        } catch (SQLException e){
            System.out.println("Errore durante la cancellazione dei preferiti: " + e.getMessage());
            return false;
        }
    }

    //Controllo esistenza preferito
    public boolean esistePreferito(String email, int idRistorante) {

        String sql = """
            SELECT 1
            FROM preferiti
            WHERE email = ? AND idristorante = ?
            LIMIT 1
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setInt(2, idRistorante);

            return statement.executeQuery().next();

        } catch (SQLException e) {
            System.out.println("Errore controllo preferito: " + e.getMessage());
            return false;
        }
    }

    //Ricerca preferiti per email
    public List<Preferito> getPreferitiByEmail(String email) {

        String sql = """
                SELECT r.*
                FROM ristorante r
                JOIN preferiti p
                    ON r.idristorante = p.idristorante
                WHERE p.email = ?
                ORDER BY r.nomeristorante
                """;

        List<Preferito> list = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    Preferito p = new Preferito();
                    p.setEmail(rs.getString("email"));
                    p.setIdRistorante(rs.getInt("idristorante"));
                    list.add(p);
                }
            }

        } catch (SQLException e) {
            System.out.println("Errore recupero preferiti: " + e.getMessage());
        }

        return list;
    }
}
