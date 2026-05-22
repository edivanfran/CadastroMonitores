package br.com.monitoria;

import br.com.monitoria.interfaces.Observador;
import br.com.monitoria.model.EditalDeMonitoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Tela que exibe uma lista de todos os editais cadastrados no sistema.
 */
public class TelaListagemEditais extends TelaBase implements Observador {

    private JTable tabelaEditais;
    private DefaultTableModel modeloTabela;
    private JButton botaoVerDetalhes;
    private JButton botaoVoltar;

    public TelaListagemEditais() {
        super("Listagem de Editais");
        GerenciadorDeEventos.getInstancia().adicionarObservador(this);
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

        // Carrega os dados iniciais na tabela
        recarregarDadosDaTabela();

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

        GerenciadorDeDados gerenciadorDeDados = GerenciadorDeDados.getInstancia();
        List<EditalDeMonitoria> editais = gerenciadorDeDados.getTodosOsEditais();

        String numeroEdital = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        EditalDeMonitoria editalSelecionado = null;
        for (EditalDeMonitoria edital : editais) {
            if (edital.getNumero().equals(numeroEdital)) {
                editalSelecionado = edital;
                break;
            }
        }

        if (editalSelecionado != null) {
            // Esconde a tela de listagem atual em vez de fechá-la
            this.setVisible(false);

            // Lógica condicional para abrir a tela correta
            if (editalSelecionado.isResultadoCalculado()) {
                // Se o resultado já foi calculado, abre a tela de resultados
                TelaResultadoEdital telaResultado = new TelaResultadoEdital(editalSelecionado);
                telaResultado.addWindowListener(new WindowAdapter() {
                    public void windowClosed(WindowEvent e) {
                        // Quando a tela de resultado fechar, apenas reexibe e atualiza a tela de listagem
                        recarregarDadosETornarVisivel();
                    }
                });
                telaResultado.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                telaResultado.inicializar();
            } else {
                // Se não, abre a tela de edição de detalhes
                TelaDetalharEdital telaDetalhes = new TelaDetalharEdital(editalSelecionado);
                telaDetalhes.addWindowListener(new WindowAdapter() {
                    public void windowClosed(WindowEvent e) {
                        // Quando a tela de detalhes fechar, apenas reexibe e atualiza a tela de listagem
                        recarregarDadosETornarVisivel();
                    }
                });
                telaDetalhes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                telaDetalhes.inicializar();
            }
        } else {
            mostrarErro("Não foi possível encontrar o edital selecionado.");
        }
    }

    /**
     * Atualiza os dados da tabela e torna a janela visível.
     * Usado para "voltar" de outra tela.
     */
    private void recarregarDadosETornarVisivel() {
        recarregarDadosDaTabela();
        // Reexibe a janela
        this.setVisible(true);
    }

    /**
     * Limpa a tabela e a preenche novamente com os dados mais recentes do banco.
     */
    private void recarregarDadosDaTabela() {
        modeloTabela.setRowCount(0);

        GerenciadorDeDados gerenciadorDeDados = GerenciadorDeDados.getInstancia();
        List<EditalDeMonitoria> editais = gerenciadorDeDados.getTodosOsEditais();

        // Preenche a tabela com os dados dos editais
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (EditalDeMonitoria edital : editais) {
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
    }

    /**
     * Fecha a tela atual e volta para a tela principal.
     */
    private void voltarParaTelaPrincipal() {
        TelaPrincipal telaPrincipal = new TelaPrincipal();
        telaPrincipal.inicializar();
        this.dispose();
    }

    @Override
    public void atualizar() {
        SwingUtilities.invokeLater(this::recarregarDadosDaTabela);
    }

    @Override
    public void dispose() {
        GerenciadorDeEventos.getInstancia().removerObservador(this);
        super.dispose();
    }
}
