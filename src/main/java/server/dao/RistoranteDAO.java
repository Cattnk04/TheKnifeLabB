package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.shared.domain.Ristorante;
import main.java.shared.enums.CampoRistorante;

import java.sql.*;
import java.util.*;

public class RistoranteDAO {

    //Mapping centralizzato
    private Ristorante map(ResultSet rs) throws SQLException {
        return new Ristorante(
                rs.getInt("idristorante"),
                rs.getString("nomeristorante"),
                rs.getString("email"),
                rs.getString("citta"),
                rs.getString("nazione"),
                rs.getString("via"),
                rs.getInt("numerocivico"),
                rs.getInt("fasciaprezzo"),
                rs.getBoolean("delivery"),
                rs.getBoolean("prenotazioneonline"),
                rs.getInt("idtipocucina")
        );
    }

    //Inserimento
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

    public boolean aggiornaCampo(int idRistorante, CampoRistorante campo, Object valore) {

        String sql = switch (campo) {
            case NOME -> "UPDATE ristorante SET nomeristorante = ? WHERE idristorante = ?";
            case CITTA -> "UPDATE ristorante SET citta = ? WHERE idristorante = ?";
            case NAZIONE -> "UPDATE ristorante SET nazione = ? WHERE idristorante = ?";
            case VIA -> "UPDATE ristorante SET via = ? WHERE idristorante = ?";
            case FASCIA_PREZZO -> "UPDATE ristorante SET fasciaprezzo = ? WHERE idristorante = ?";
            default -> throw new IllegalArgumentException("Campo non valido");
        };

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            switch (campo) {
                case FASCIA_PREZZO -> ps.setInt(1, (Integer) valore);
                default -> ps.setString(1, valore.toString());
            }

            ps.setInt(2, idRistorante);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Errore update: " + e.getMessage());
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
    //Ricerca di tutti i ristoranti
    public List<Ristorante> trovaTutti() {

        List<Ristorante> list = new ArrayList<>();
        String sql = "SELECT * FROM ristorante";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore findAll: " + e.getMessage());
        }

        return list;
    }

    //Ricerca (safe version)
    public List<Ristorante> cercaPerCampo(CampoRistorante campo, String valore) {

        List<Ristorante> risultati = new ArrayList<>();

        String colonna = switch (campo) {
            case NOME -> "nomeristorante";
            case CITTA -> "citta";
            case NAZIONE -> "nazione";
            case VIA -> "via";
            case FASCIA_PREZZO -> "fasciaprezzo";
        };

        String sql = "SELECT * FROM ristorante WHERE " + colonna + " LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + valore + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    risultati.add(map(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Errore search: " + e.getMessage());
        }

        return risultati;
    }

    //Controllo esistenza ristorante
    public boolean esisteRistorante(String nomeRistorante) {

        String sql = """
            SELECT 1
            FROM ristorante
            WHERE nomeristorante = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nomeRistorante);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Errore controllo esistenza ristorante: " + e.getMessage());
            return false;
        }
    }
}
