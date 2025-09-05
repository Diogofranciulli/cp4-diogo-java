package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável APENAS pela conexão com o banco de dados
 */
public class Conexao {
    private static final String URL = "jdbc:sqlite:games.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
