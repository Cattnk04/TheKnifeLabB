package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.shared.domain.Recensione;

import java.sql.*;
import java.util.*;

public class RecensioniDAO {

    //Salvare le recensioni
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

    //Aggiornamento
    public boolean aggiornamentoRecensioni(int idRistorante, String email, int valutazione, String recensione, String risposta){
        String sql = """
                UPDATE recensioni 
                SET valutazione = ?, recensione = ?, risposta = ?
                WHERE idristorante = ? AND email = ?
                """;
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, valutazione);
            statement.setString(2, recensione);
            statement.setString(4, risposta);
            statement.setInt(3, idRistorante);
            statement.setString(4, email);

            return statement.executeUpdate() > 0;

        } catch (SQLException e){
            System.out.println("Errore durante l'aggiornamento delle recensioni: " + e.getMessage());
        }
        return false;
    }

    //Cancellazione
    public boolean cancellaRecensioni (int idRistorante, String email){
        String sql = """
                DELETE FROM recensioni
                WHERE idristorante = ? AND email = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

                statement.setInt(1, idRistorante);
                statement.setString(2, email);

                return statement.executeUpdate() == 1;

        } catch (SQLException e){
            System.out.println("Errore durante la cancellazione delle recensioni: " + e.getMessage());
            return false;
        }
    }

    //Ricerca per ristorante
    public List<Recensione> getRecensioniByRistorante(int idRistorante) {

        List<Recensione> lista = new ArrayList<>();

        String sql = """
            SELECT *
            FROM recensioni
            WHERE idRistorante = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idRistorante);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                lista.add(new Recensione(
                        rs.getInt("IDRistorante"),
                        rs.getString("Email"),
                        rs.getInt("Valutazione"),
                        rs.getString("Recensione"),
                        rs.getString("Risposta")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Errore lettura recensioni: " + e.getMessage());
        }

        return lista;
    }
}
