package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.shared.domain.Recensione;
import main.java.shared.dto.RiepilogoRecensioniDTO;
import main.java.shared.enums.CampoRecensione;

import java.sql.*;
import java.util.*;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * DAO (Data Access Object) per l'accesso ai dati delle recensioni,
 * memorizzate nella tabella {@code recensioni} del database.
 * Ogni operazione apre e chiude una propria connessione al database
 * (connection-per-operation).
 */
public class RecensioneDAO {

    /**
     * Inserisce una nuova recensione nel database.
     *
     * @param recensione l'oggetto {@link Recensione} da salvare
     * @return true se l'inserimento va a buon fine, false in caso di errore
     */
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

            return statement.executeUpdate() > 0;

        }catch (SQLException e){
            System.out.println("Errore durante la salvataggio delle recensioni: " + e.getMessage());
            return false;
        }
    }

    /**
     * Aggiorna un singolo campo di una recensione esistente, identificata da ristorante ed
     * email dell'autore. Il campo da aggiornare viene selezionato dinamicamente tramite
     * {@link CampoRecensione}.
     *
     * @param idRistorante id del ristorante recensito
     * @param email email dell'autore della recensione
     * @param campo il campo della recensione da aggiornare ({@code RECENSIONE}, {@code VALUTAZIONE} o {@code RISPOSTA})
     * @param valore il nuovo valore da impostare per il campo indicato
     * @return true se l'aggiornamento va a buon fine, false in caso di errore
     */
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

    /**
     * Elimina una recensione esistente, identificata da ristorante ed email dell'autore.
     *
     * @param idRistorante id del ristorante recensito
     * @param email email dell'autore della recensione da eliminare
     * @return true se la cancellazione va a buon fine, false in caso di errore
     */
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

    /**
     * Recupera tutte le recensioni presenti nel database.
     *
     * @return la lista di tutte le recensioni; una lista vuota se non ce ne sono o in caso di errore
     */
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

    /**
     * Recupera tutte le recensioni relative a un determinato ristorante, ordinate per
     * email in ordine decrescente.
     *
     * @param idRistorante id del ristorante di cui recuperare le recensioni
     * @return la lista delle recensioni del ristorante; una lista vuota se non ce ne sono o in caso di errore
     */
    public List<Recensione> getRecensioniByRistorante(int idRistorante) {

        List<Recensione> listaByRist = new ArrayList<>();

        String sql = """
            SELECT email, idristorante, valutazione, recensione, risposta
            FROM recensioni
            WHERE idristorante = ?
            ORDER BY email DESC
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idRistorante);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    listaByRist.add(new Recensione(
                            rs.getInt("idristorante"),
                            rs.getString("email"),
                            rs.getInt("valutazione"),
                            rs.getString("recensione"),
                            rs.getString("risposta")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Errore lettura recensioni: " + e.getMessage());
        }

        return listaByRist;
    }

    /**
     * Recupera tutte le recensioni scritte da un determinato utente.
     *
     * @param email email dell'utente autore delle recensioni
     * @return la lista delle recensioni scritte dall'utente; una lista vuota se non ce ne sono o in caso di errore
     */
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

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    listaByEmail.add(new Recensione(
                            rs.getInt("idristorante"),
                            rs.getString("email"),
                            rs.getInt("valutazione"),
                            rs.getString("recensione"),
                            rs.getString("risposta")
                    ));
                }
            }

        } catch (SQLException e ){
            System.out.println("Errore lettura recensioni: " + e.getMessage());
        }

        return listaByEmail;
    }

    /**
     * Calcola il riepilogo delle recensioni di un ristorante, ovvero il numero totale
     * di recensioni e la media delle valutazioni.
     *
     * @param idRistorante id del ristorante di cui calcolare il riepilogo
     * @return il {@link RiepilogoRecensioniDTO} calcolato; se il ristorante non ha recensioni o si
     * verifica un errore, viene restituito un riepilogo con 0 recensioni e media 0.0
     */
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
            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new RiepilogoRecensioniDTO(
                            rs.getInt("tot"),
                            rs.getDouble("media")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Errore lettura riepilogo recensioni: " + e.getMessage());
        }

        return new RiepilogoRecensioniDTO(0, 0.0);
    }

    /**
     * Inserisce la risposta del ristoratore a una recensione, ma solo se questa non ha
     * già una risposta (il campo {@code risposta} deve essere nullo o vuoto).
     *
     * @param idRistorante id del ristorante recensito
     * @param email email dell'autore della recensione
     * @param risposta testo della risposta da inserire
     * @return true se la risposta viene inserita correttamente, false se la recensione ha già
     * una risposta o in caso di errore
     */
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
