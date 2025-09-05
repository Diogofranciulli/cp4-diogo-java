package view;

import db.DatabaseInitializer;
import model.Game;
import service.GameService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class GameManager extends JFrame {
    private GameService service = new GameService();
    private JTable tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;

    // Componentes de filtro
    private JComboBox<String> comboGenero;
    private JComboBox<String> comboPlataforma;
    private JComboBox<String> comboStatus;

    public GameManager() {
        initComponents();
        DatabaseInitializer.initialize();
        carregarTabela();
        atualizarFiltros();
    }

    private void initComponents() {
        setTitle("Gerenciador de Jogos - Sistema Completo");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu
        criarMenu();

        // Panel principal
        setLayout(new BorderLayout());

        // Panel de filtros
        criarPanelFiltros();

        // Tabela
        criarTabela();

        // Panel de botões
        criarPanelBotoes();
    }

    private void criarMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuJogos = new JMenu("Jogos");
        JMenuItem cadastrar = new JMenuItem("Cadastrar Jogo");
        JMenuItem listar = new JMenuItem("Listar Todos");
        JMenuItem atualizar = new JMenuItem("Atualizar Jogo");
        JMenuItem remover = new JMenuItem("Remover Jogo");

        cadastrar.addActionListener(e -> cadastrarJogo());
        listar.addActionListener(e -> carregarTabela());
        atualizar.addActionListener(e -> atualizarJogo());
        remover.addActionListener(e -> removerJogo());

        menuJogos.add(cadastrar);
        menuJogos.add(listar);
        menuJogos.addSeparator();
        menuJogos.add(atualizar);
        menuJogos.add(remover);

        JMenu menuRelatorios = new JMenu("Relatórios");
        JMenuItem relGenero = new JMenuItem("Por Gênero");
        JMenuItem relPlataforma = new JMenuItem("Por Plataforma");

        relGenero.addActionListener(e -> mostrarRelatorioGenero());
        relPlataforma.addActionListener(e -> mostrarRelatorioPlataforma());

        menuRelatorios.add(relGenero);
        menuRelatorios.add(relPlataforma);

        menuBar.add(menuJogos);
        menuBar.add(menuRelatorios);
        setJMenuBar(menuBar);
    }

    private void criarPanelFiltros() {
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        panelFiltros.add(new JLabel("Gênero:"));
        comboGenero = new JComboBox<>();
        comboGenero.addActionListener(e -> aplicarFiltros());
        panelFiltros.add(comboGenero);

        panelFiltros.add(new JLabel("Plataforma:"));
        comboPlataforma = new JComboBox<>();
        comboPlataforma.addActionListener(e -> aplicarFiltros());
        panelFiltros.add(comboPlataforma);

        panelFiltros.add(new JLabel("Status:"));
        comboStatus = new JComboBox<>(new String[]{"Todos", "Jogando", "Concluído", "Wishlist", "Abandonado"});
        comboStatus.addActionListener(e -> aplicarFiltros());
        panelFiltros.add(comboStatus);

        JButton btnLimparFiltros = new JButton("Limpar Filtros");
        btnLimparFiltros.addActionListener(e -> limparFiltros());
        panelFiltros.add(btnLimparFiltros);

        add(panelFiltros, BorderLayout.NORTH);
    }

    private void criarTabela() {
        modelo = new DefaultTableModel(new String[]{"ID", "Título", "Gênero", "Plataforma", "Ano", "Status", "Nota"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar largura das colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tabela.getColumnModel().getColumn(1).setPreferredWidth(200); // Título
        tabela.getColumnModel().getColumn(2).setPreferredWidth(100); // Gênero
        tabela.getColumnModel().getColumn(3).setPreferredWidth(120); // Plataforma
        tabela.getColumnModel().getColumn(4).setPreferredWidth(60);  // Ano
        tabela.getColumnModel().getColumn(5).setPreferredWidth(100); // Status
        tabela.getColumnModel().getColumn(6).setPreferredWidth(60);  // Nota

        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void criarPanelBotoes() {
        JPanel panelBotoes = new JPanel(new FlowLayout());

        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnRemover = new JButton("Remover");
        JButton btnRelatorio = new JButton("Relatório");

        btnCadastrar.addActionListener(e -> cadastrarJogo());
        btnAtualizar.addActionListener(e -> atualizarJogo());
        btnRemover.addActionListener(e -> removerJogo());
        btnRelatorio.addActionListener(e -> mostrarRelatorioGenero());

        panelBotoes.add(btnCadastrar);
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnRemover);
        panelBotoes.add(btnRelatorio);

        add(panelBotoes, BorderLayout.SOUTH);
    }

    private void cadastrarJogo() {
        JDialog dialog = new JDialog(this, "Cadastrar Jogo", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Campos do formulário
        JTextField txtTitulo = new JTextField(20);
        JTextField txtGenero = new JTextField(20);
        JTextField txtPlataforma = new JTextField(20);
        JSpinner spnAno = new JSpinner(new SpinnerNumberModel(2024, 1970, 2030, 1));
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Jogando", "Concluído", "Wishlist", "Abandonado"});
        JSpinner spnNota = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10.0, 0.1));

        // Layout do formulário
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(txtTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Gênero:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(txtGenero, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Plataforma:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(txtPlataforma, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Ano:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(spnAno, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(cmbStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Nota:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(spnNota, gbc);

        // Botões
        JPanel panelBotoes = new JPanel();
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Game game = new Game(0,
                            txtTitulo.getText().trim(),
                            txtGenero.getText().trim(),
                            txtPlataforma.getText().trim(),
                            (Integer) spnAno.getValue(),
                            (String) cmbStatus.getSelectedItem(),
                            (Double) spnNota.getValue()
                    );

                    service.cadastrar(game);
                    JOptionPane.showMessageDialog(dialog, "Jogo cadastrado com sucesso!");
                    dialog.dispose();
                    carregarTabela();
                    atualizarFiltros();
                } catch (IllegalArgumentException | SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        panelBotoes.add(btnSalvar);
        panelBotoes.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(panelBotoes, gbc);

        dialog.setVisible(true);
    }

    private void atualizarJogo() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um jogo para atualizar.");
            return;
        }

        linha = tabela.convertRowIndexToModel(linha);
        int id = (Integer) modelo.getValueAt(linha, 0);
        String titulo = (String) modelo.getValueAt(linha, 1);
        String genero = (String) modelo.getValueAt(linha, 2);
        String plataforma = (String) modelo.getValueAt(linha, 3);
        int ano = (Integer) modelo.getValueAt(linha, 4);
        String status = (String) modelo.getValueAt(linha, 5);
        double nota = (Double) modelo.getValueAt(linha, 6);

        // Criar dialog similar ao cadastro, mas com campos preenchidos
        JDialog dialog = new JDialog(this, "Atualizar Jogo", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField txtTitulo = new JTextField(titulo, 20);
        JTextField txtGenero = new JTextField(genero, 20);
        JTextField txtPlataforma = new JTextField(plataforma, 20);
        JSpinner spnAno = new JSpinner(new SpinnerNumberModel(ano, 1970, 2030, 1));
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Jogando", "Concluído", "Wishlist", "Abandonado"});
        cmbStatus.setSelectedItem(status);
        JSpinner spnNota = new JSpinner(new SpinnerNumberModel(nota, 0.0, 10.0, 0.1));

        // Layout do formulário (igual ao cadastro)
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(txtTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Gênero:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(txtGenero, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Plataforma:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(txtPlataforma, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Ano:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(spnAno, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(cmbStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Nota:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(spnNota, gbc);

        JPanel panelBotoes = new JPanel();
        JButton btnSalvar = new JButton("Atualizar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Game game = new Game(id,
                            txtTitulo.getText().trim(),
                            txtGenero.getText().trim(),
                            txtPlataforma.getText().trim(),
                            (Integer) spnAno.getValue(),
                            (String) cmbStatus.getSelectedItem(),
                            (Double) spnNota.getValue()
                    );

                    service.atualizar(game);
                    JOptionPane.showMessageDialog(dialog, "Jogo atualizado com sucesso!");
                    dialog.dispose();
                    carregarTabela();
                    atualizarFiltros();
                } catch (IllegalArgumentException | SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        panelBotoes.add(btnSalvar);
        panelBotoes.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(panelBotoes, gbc);

        dialog.setVisible(true);
    }

    private void removerJogo() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um jogo para remover.");
            return;
        }

        linha = tabela.convertRowIndexToModel(linha);
        int id = (Integer) modelo.getValueAt(linha, 0);
        String titulo = (String) modelo.getValueAt(linha, 1);

        int resposta = JOptionPane.showConfirmDialog(this,
                "Deseja realmente remover o jogo '" + titulo + "'?",
                "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                service.remover(id);
                JOptionPane.showMessageDialog(this, "Jogo removido com sucesso!");
                carregarTabela();
                atualizarFiltros();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao remover jogo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarTabela() {
        try {
            modelo.setRowCount(0);
            List<Game> jogos = service.listar();
            for (Game g : jogos) {
                modelo.addRow(new Object[]{
                        g.getId(), g.getTitulo(), g.getGenero(), g.getPlataforma(),
                        g.getAnoLancamento(), g.getStatus(), g.getNota()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar jogos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarFiltros() {
        try {
            // Salvar seleções atuais
            String generoSelecionado = (String) comboGenero.getSelectedItem();
            String plataformaSelecionada = (String) comboPlataforma.getSelectedItem();

            // Atualizar combo gênero
            comboGenero.removeAllItems();
            comboGenero.addItem("Todos");
            List<String> generos = service.getGenerosDisponiveis();
            for (String genero : generos) {
                comboGenero.addItem(genero);
            }
            if (generoSelecionado != null && generos.contains(generoSelecionado)) {
                comboGenero.setSelectedItem(generoSelecionado);
            }

            // Atualizar combo plataforma
            comboPlataforma.removeAllItems();
            comboPlataforma.addItem("Todas");
            List<String> plataformas = service.getPlataformasDisponiveis();
            for (String plataforma : plataformas) {
                comboPlataforma.addItem(plataforma);
            }
            if (plataformaSelecionada != null && plataformas.contains(plataformaSelecionada)) {
                comboPlataforma.setSelectedItem(plataformaSelecionada);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar filtros: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarFiltros() {
        try {
            modelo.setRowCount(0);
            String genero = (String) comboGenero.getSelectedItem();
            String plataforma = (String) comboPlataforma.getSelectedItem();
            String status = (String) comboStatus.getSelectedItem();

            List<Game> jogos = service.listarComFiltros(genero, plataforma, status);
            for (Game g : jogos) {
                modelo.addRow(new Object[]{
                        g.getId(), g.getTitulo(), g.getGenero(), g.getPlataforma(),
                        g.getAnoLancamento(), g.getStatus(), g.getNota()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao aplicar filtros: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFiltros() {
        comboGenero.setSelectedItem("Todos");
        comboPlataforma.setSelectedItem("Todas");
        comboStatus.setSelectedItem("Todos");
        carregarTabela();
    }

    private void mostrarRelatorioGenero() {
        try {
            Map<String, Integer> stats = service.getEstatisticasPorGenero();
            StringBuilder sb = new StringBuilder();
            sb.append("=== RELATÓRIO POR GÊNERO ===\n\n");

            int total = stats.values().stream().mapToInt(Integer::intValue).sum();
            sb.append("Total de jogos: ").append(total).append("\n\n");

            for (Map.Entry<String, Integer> entry : stats.entrySet()) {
                double percentual = (entry.getValue() * 100.0) / total;
                sb.append(String.format("%-15s: %3d jogos (%.1f%%)\n",
                        entry.getKey(), entry.getValue(), percentual));
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Relatório por Gênero", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarRelatorioPlataforma() {
        try {
            Map<String, Integer> stats = service.getEstatisticasPorPlataforma();
            StringBuilder sb = new StringBuilder();
            sb.append("=== RELATÓRIO POR PLATAFORMA ===\n\n");

            int total = stats.values().stream().mapToInt(Integer::intValue).sum();
            sb.append("Total de jogos: ").append(total).append("\n\n");

            for (Map.Entry<String, Integer> entry : stats.entrySet()) {
                double percentual = (entry.getValue() * 100.0) / total;
                sb.append(String.format("%-15s: %3d jogos (%.1f%%)\n",
                        entry.getKey(), entry.getValue(), percentual));
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Relatório por Plataforma", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new GameManager().setVisible(true);
        });
    }
}