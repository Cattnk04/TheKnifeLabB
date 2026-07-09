package main.java.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Classe di utilità per la gestione della connessione al database PostgreSQL.
 * <p>
 * I parametri di connessione (URL, utente, password) vengono letti dalle
 * variabili d'ambiente, con valori di default utilizzati per l'ambiente
 * di sviluppo locale nel caso in cui non siano definite.
 * </p>
 */
public class DatabaseConnection {

    /**
     * URL JDBC del database, letto dalla variabile d'ambiente {@code DB_URL}
     * oppure impostato di default su {@code jdbc:postgresql://localhost:5432/postgres}.
     */
    private static final String URL = System.getenv().getOrDefault(
            "DB_URL",
            "jdbc:postgresql://localhost:5432/postgres"
    );

    /**
     * Nome utente per la connessione al database, letto dalla variabile
     * d'ambiente {@code DB_USER} oppure impostato di default su {@code postgres}.
     */
    private static final String USER = System.getenv().getOrDefault(
            "DB_USER",
            "postgres"
    );

    /**
     * Password per la connessione al database, letta dalla variabile
     * d'ambiente {@code DB_PASSWORD} oppure impostata di default su {@code postgres}.
     */
    private static final String PASSWORD = System.getenv().getOrDefault(
            "DB_PASSWORD",
            "postgres"
    );

    /**
     * Costruttore privato: la classe espone solo membri statici e non è
     * pensata per essere istanziata.
     */
    private DatabaseConnection() {
    }

    /**
     * Apre e restituisce una nuova connessione al database, utilizzando
     * i parametri configurati (URL, utente, password).
     *
     * @return una nuova {@link Connection} verso il database
     * @throws SQLException se la connessione al database fallisce
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}