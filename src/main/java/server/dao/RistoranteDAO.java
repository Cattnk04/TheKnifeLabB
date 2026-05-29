package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.shared.domain.Ristorante;

import java.sql.*;
import java.util.*;

public class RistoranteDAO {

    public boolean inserisciRistorante(Ristorante ristorante){
        String sql = """
                INSERT INTO ristorante (
                    nomeristorante, email, citta, nazione, via, numeroCivico, 
                    fasciaPrezzo, delivery, prenotazioneOnline, idtipocucina
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, ristorante.getNomeRistorante());
            statement.setString(2, ristorante.getEmail());
            statement.setString(3, ristorante.getCitta());
            statement.setString(4, ristorante.getNazione());
            statement.setString(5, ristorante.getVia());
            statement.setInt(6, ristorante.getNumeroCivico());
            statement.setInt(7, ristorante.getFasciaPrezzo());
            statement.setBoolean(8, ristorante.getDelivery());
            statement.setBoolean(9, ristorante.getPrenotazioneOnline());
            statement.setInt(10, ristorante.getIdTipoCucina());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento del ristorante: " + e.getMessage());
            return false;
        }
    }
    //Aggiornamento di un campo generico
    public boolean aggiornaCampo(int idRistorante, String campo, String valore) {

        String sql = "UPDATE ristorante SET " + campo + " = ? WHERE idristorante = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, valore);
            statement.setInt(2, idRistorante);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento: " + e.getMessage());
            return false;
        }
    }

    //Cancellazione
    public boolean rimuoviRistorante(int idRistorante) {
        String sql = """
            DELETE FROM ristorante
            WHERE idRistorante = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idRistorante);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'eliminazione del ristorante: " + e.getMessage());
            return false;
        }
    }

    //Aggiungere metodi per la ricerca
}
