import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
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

    public TelaResultadoEdital(EditalDeMonitoria edital, CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Resultado do Edital: " + edital.getNumero(), central, persistencia, nomeArquivo);
        this.edital = edital;
        setSize(800, 600);
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
            Map<String, ArrayList<Inscricao>> ranques = edital.getRanquePorDisciplina();
            if (ranques.isEmpty() && edital.getInscricoes().isEmpty()) {
                JPanel painelAviso = new JPanel(new GridBagLayout());
                JLabel aviso = criarLabel("Não há inscrições neste edital para exibir.", Estilos.FONTE_NORMAL);
                painelAviso.add(aviso);
                abasDisciplinas.addTab("Aviso", painelAviso);
            } else {
                for (Disciplina disciplina : edital.getDisciplinas()) {
                    ArrayList<Inscricao> ranque = ranques.get(disciplina.getNomeDisciplina());
                    JPanel painelDisciplina = new JPanel(new BorderLayout());
                    JTable tabelaRanque = criarTabelaRanque(ranque, disciplina);
                    painelDisciplina.add(new JScrollPane(tabelaRanque), BorderLayout.CENTER);
                    abasDisciplinas.addTab(disciplina.getNomeDisciplina(), painelDisciplina);
                }
            }
        }
        painelPrincipal.add(abasDisciplinas, BorderLayout.CENTER);
    }

    private JTable criarTabelaRanque(ArrayList<Inscricao> ranque, Disciplina disciplina) {
        String[] colunas = {"Pos.", "Aluno", "Pontuação", "Status", "Ação"};
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        if (ranque != null) {
            int pos = 1;
            for (Inscricao inscricao : ranque) {
                String status = (pos <= disciplina.getVagasRemuneradas()) ? "Contemplado (Bolsa)" :
                                (pos <= disciplina.getVagasRemuneradas() + disciplina.getVagasVoluntarias()) ? "Contemplado (Voluntário)" : "Não Contemplado";
                
                modeloTabela.addRow(new Object[]{
                    pos,
                    inscricao.getNomeAluno(),
                    String.format("%.2f", inscricao.getPontuacaoFinal()),
                    status,
                    "Desistir"
                });
                pos++;
            }
        }

        JTable tabela = new JTable(modeloTabela);
        tabela.setRowHeight(30);
        
        // Configura a coluna "Ação" para ter um botão
        tabela.getColumn("Ação").setCellRenderer(new BotaoRenderer());
        tabela.getColumn("Ação").setCellEditor(new BotaoEditor(new JCheckBox(), edital, disciplina, this));

        // Oculta a coluna de ação se o usuário for Coordenador ou se o período de desistência estiver encerrado
        if (isCoordenador() || edital.isPeriodoDesistenciaEncerrado()) {
             tabela.getColumnModel().getColumn(4).setMinWidth(0);
             tabela.getColumnModel().getColumn(4).setMaxWidth(0);
        }

        return tabela;
    }

    private void criarPainelDeBotoes() {
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        if (isCoordenador()) {
            if (!edital.isResultadoCalculado()) {
                botaoAcaoCoordenador = criarBotao("Calcular Resultado", e -> calcularResultado());
            } else if (!edital.isPeriodoDesistenciaEncerrado()) {
                botaoAcaoCoordenador = criarBotao("Encerrar Período de Desistência", e -> encerrarPeriodo());
            }
            if (botaoAcaoCoordenador != null) {
                painelBotoes.add(botaoAcaoCoordenador);
            }
        }

        botaoVoltar = criarBotaoSecundario("Voltar", e -> voltarParaListagem());
        painelBotoes.add(botaoVoltar);

        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
    }

    private void calcularResultado() {
        try {
            edital.calcularResultado();
            getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
            mostrarSucesso("Resultado calculado com sucesso!");
            recarregarTela();
        } catch (Exception e) {
            mostrarErro("Erro ao calcular resultado: " + e.getMessage());
        }
    }

    private void encerrarPeriodo() {
        try {
            edital.encerrarPeriodoDesistencia(getCentral().getCoordenador());
            getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
            mostrarSucesso("Período de desistências encerrado. O resultado agora é final.");
            recarregarTela();
        } catch (Exception e) {
            mostrarErro("Erro ao encerrar período: " + e.getMessage());
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

/**
 * Renderizador que desenha um botão na célula da tabela.
 */
class BotaoRenderer extends JButton implements TableCellRenderer {
    public BotaoRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

/**
 * Editor que trata o clique no botão da célula da tabela.
 */
class BotaoEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private EditalDeMonitoria edital;
    private Disciplina disciplina;
    private TelaResultadoEdital tela;
    private int row;

    public BotaoEditor(JCheckBox checkBox, EditalDeMonitoria edital, Disciplina disciplina, TelaResultadoEdital tela) {
        super(checkBox);
        this.edital = edital;
        this.disciplina = disciplina;
        this.tela = tela;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            int resposta = JOptionPane.showConfirmDialog(tela,
                    "Tem certeza que deseja desistir da vaga para " + disciplina.getNomeDisciplina() + "?",
                    "Confirmar Desistência", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                try {
                    Inscricao inscricaoAlvo = edital.getRanquePorDisciplina().get(disciplina.getNomeDisciplina()).get(this.row);
                    edital.processarDesistencia(inscricaoAlvo.getAluno(), disciplina);
                    tela.getPersistencia().salvarCentral(tela.getCentral(), tela.getNomeArquivo());
                    JOptionPane.showMessageDialog(tela, "Desistência registrada com sucesso. O resultado será recalculado.");
                    tela.recarregarTela();
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
