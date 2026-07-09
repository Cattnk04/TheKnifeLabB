package main.java.server.dao;

import main.java.server.db.DatabaseConnection;
import main.java.shared.dto.TipoCucinaDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * DAO (Data Access Object) per l'accesso ai dati dei tipi di cucina
 * memorizzati nella tabella {@code tipicucina} del database.
 * Ogni operazione apre e chiude una propria connessione al database
 * (connection-per-operation).
 */
public class TipoCucinaDAO {

    /**
     * Converte la riga corrente di un {@link ResultSet} in un oggetto {@link TipoCucinaDTO}.
     *
     * @param rs il ResultSet posizionato sulla riga da convertire
     * @return il {@link TipoCucinaDTO} corrispondente alla riga corrente
     * @throws SQLException se si verifica un errore durante la lettura del ResultSet
     */
    private TipoCucinaDTO map(ResultSet rs) throws SQLException {
        return new TipoCucinaDTO(
                rs.getInt("idtipocucina"),
                rs.getString("tipocucina")
        );
    }

    /**
     * Recupera l'elenco di tutti i tipi di cucina presenti nel database.
     *
     * @return la lista dei tipi di cucina; una lista vuota se non ce ne sono
     * o se si verifica un errore durante l'accesso al database
     */
    public List<TipoCucinaDTO> getTipoCucina() {
        List<TipoCucinaDTO> lista = new ArrayList<>();

        String sql = """
                SELECT idtipocucina, tipocucina
                FROM tipicucina
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                lista.add(map(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore recupero tipi di cucina: " + e.getMessage());
        }

        return lista;
    }
}