import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Tela que exibe uma lista de todos os editais cadastrados no sistema.
 */
public class TelaListagemEditais extends TelaBase {

    private JTable tabelaEditais;
    private DefaultTableModel modeloTabela;
    private JButton botaoVerDetalhes;
    private JButton botaoVoltar;

    public TelaListagemEditais(CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Listagem de Editais", central, persistencia, nomeArquivo);
    }

    @Override
    protected void criarComponentes() {
        painelPrincipal.setLayout(null);

        // Título
        JLabel titulo = criarLabel("Listagem de Editais", Estilos.FONTE_TITULO);
        titulo.setBounds(0, 30, Estilos.LARGURA_TELA, 30);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        painelPrincipal.add(titulo);

        // Tabela de Editais
        criarTabela();

        // Botões
        criarBotoes();
    }

    /**
     * Cria e configura a tabela para exibir os editais.
     */
    private void criarTabela() {
        String[] colunas = {"Número", "Data Início", "Data Limite", "Status", "Resultado"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna todas as células não editáveis
            }
        };

        tabelaEditais = new JTable(modeloTabela);
        tabelaEditais.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaEditais.setFont(Estilos.FONTE_NORMAL);
        tabelaEditais.getTableHeader().setFont(Estilos.FONTE_BOTAO);

        // Preenche a tabela com os dados dos editais
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (EditalDeMonitoria edital : getCentral().getTodosOsEditais()) {
            String status = edital.isAberto() ? "Aberto" : "Fechado";
            String resultado = edital.isResultadoCalculado() ? "Calculado" : "Pendente";
            Object[] linha = {
                edital.getNumero(),
                edital.getDataInicio().format(formatador),
                edital.getDataLimite().format(formatador),
                status,
                resultado
            };
            modeloTabela.addRow(linha);
        }

        JScrollPane painelTabela = new JScrollPane(tabelaEditais);
        painelTabela.setBounds(50, 80, Estilos.LARGURA_TELA - 100, 450);
        painelPrincipal.add(painelTabela);
    }

    /**
     * Cria e posiciona os botões da tela.
     */
    private void criarBotoes() {
        botaoVerDetalhes = criarBotao("Ver Detalhes", e -> verDetalhesEdital());
        botaoVerDetalhes.setBounds(250, 550, 200, Estilos.ALTURA_BOTAO);
        painelPrincipal.add(botaoVerDetalhes);

        botaoVoltar = criarBotaoSecundario("Voltar", e -> voltarParaTelaPrincipal());
        botaoVoltar.setBounds(470, 550, 200, Estilos.ALTURA_BOTAO);
        painelPrincipal.add(botaoVoltar);
    }

    /**
     * Ação para o botão "Ver Detalhes".
     * Verifica o status do resultado e abre a tela apropriada.
     */
    private void verDetalhesEdital() {
        int linhaSelecionada = tabelaEditais.getSelectedRow();

        if (linhaSelecionada == -1) {
            mostrarAviso("Por favor, selecione um edital na tabela.");
            return;
        }

        String numeroEdital = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        EditalDeMonitoria editalSelecionado = null;
        for (EditalDeMonitoria edital : getCentral().getTodosOsEditais()) {
            if (edital.getNumero().equals(numeroEdital)) {
                editalSelecionado = edital;
                break;
            }
        }

        if (editalSelecionado != null) {
            // Lógica condicional para abrir a tela correta
            if (editalSelecionado.isResultadoCalculado()) {
                // Se o resultado já foi calculado, abre a tela de resultados
                TelaResultadoEdital telaResultado = new TelaResultadoEdital(editalSelecionado, getCentral(), getPersistencia(), getNomeArquivo());
                telaResultado.inicializar();
            } else {
                // Se não, abre a tela de edição de detalhes
                TelaDetalharEdital telaDetalhes = new TelaDetalharEdital(editalSelecionado, getCentral(), getPersistencia(), getNomeArquivo());
                telaDetalhes.inicializar();
            }
            this.dispose();
        } else {
            mostrarErro("Não foi possível encontrar o edital selecionado.");
        }
    }

    /**
     * Fecha a tela atual e volta para a tela principal.
     */
    private void voltarParaTelaPrincipal() {
        TelaPrincipal telaPrincipal = new TelaPrincipal(getCentral(), getPersistencia(), getNomeArquivo());
        telaPrincipal.inicializar();
        this.dispose();
    }
}
