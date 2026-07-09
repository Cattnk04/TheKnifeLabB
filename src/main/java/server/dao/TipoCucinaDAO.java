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
 *
 */
public class TipoCucinaDAO {

    /**
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private TipoCucinaDTO map(ResultSet rs) throws SQLException {
        return new TipoCucinaDTO(
                rs.getInt("idtipocucina"),
                rs.getString("tipocucina")
        );
    }

    /**
     *
     * @return
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