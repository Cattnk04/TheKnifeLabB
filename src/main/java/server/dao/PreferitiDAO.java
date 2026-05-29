package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.shared.domain.Preferito;

import javax.xml.crypto.Data;
import java.sql.*;

public class PreferitiDAO {

    public boolean salvaPreferiti (Preferito preferito){
        String sql = """
                INSERT INTO preferiti (email, idristorante)
                VALUES (?, ?)
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, preferito.getEmail());
            statement.setString(2, preferito.getIdRistorante());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Errore durante la salvataggio dei preferiti: " + e.getMessage());
            return false;
        }
    }
}
