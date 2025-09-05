package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe responsável pela inicialização e criação das tabelas do banco
 */
public class DatabaseInitializer {
    private static final String URL = "jdbc:sqlite:games.db";

    public static void initialize() {
        createTableIfNotExists();
    }

    private static void createTableIfNotExists() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS games ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "titulo TEXT NOT NULL, "
                + "genero TEXT NOT NULL, "
                + "plataforma TEXT NOT NULL, "
                + "anoLancamento INTEGER NOT NULL, "
                + "status TEXT NOT NULL, "
                + "nota REAL NOT NULL)";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCreate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}