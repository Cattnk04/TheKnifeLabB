package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.shared.domain.Preferito;

import java.sql.*;

public class PreferitiDAO {

    //Salvare i preferiti
    public boolean salvaPreferiti (Preferito preferito){
        String sql = """
                INSERT INTO preferiti (email, idristorante)
                VALUES (?, ?)
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, preferito.getEmail());
            statement.setInt(2, preferito.getIdRistorante());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante la salvataggio dei preferiti: " + e.getMessage());
            return false;
        }
    }

    //Cancellazione
    public boolean cancellaPreferiti (String email){
        String sql = """
                DELETE FROM preferiti
                WHERE email = ? AND idristorante = ?
                """;
        try(Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, email);

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
}
