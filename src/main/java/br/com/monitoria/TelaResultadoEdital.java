package br.com.monitoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Tela para exibir o resultado de um edital, com ranqueamentos,
 * e permitir ações como desistir de vaga ou gerenciar o resultado (coordenador).
 */
public class TelaResultadoEdital extends TelaBase {

    private EditalDeMonitoria edital;
    private JTabbedPane abasDisciplinas;
    private JButton botaoAcaoCoordenador;
    private JButton botaoVoltar;
    private JButton botaoExportarPDF;

    public TelaResultadoEdital(EditalDeMonitoria edital, CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Resultado do Edital: " + edital.getNumero(), central, persistencia, nomeArquivo);
        this.edital = edital;
        setSize(900, 700); // Aumentar a largura para melhor visualização
        setLocationRelativeTo(null);
    }

    @Override
    protected void criarComponentes() {
        painelPrincipal.setLayout(new BorderLayout());

        JLabel titulo = criarLabel("Resultado do Edital " + edital.getNumero(), Estilos.FONTE_TITULO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        criarPainelDeResultados();
        criarPainelDeBotoes();
    }

    private void criarPainelDeResultados() {
        abasDisciplinas = new JTabbedPane();
        abasDisciplinas.setFont(Estilos.FONTE_NORMAL);

        if (!edital.isResultadoCalculado()) {
            JPanel painelAviso = new JPanel(new GridBagLayout());
            JLabel aviso = criarLabel("O resultado deste edital ainda não foi calculado.", Estilos.FONTE_NORMAL);
            painelAviso.add(aviso);
            abasDisciplinas.addTab("Aviso", painelAviso);
        } else {
            // Agrupa todas as inscrições (ativas e desistentes) por disciplina
            Map<String, ArrayList<Inscricao>> todasInscricoesPorDisciplina = new HashMap<>();
            for (Inscricao inscricao : edital.getInscricoes()) {
                String nomeDisciplina = inscricao.getDisciplina().getNomeDisciplina();
                todasInscricoesPorDisciplina.putIfAbsent(nomeDisciplina, new ArrayList<>());
                todasInscricoesPorDisciplina.get(nomeDisciplina).add(inscricao);
            }

            for (Disciplina disciplina : edital.getDisciplinas()) {
                ArrayList<Inscricao> inscricoesDaDisciplina = todasInscricoesPorDisciplina.get(disciplina.getNomeDisciplina());
                JPanel painelDisciplina = new JPanel(new BorderLayout());

                // Se não houver inscrições para uma disciplina, mostra um aviso
                if (inscricoesDaDisciplina == null || inscricoesDaDisciplina.isEmpty()) {
                    JPanel painelAviso = new JPanel(new GridBagLayout());
                    JLabel aviso = criarLabel("Não há inscrições para esta disciplina.", Estilos.FONTE_NORMAL);
                    painelAviso.add(aviso);
                    painelDisciplina.add(painelAviso, BorderLayout.CENTER);
                } else {
                    JTable tabelaRanque = criarTabelaRanque(inscricoesDaDisciplina, disciplina);
                    painelDisciplina.add(new JScrollPane(tabelaRanque), BorderLayout.CENTER);
                }
                abasDisciplinas.addTab(disciplina.getNomeDisciplina(), painelDisciplina);
            }
        }
        painelPrincipal.add(abasDisciplinas, BorderLayout.CENTER);
    }

    private JTable criarTabelaRanque(ArrayList<Inscricao> inscricoes, Disciplina disciplina) {
        String[] colunas = {"Pos.", "Aluno", "Matrícula", "Pontuação", "Status", "Ação"};
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Ação
            }
        };

        ArrayList<Inscricao> ranqueAtivo = edital.getRanquePorDisciplina().get(disciplina.getNomeDisciplina());
        if (ranqueAtivo == null) ranqueAtivo = new ArrayList<>();

        // Ordena a lista completa para garantir consistência na exibição
        inscricoes.sort((i1, i2) -> Double.compare(i2.getPontuacaoFinal(), i1.getPontuacaoFinal()));

        for (Inscricao inscricao : inscricoes) {
            String status;
            int pos = ranqueAtivo.indexOf(inscricao) + 1;

            if (inscricao.isDesistiu()) {
                status = "Desistente";
                pos = 0;
            } else {
                if (pos > 0 && pos <= disciplina.getVagasRemuneradas()) {
                    status = "Contemplado (Bolsa)";
                } else if (pos > 0 && pos <= disciplina.getVagasRemuneradas() + disciplina.getVagasVoluntarias()) {
                    status = "Contemplado (Voluntário)";
                } else {
                    status = "Não Contemplado";
                }
            }

            modeloTabela.addRow(new Object[]{
                (pos > 0) ? String.valueOf(pos) : "-",
                inscricao.getNomeAluno(),
                inscricao.getMatriculaAluno(),
                String.format("%.2f", inscricao.getPontuacaoFinal()),
                status,
                "Desistir"
            });
        }

        JTable tabela = new JTable(modeloTabela);
        tabela.setRowHeight(30);
        tabela.setFont(Estilos.FONTE_NORMAL);
        tabela.getTableHeader().setFont(Estilos.FONTE_BOTAO);

        TableColumn colunaAcao = tabela.getColumn("Ação");
        colunaAcao.setCellRenderer(new BotaoRenderer());
        colunaAcao.setCellEditor(new BotaoEditor(new JCheckBox(), edital, disciplina, this));

