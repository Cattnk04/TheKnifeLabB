package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.shared.domain.Recensione;

import java.sql.*;

public class RecensioniDAO {

    public boolean salvaRecensioni (Recensione recensione){
        String sql = """
                INSERT INTO recensioni (email, idristorante, valutazione, recensione, risposta)
                Values (?, ?, ?, ?, ?)
                """;
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, recensione.getEmail());
            statement.setInt(2, recensione.getIdRistorante());
            statement.setInt(3, recensione.getValutazione());
            statement.setString(4, recensione.getRecensione());
            statement.setString(5, recensione.getRisposta());

            return statement.executeUpdate() == 1;

        }catch (SQLException e){
            System.out.println("Errore durante la salvataggio delle recensioni: " + e.getMessage());
            return false;
        }
    }
    //Aggiungere metodi per la ricerca, aggiornamento e cancellazione delle recensioni
}
