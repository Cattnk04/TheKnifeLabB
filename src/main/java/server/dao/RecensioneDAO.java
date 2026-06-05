package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.shared.domain.Recensione;
import main.java.shared.dto.RiepilogoRecensioniDTO;
import main.java.shared.enums.CampoRecensione;

import java.sql.*;
import java.util.*;

public class RecensioneDAO {

    //Salvataggio le recensioni
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
    public boolean aggiornaRecensione(int idRistorante, String email, CampoRecensione campo, Object valore) {

        String sql = switch (campo) {
            case RECENSIONE -> "UPDATE recensioni SET recensione = ? WHERE idristorante = ? AND email = ?";
            case VALUTAZIONE -> "UPDATE recensioni SET valutazione = ? WHERE idristorante = ? AND email = ?";
            case RISPOSTA -> "UPDATE recensioni SET risposta = ? WHERE idristorante = ? AND email = ?";
        };

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, valore);
            statement.setInt(2, idRistorante);
            statement.setString(3, email);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento delle recensioni: " + e.getMessage());
            return false;
        }
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
    //Ricerca di tutte le recensioni
    public List<Recensione> getRecensioni() {

        List<Recensione> listaRecensioni = new ArrayList<>();

        String sql = """
            SELECT email, idristorante, valutazione, recensione, risposta
            FROM recensioni
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);

             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                listaRecensioni.add(new Recensione(
                        rs.getInt("idristorante"),
                        rs.getString("email"),
                        rs.getInt("valutazione"),
                        rs.getString("recensione"),
                        rs.getString("risposta")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Errore lettura recensioni: " + e.getMessage());
        }

        return listaRecensioni;
    }

    //Ricerca per ristorante
    public List<Recensione> getRecensioniByRistorante(int idRistorante) {

        List<Recensione> listaByRist = new ArrayList<>();

        String sql = """
            SELECT email, idristorante, valutazione, recensione, risposta
            FROM recensioni
            WHERE idristorante = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idRistorante);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                listaByRist.add(new Recensione(
                        rs.getInt("idristorante"),
                        rs.getString("email"),
                        rs.getInt("Valutazione"),
                        rs.getString("Recensione"),
                        rs.getString("Risposta")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Errore lettura recensioni: " + e.getMessage());
        }

        return listaByRist;
    }

    //Ricerca per email
    public List<Recensione> getRecensioniByEmail(String email) {

        List<Recensione> listaByEmail = new ArrayList<>();

        String sql = """
                SELECT email, idristorante, valutazione, recensione, risposta
                FROM recensioni
                WHERE email = ?
                """;
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

                statement.setString(1, email);
                ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                listaByEmail.add(new Recensione(
                        rs.getInt("idristorante"),
                        rs.getString("Email"),
                        rs.getInt("Valutazione"),
                        rs.getString("Recensione"),
                        rs.getString("Risposta")
                ));
            }

        } catch (SQLException e ){
            System.out.println("Errore lettura recensioni: " + e.getMessage());
        }

        return listaByEmail;
    }

    //Riepilogo recensioni per ristoratore
    public RiepilogoRecensioniDTO getRiepilogo(int idRistorante) {
        String sql = """
                SELECT COUNT(*) AS totRecensioni,
                    COALESCE(AVG(valutazione),0) AS mediaValutazione
                FROM recensioni
                WHERE idristorante = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, idRistorante);
            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                int totale = rs.getInt("totRecensioni");
                double media = rs.getDouble("mediaValutazione");

                return new RiepilogoRecensioniDTO(totale, media);
            }

        } catch (SQLException e) {
            System.out.println("Errore lettura riepilogo recensioni: " + e.getMessage());
        }

        return new RiepilogoRecensioniDTO(0, 0.0);
    }

    //Risposta recensioni
    public boolean rispostaRecensione (int idRistorante, String email, String risposta){

        String sql = """
                UPDATE recensioni
                SET risposta = ?
                WHERE idristorante = ? 
                    AND email = ?
                    AND (risposta IS NULL OR risposta = '')
                """;
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, risposta);
            statement.setInt(2, idRistorante);
            statement.setString(3, email);

            return statement.executeUpdate() > 0;

        } catch(SQLException e){
            System.out.println("Errore risposta recensioni: " + e.getMessage());
            return false;
        }
    }
}