        Usuario usuarioLogado = sessao.getUsuarioLogado();
        for (int i = 0; i < tabela.getRowCount(); i++) {
            String matriculaNaTabela = (String) tabela.getValueAt(i, 2);
            boolean isUsuarioDaLinha = usuarioLogado instanceof Aluno && ((Aluno) usuarioLogado).getMatricula().equals(matriculaNaTabela);
            boolean podeDesistir = isUsuarioDaLinha && !edital.isPeriodoDesistenciaEncerrado() && !((String)tabela.getValueAt(i, 4)).equals("Desistente");

            if (!podeDesistir) {
                tabela.setValueAt(null, i, 5);
            }
        }
        
        if (!isCoordenador()) {
            TableColumn colunaMatricula = tabela.getColumn("Matrícula");
            colunaMatricula.setMinWidth(0);
            colunaMatricula.setMaxWidth(0);
            colunaMatricula.setPreferredWidth(0);
        }

        return tabela;
    }

    private void criarPainelDeBotoes() {
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        if (isCoordenador()) {
            // Botão para exportar o resultado em PDF
            if (edital.isResultadoCalculado()) {
                botaoExportarPDF = criarBotao("Exportar Resultado (PDF)", e -> exportarResultadoPDF());
                painelBotoes.add(botaoExportarPDF);
            }

            // Botão para fechar o edital
            if (edital.isResultadoCalculado() && !edital.isPeriodoDesistenciaEncerrado()) {
                botaoAcaoCoordenador = criarBotao("Fechar Edital", e -> encerrarPeriodo());
                botaoAcaoCoordenador.setToolTipText("Encerra o período de desistências e torna o resultado final.");
                painelBotoes.add(botaoAcaoCoordenador);
            } else if (edital.isPeriodoDesistenciaEncerrado()) {
                JLabel resultadoFinalLabel = criarLabel("Resultado Final", Estilos.FONTE_SUBTITULO);
                resultadoFinalLabel.setForeground(Estilos.COR_VERDE_CLARO);
                painelBotoes.add(resultadoFinalLabel);
            }
        }

        botaoVoltar = criarBotaoSecundario("Voltar", e -> voltarParaListagem());
        painelBotoes.add(botaoVoltar);

        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
    }

    private void exportarResultadoPDF() {
        try {
            GeradorDeRelatorios.gerarResultadoEdital(edital);
            mostrarSucesso("Relatório PDF gerado com sucesso na pasta do projeto!");
        } catch (Exception e) {
            mostrarErro("Erro ao gerar PDF: " + e.getMessage());
        }
    }

    private void encerrarPeriodo() {
        int resposta = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja fechar o edital? Esta ação é irreversível\n" +
                "e tornará o resultado atual em final.",
                "Confirmar Encerramento", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                edital.encerrarPeriodoDesistencia((Coordenador) sessao.getUsuarioLogado());
                getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
                mostrarSucesso("Edital fechado com sucesso. O resultado agora é final.");
                recarregarTela();
            } catch (Exception e) {
                mostrarErro("Erro ao fechar o edital: " + e.getMessage());
            }
        }
    }

    public void recarregarTela() {
        TelaResultadoEdital novaTela = new TelaResultadoEdital(this.edital, getCentral(), getPersistencia(), getNomeArquivo());
        novaTela.inicializar();
        this.dispose();
    }

    private void voltarParaListagem() {
        TelaListagemEditais telaListagem = new TelaListagemEditais(getCentral(), getPersistencia(), getNomeArquivo());
        telaListagem.inicializar();
        this.dispose();
    }
}

class BotaoRenderer extends JButton implements TableCellRenderer {
    public BotaoRenderer() {
        setOpaque(true);
        setFont(Estilos.FONTE_PEQUENA);
        setBackground(new Color(220, 53, 69));
        setForeground(Color.WHITE);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        setVisible(value != null && !value.toString().isEmpty());
        return this;
    }
}

class BotaoEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private EditalDeMonitoria edital;
    private Disciplina disciplina;
    private TelaResultadoEdital tela;
    private int row;
    private JTable table;

    public BotaoEditor(JCheckBox checkBox, EditalDeMonitoria edital, Disciplina disciplina, TelaResultadoEdital tela) {
        super(checkBox);
        this.edital = edital;
        this.disciplina = disciplina;
        this.tela = tela;
        button = new JButton();
        button.setOpaque(true);
        button.setFont(Estilos.FONTE_PEQUENA);
        button.setBackground(new Color(220, 53, 69));
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        this.row = row;
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = (value != null && !value.toString().isEmpty());
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            int resposta = JOptionPane.showConfirmDialog(tela,
                    "Tem certeza que deseja desistir da vaga para " + disciplina.getNomeDisciplina() + "?",
                    "Confirmar Desistência", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                try {
                    // LÓGICA CORRIGIDA E SIMPLIFICADA
                    DefaultTableModel model = (DefaultTableModel) this.table.getModel();
                    String matriculaAlvo = (String) model.getValueAt(this.row, 2); // Pega a matrícula da coluna 2

                    Aluno alunoAlvo = tela.getCentral().recuperarAluno(matriculaAlvo);

                    if (alunoAlvo != null) {
                        edital.processarDesistencia(alunoAlvo, disciplina);
                        tela.getPersistencia().salvarCentral(tela.getCentral(), tela.getNomeArquivo());
                        JOptionPane.showMessageDialog(tela, "Desistência registrada com sucesso. O resultado foi recalculado.");
                        tela.recarregarTela();
                    } else {
                        throw new Exception("Não foi possível encontrar o aluno para processar a desistência.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(tela, "Erro ao processar desistência: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        isPushed = false;
        return label;
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
