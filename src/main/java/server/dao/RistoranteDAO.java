package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.shared.domain.Ristorante;
import main.java.shared.dto.FiltroRicercaDTO;
import main.java.shared.dto.RistoranteDTO;
import main.java.shared.enums.CampoRistorante;

import java.sql.*;
import java.util.*;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * DAO (Data Access Object) per l'accesso ai dati dei ristoranti,
 * memorizzati nella tabella {@code ristorante} del database.
 * Ogni operazione apre e chiude una propria connessione al database
 * (connection-per-operation).
 */
public class RistoranteDAO {

    /**
     * Converte la riga corrente di un {@link ResultSet} in un oggetto {@link Ristorante}.
     *
     * @param rs il ResultSet posizionato sulla riga da convertire
     * @return il {@link Ristorante} corrispondente alla riga corrente
     * @throws SQLException se si verifica un errore durante la lettura del ResultSet
     */
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

    /**
     * Inserisce un nuovo ristorante nel database.
     *
     * @param ristorante l'oggetto {@link Ristorante} da salvare
     * @return true se l'inserimento va a buon fine, false in caso di errore
     */
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

    /**
     * Aggiorna un singolo campo di un ristorante esistente. Il campo da aggiornare
     * viene selezionato dinamicamente tramite {@link CampoRistorante}.
     *
     * @param idRistorante id del ristorante da aggiornare
     * @param campo il campo da aggiornare ({@code NOME}, {@code CITTA}, {@code NAZIONE}, {@code VIA} o {@code FASCIA_PREZZO})
     * @param valore il nuovo valore da impostare per il campo indicato
     * @return true se l'aggiornamento va a buon fine, false in caso di errore
     * @throws IllegalArgumentException se il campo indicato non è valido
     */
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

    /**
     * Elimina un ristorante esistente dal database.
     *
     * @param idRistorante id del ristorante da eliminare
     * @return true se l'eliminazione va a buon fine, false in caso di errore
     */
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

    /**
     * Aggiorna tutti i dati di un ristorante esistente con i valori forniti nel DTO.
     *
     * @param dto il {@link RistoranteDTO} con i nuovi dati del ristorante (deve contenere l'id)
     * @return true se l'aggiornamento va a buon fine, false in caso di errore
     */
    public boolean aggiornaRistorante(RistoranteDTO dto){
        String sql = """
                UPDATE ristorante
                SET nomeRistorante = ?,
                citta = ?,
                nazione = ?,
                via = ?,
                numeroCivico = ?,
                fasciaPrezzo = ?,
                delivery = ?,
                prenotazioneOnline = ?,
                idtipoCucina = ?
                WHERE idristorante = ?
                """;
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, dto.getNomeRistorante());
            statement.setString(2, dto.getCitta());
            statement.setString(3, dto.getNazione());
            statement.setString(4, dto.getVia());
            statement.setInt(5, dto.getNumeroCivico());
            statement.setInt(6, dto.getFasciaPrezzo());
            statement.setBoolean(7, dto.isDelivery());
            statement.setBoolean(8, dto.isPrenotazioneOnline());
            statement.setInt(9, dto.getTipoCucina());
            statement.setInt(10, dto.getIdRistorante());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Errore aggiornamento ristorante: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera tutti i ristoranti presenti nel database, ordinati alfabeticamente per nome.
     *
     * @return la lista di tutti i ristoranti; una lista vuota se non ce ne sono o in caso di errore
     */
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

    /**
     * Recupera tutti i ristoranti associati a un ristoratore, a partire dalla sua email.
     *
     * @param email email del ristoratore
     * @return la lista dei ristoranti del ristoratore; una lista vuota se non ce ne sono o in caso di errore
     */
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

    /**
     * Ricerca i ristoranti che soddisfano i criteri specificati nel filtro, costruendo
     * dinamicamente la query SQL in base ai soli criteri non nulli presenti nel DTO.
     *
     * @param filtro il {@link FiltroRicercaDTO} con i criteri di ricerca da applicare
     * @return la lista dei ristoranti che soddisfano i criteri; una lista vuota se non ce ne
     * sono o in caso di errore
     */
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
            parametri.add("%" + filtro.getNomeRistorante().trim() + "%");
        }

        if (filtro.getCitta() != null) {
            sql.append(" AND citta ILIKE ?");
            parametri.add("%" + filtro.getCitta().trim() + "%");
        }

        if (filtro.getNazione() != null) {
            sql.append(" AND nazione ILIKE ?");
            parametri.add("%" + filtro.getNazione().trim() + "%");
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

    /**
     * Verifica se esiste già un ristorante con lo stesso nome associato allo stesso
     * ristoratore (email).
     *
     * @param nomeRistorante nome del ristorante da verificare
     * @param email email del ristoratore proprietario
     * @return true se il ristorante esiste già, false altrimenti o in caso di errore
     */
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
