package dao;

import model.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameDAO {

    public void create(Game game) throws SQLException {
        String sql = "INSERT INTO games (titulo, genero, plataforma, anoLancamento, status, nota) VALUES (?,?,?,?,?,?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, game.getTitulo());
            stmt.setString(2, game.getGenero());
            stmt.setString(3, game.getPlataforma());
            stmt.setInt(4, game.getAnoLancamento());
            stmt.setString(5, game.getStatus());
            stmt.setDouble(6, game.getNota());
            stmt.executeUpdate();
        }
    }

    public List<Game> readAll() throws SQLException {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM games ORDER BY titulo";
        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                games.add(new Game(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("genero"),
                        rs.getString("plataforma"),
                        rs.getInt("anoLancamento"),
                        rs.getString("status"),
                        rs.getDouble("nota")
                ));
            }
        }
        return games;
    }

    public List<Game> readWithFilters(String genero, String plataforma, String status) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM games WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (genero != null && !genero.trim().isEmpty() && !genero.equals("Todos")) {
            sql.append(" AND genero = ?");
            params.add(genero);
        }
        if (plataforma != null && !plataforma.trim().isEmpty() && !plataforma.equals("Todas")) {
            sql.append(" AND plataforma = ?");
            params.add(plataforma);
        }
        if (status != null && !status.trim().isEmpty() && !status.equals("Todos")) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        sql.append(" ORDER BY titulo");

        List<Game> games = new ArrayList<>();
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    games.add(new Game(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("genero"),
                            rs.getString("plataforma"),
                            rs.getInt("anoLancamento"),
                            rs.getString("status"),
                            rs.getDouble("nota")
                    ));
                }
            }
        }
        return games;
    }

    public void update(Game game) throws SQLException {
        String sql = "UPDATE games SET titulo=?, genero=?, plataforma=?, anoLancamento=?, status=?, nota=? WHERE id=?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, game.getTitulo());
            stmt.setString(2, game.getGenero());
            stmt.setString(3, game.getPlataforma());
            stmt.setInt(4, game.getAnoLancamento());
            stmt.setString(5, game.getStatus());
            stmt.setDouble(6, game.getNota());
            stmt.setInt(7, game.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM games WHERE id=?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public boolean existsGame(String titulo, String plataforma, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM games WHERE titulo = ? AND plataforma = ? AND id != ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titulo);
            stmt.setString(2, plataforma);
            stmt.setInt(3, excludeId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public Map<String, Integer> getStatsByGenero() throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT genero, COUNT(*) as total FROM games GROUP BY genero ORDER BY total DESC";
        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stats.put(rs.getString("genero"), rs.getInt("total"));
            }
        }
        return stats;
    }

    public Map<String, Integer> getStatsByPlataforma() throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT plataforma, COUNT(*) as total FROM games GROUP BY plataforma ORDER BY total DESC";
        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stats.put(rs.getString("plataforma"), rs.getInt("total"));
            }
        }
        return stats;
    }

    public List<String> getDistinctGeneros() throws SQLException {
        List<String> generos = new ArrayList<>();
        String sql = "SELECT DISTINCT genero FROM games ORDER BY genero";
        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                generos.add(rs.getString("genero"));
            }
        }
        return generos;
    }

    public List<String> getDistinctPlataformas() throws SQLException {
        List<String> plataformas = new ArrayList<>();
        String sql = "SELECT DISTINCT plataforma FROM games ORDER BY plataforma";
        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                plataformas.add(rs.getString("plataforma"));
            }
        }
        return plataformas;
    }
}
