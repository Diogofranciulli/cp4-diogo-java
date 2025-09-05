package model;

public class Game {
    private int id;
    private String titulo;
    private String genero;
    private String plataforma;
    private int anoLancamento;
    private String status;
    private double nota;

    public Game(int id, String titulo, String genero, String plataforma,
                int anoLancamento, String status, double nota) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.plataforma = plataforma;
        this.anoLancamento = anoLancamento;
        this.status = status;
        this.nota = nota;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getPlataforma() { return plataforma; }
    public void setPlataforma(String plataforma) { this.plataforma = plataforma; }
    public int getAnoLancamento() { return anoLancamento; }
    public void setAnoLancamento(int anoLancamento) { this.anoLancamento = anoLancamento; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getNota() { return nota; }
    public void setNota(double nota) { this.nota = nota; }

    @Override
    public String toString() {
        return titulo + " (" + plataforma + ")";
    }
}