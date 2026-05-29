package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.shared.domain.Utente;

import java.sql.*;


public class UtenteDAO {

    //Registrazione
    public boolean registrazione(Utente utente){
        String sql = """
                INSERT INTO utente (email, nomeutente, cognomeutente, hashpwd, nazione, citta, ristoratore)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, utente.getEmail());
            statement.setString(2, utente.getNomeUtente());
            statement.setString(3, utente.getCognomeUtente());
            statement.setString(4, utente.getHashpwd());
            statement.setString(5, utente.getNazione());
            statement.setString(6, utente.getCitta());
            statement.setBoolean(7, utente.getRistoratore());

            return statement.executeUpdate() == 1;
        }
        catch (SQLException e) {
            System.out.println("Errore durante il salvataggio dell'utente: " + e.getMessage());
            return false;
        }
    }

    //Login
    public boolean login(Utente utente){
        String sql = """
                SELECT email, hashpwd
                FROM utente 
                WHERE email = ? AND hashpwd = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, utente.getEmail());
            statement.setString(2, utente.getHashpwd());

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
        catch (SQLException e) {
            System.out.println("Errore durante il login dell'utente: " + e.getMessage());
        }

    return false;
    }

    public Utente trovaUtente(String email){
        String sql = """
                SELECT email 
                FROM utente 
                WHERE email = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()){
                    return new Utente(
                            resultSet.getString("email"),
                            resultSet.getString("nome"),
                            resultSet.getString("cognome"),
                            resultSet.getString("password"),
                            resultSet.getString("nazione"),
                            resultSet.getString("citta"),
                            resultSet.getBoolean("ristoratore")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Errore durante la ricerca dell'utente: " + e.getMessage());
        }
        return null;
    }
}
