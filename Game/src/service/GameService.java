package service;

import dao.GameDAO;
import model.Game;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class GameService {
    private GameDAO dao = new GameDAO();

    public void cadastrar(Game game) throws IllegalArgumentException, SQLException {
        validarGame(game, 0);
        dao.create(game);
    }

    public void atualizar(Game game) throws IllegalArgumentException, SQLException {
        validarGame(game, game.getId());
        dao.update(game);
    }

    public void remover(int id) throws SQLException {
        dao.delete(id);
    }

    public List<Game> listar() throws SQLException {
        return dao.readAll();
    }

    public List<Game> listarComFiltros(String genero, String plataforma, String status) throws SQLException {
        return dao.readWithFilters(genero, plataforma, status);
    }

    public Map<String, Integer> getEstatisticasPorGenero() throws SQLException {
        return dao.getStatsByGenero();
    }

    public Map<String, Integer> getEstatisticasPorPlataforma() throws SQLException {
        return dao.getStatsByPlataforma();
    }

    public List<String> getGenerosDisponiveis() throws SQLException {
        return dao.getDistinctGeneros();
    }

    public List<String> getPlataformasDisponiveis() throws SQLException {
        return dao.getDistinctPlataformas();
    }

    private void validarGame(Game game, int excludeId) throws IllegalArgumentException, SQLException {
        if (game.getTitulo() == null || game.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("Título é obrigatório!");
        }
        if (game.getGenero() == null || game.getGenero().trim().isEmpty()) {
            throw new IllegalArgumentException("Gênero é obrigatório!");
        }
        if (game.getPlataforma() == null || game.getPlataforma().trim().isEmpty()) {
            throw new IllegalArgumentException("Plataforma é obrigatória!");
        }
        if (game.getAnoLancamento() < 1970 || game.getAnoLancamento() > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("Ano de lançamento inválido! (1970 - " + java.time.Year.now().getValue() + ")");
        }
        if (game.getNota() < 0 || game.getNota() > 10) {
            throw new IllegalArgumentException("Nota deve ser entre 0 e 10!");
        }
        if (dao.existsGame(game.getTitulo(), game.getPlataforma(), excludeId)) {
            throw new IllegalArgumentException("Já existe um jogo com este título e plataforma!");
        }
    }
}