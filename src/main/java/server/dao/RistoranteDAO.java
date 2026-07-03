package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.shared.domain.Ristorante;
import main.java.shared.dto.FiltroRicercaDTO;
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

    //Aggiornamento campo singolo (per ID, univoco anche con più ristoranti per ristoratore)
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
            System.out.println("Errore durante l'aggiornamento del campo: " + e.getMessage());
            return false;
        }
    }

    //Cancellazione per ID (il nome da solo non è più un identificatore affidabile:
    //un ristoratore può avere più ristoranti, e nomi uguali possono ripetersi tra ristoratori diversi)
    public boolean rimuoviRistorante(int idRistorante) {
        String sql = "DELETE FROM ristorante WHERE idristorante = ?";

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
        String sql = """
                    SELECT idristorante, nomeristorante, email, citta, nazione, via, numeroCivico,
                            fasciaPrezzo, delivery, prenotazioneOnline, idtipocucina
                    FROM ristorante
                    ORDER BY nomeristorante ASC
                    """;

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

    //Tutti i ristoranti di un determinato ristoratore (identificato per email)
    public List<Ristorante> trovaPerRistoratore(String email) {

        List<Ristorante> list = new ArrayList<>();
        String sql = """
                SELECT idristorante, nomeristorante, email, citta, nazione, via, numeroCivico,
                        fasciaPrezzo, delivery, prenotazioneOnline, idtipocucina
                FROM ristorante
                WHERE email = ?""";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Errore trovaPerRistoratore: " + e.getMessage());
        }

        return list;
    }

    //Ricerca con filtro multi-criterio (usata dalla ricerca guest)
    public List<Ristorante> cercaConFiltro(FiltroRicercaDTO filtro) {

        List<Ristorante> risultati = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT idristorante, nomeristorante, email, citta, nazione, via, numeroCivico,
                        fasciaPrezzo, delivery, prenotazioneOnline, idtipocucina
                FROM ristorante
                WHERE 1=1""");
        List<Object> parametri = new ArrayList<>();

        if (filtro.getNomeRistorante() != null) {
            sql.append(" AND nomeristorante ILIKE ?");
            parametri.add("%" + filtro.getNomeRistorante() + "%");
        }

        if (filtro.getCitta() != null) {
            sql.append(" AND citta ILIKE ?");
            parametri.add("%" + filtro.getCitta() + "%");
        }

        if (filtro.getNazione() != null) {
            sql.append(" AND nazione ILIKE ?");
            parametri.add("%" + filtro.getNazione() + "%");
        }

        if (filtro.getFasciaPrezzoMassima() != null) {
            sql.append(" AND fasciaprezzo <= ?");
            parametri.add(filtro.getFasciaPrezzoMassima());
        }

        if (filtro.getDelivery() != null) {
            sql.append(" AND delivery = ?");
            parametri.add(filtro.getDelivery() == 1);
        }

        if (filtro.getPrenotazioneOnline() != null) {
            sql.append(" AND prenotazioneonline = ?");
            parametri.add(filtro.getPrenotazioneOnline() == 1);
        }

        if (filtro.getTipoCucina() != null) {
            sql.append(" AND idtipocucina = ?");
            parametri.add(filtro.getTipoCucina());
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametri.size(); i++) {
                ps.setObject(i + 1, parametri.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    risultati.add(map(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la ricerca con filtro: " + e.getMessage());
        }

        return risultati;
    }

   /*Controllo esistenza ristorante: la combinazione nome + email del ristoratore
   è il criterio di univocità corretto, dato che uno stesso ristoratore
   non dovrebbe poter registrare due ristoranti con lo stesso nome,
   mentre ristoratori diversi possono usare nomi uguali per locali diversi.*/
    public boolean esisteRistorante(String nomeRistorante, String email) {

        String sql = """
            SELECT 1
            FROM ristorante
            WHERE nomeristorante = ? AND email = ?
            LIMIT 1
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nomeRistorante);
            statement.setString(2, email);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Errore controllo esistenza ristorante: " + e.getMessage());
            return false;
        }
    }
}
