import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Tela principal do sistema.
 * Apresenta as funcionalidades em um menu lateral com abas.
 */
public class TelaPrincipal extends TelaBase {

    private JTabbedPane menuAbas;
    private JScrollPane painelTabelaEditais;
    private JScrollPane painelTabelaAlunos;
    private JTable tabelaEditais;
    private JTable tabelaAlunos;
    private JButton botaoCadastrarEdital;
    private JButton botaoListarEditais;
    private JButton botaoDetalharEdital;
    private JButton botaoCalcularResultado;
    private JButton botaoFecharEdital;
    private JButton botaoListarAlunos;
    private JButton botaoDetalharAluno;
    private JButton botaoInscreverMonitoria;
    private JButton botaoVerRanque;
    private JButton botaoSair;
    
    public TelaPrincipal(CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Sistema de Cadastro de Monitores", central, persistencia, nomeArquivo);
    }
    
    @Override
    protected void criarComponentes() {
        // Cabeçalho
        criarCabecalho();
        
        // Menu lateral com abas
        criarMenuLateral();

        // Rodapé
        criarRodape();

        // Criar tabela na interface
        criarPainelTabelaEditais();
        criarPainelTabelaAlunos();
        painelTabelaAlunos.setVisible(false); // Começa invisível
    }

    /**
     * Cria o cabeçalho da tela com informações do usuário.
     */
    private void criarCabecalho() {
        JPanel painelCabecalho = new JPanel();
        painelCabecalho.setLayout(null);
        painelCabecalho.setBackground(Estilos.COR_PRIMARIA);
        painelCabecalho.setBounds(0, 0, Estilos.LARGURA_TELA, 60);

        // Título
        JLabel titulo = criarLabel("Sistema de Cadastro de Monitores", Estilos.FONTE_TITULO);
        titulo.setForeground(Estilos.COR_BRANCO);
        titulo.setBounds(20, 0, 600, 60);
        painelCabecalho.add(titulo);

        // Informações do usuário
        String tipoUsuario = isCoordenador() ? "Coordenador" : "Aluno";
        // Mostrar só até o segundo nome
        String nomeCompleto = sessao.getNomeUsuario();
        String[] nomeESobrenome = {"Nome", "desconhecido"};

        if (nomeCompleto != null && nomeCompleto.contains(" ")) {
            nomeESobrenome = nomeCompleto.split(" ");
        }
        JLabel labelUsuario = criarLabel("Usuário: " + nomeESobrenome[0] + " " + nomeESobrenome[1] + " (" + tipoUsuario + ")",
                Estilos.FONTE_NORMAL);
        labelUsuario.setForeground(Estilos.COR_BRANCO);
        labelUsuario.setBounds(600, 0, 280, 60);
        labelUsuario.setHorizontalAlignment(SwingConstants.RIGHT);
        painelCabecalho.add(labelUsuario);

        painelPrincipal.add(painelCabecalho);
    }

    /**
     * Cria o menu lateral com abas para as funcionalidades.
     */
    private void criarMenuLateral() {
        menuAbas = new JTabbedPane();
        menuAbas.setTabPlacement(JTabbedPane.TOP);
        menuAbas.setFont(Estilos.FONTE_NORMAL);

        // Painel para a aba de Editais
        JPanel painelEditais = criarPainelAba();
        menuAbas.addTab("Editais", null, painelEditais, "Funcionalidades relacionadas a editais, pressione Alt + 1 para abrir essa aba");
        menuAbas.setMnemonicAt(0, KeyEvent.VK_1);

        // Painel para a aba de Alunos
        JPanel painelAlunos = criarPainelAba();
        menuAbas.addTab("Alunos", null, painelAlunos, "Funcionalidades relacionadas a alunos, pressione Alt + 1 para abrir essa aba");
        menuAbas.setMnemonicAt(1, KeyEvent.VK_2);

        // Adicionar botões à aba de Editais
        botaoCadastrarEdital = criarBotao("Cadastrar Edital",
                new OuvinteBotaoCadastrarEdital());
        definirPermissao(botaoCadastrarEdital, isCoordenador());
        painelEditais.add(botaoCadastrarEdital);

        botaoListarEditais = criarBotao("Listar Editais",
                e -> mostrarSucesso("Funcionalidade em desenvolvimento"));
        painelEditais.add(botaoListarEditais);

        botaoDetalharEdital = criarBotao("Detalhar Edital",
                new OuvinteBotaoDetalharEdital());
        painelEditais.add(botaoDetalharEdital);

        botaoCalcularResultado = criarBotao("Calcular Resultado",
                e -> mostrarSucesso("Funcionalidade em desenvolvimento"));
        definirPermissao(botaoCalcularResultado, isCoordenador());
        painelEditais.add(botaoCalcularResultado);

        botaoFecharEdital = criarBotao("Fechar Edital",
                e -> mostrarSucesso("Funcionalidade em desenvolvimento"));
        definirPermissao(botaoFecharEdital, isCoordenador());
        painelEditais.add(botaoFecharEdital);

        // Adicionar botões à aba de Alunos
        botaoListarAlunos = criarBotao("Listar Alunos",
                e -> mostrarSucesso("Funcionalidade em desenvolvimento"));
        definirPermissao(botaoListarAlunos, isCoordenador());
        painelAlunos.add(botaoListarAlunos);

        botaoDetalharAluno = criarBotao("Detalhar Aluno",
                e -> mostrarSucesso("Funcionalidade em desenvolvimento"));
        definirPermissao(botaoDetalharAluno, isCoordenador());
        painelAlunos.add(botaoDetalharAluno);

        botaoInscreverMonitoria = criarBotao("Inscrever em Monitoria",
                e -> mostrarSucesso("Funcionalidade em desenvolvimento"));
        painelAlunos.add(botaoInscreverMonitoria);

        botaoVerRanque = criarBotao("Ver Ranque",
                e -> mostrarSucesso("Funcionalidade em desenvolvimento"));
        painelAlunos.add(botaoVerRanque);

        // Adicionar um Listener para quando a aba trocar exibir tabelas diferentes.
        menuAbas.addChangeListener(new OuvinteTrocaDeAba());

        // Posicionar o JTabbedPane na tela
        // A largura precisa acomodar o texto da aba + os botões
        menuAbas.setBounds(0, 70, 200, Estilos.ALTURA_TELA - 150);
        painelPrincipal.add(menuAbas);
    }

    private void criarPainelTabelaEditais() {
        if (getCentral().getTodosOsEditais().isEmpty()) {
            // Cria um painel vazio para evitar NullPointerException
            painelTabelaEditais = new JScrollPane();
        } else {
            DefaultTableModel modelo = new DefaultTableModel() {
                // Não deixar nenhuma célula ser editável
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            String[] colunas = {"id", "numero", "dataInicio", "dataLimite", "disciplinas", "aberto"};
            // Criar as colunas
            for (String coluna : colunas) {
                modelo.addColumn(coluna);
            }
            // Colocar data em formato DD/MM/AAAA
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            //Filtra as colunas que deseja colocar na tabela
            for (EditalDeMonitoria item : getCentral().getTodosOsEditais()) {
                modelo.addRow(new Object[]{item.getId(), item.getNumero(), item.getDataInicio().format(formatador),
                        item.getDataLimite().format(formatador), item.getDisciplinas(), item.isAberto()});
            }
            tabelaEditais = new JTable(modelo);
            // Não deixa que as colunas sejam reordenadas, isso evita bugs no código.
            tabelaEditais.getTableHeader().setReorderingAllowed(false);
            mudarVisualDeTabela(tabelaEditais);

            // Para a tabela aparecer, ela precisa estar dentro de um JScrollPane
            painelTabelaEditais = new JScrollPane(tabelaEditais);
        }

        painelTabelaEditais.setBounds(210, 125, 650, 400); // Ajuste a posição e o tamanho conforme necessário
        painelPrincipal.add(painelTabelaEditais);
    }

    private void criarPainelTabelaAlunos() {
        if (getCentral().getTodosOsAlunos().isEmpty()) {
            // Cria um painel vazio para evitar NullPointerException
            painelTabelaAlunos = new JScrollPane();
        } else {
            atualizarValoresDaTabelaEdital();
            painelTabelaAlunos = new JScrollPane(tabelaAlunos);
        }

        painelTabelaAlunos.setBounds(210, 125, 650, 400);
        painelPrincipal.add(painelTabelaAlunos);
    }

    public void atualizarValoresDaTabelaEdital() {
        DefaultTableModel modelo = new DefaultTableModel() {
            // Não deixar as células ser editáveis
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] colunas = {"matricula", "nome", "email"};
        for (String coluna : colunas) {
            modelo.addColumn(coluna);
        }

        //Filtra as colunas que deseja colocar na tabela
        for (Aluno alguem : getCentral().getTodosOsAlunos()) {
            modelo.addRow(new Object[]{alguem.getMatricula(), alguem.getNome(), alguem.getEmail()});
        }
        tabelaAlunos = new JTable(modelo);
        // Não deixa que as colunas sejam reordenadas, isso evita bugs no código.
        tabelaAlunos.getTableHeader().setReorderingAllowed(false);
        mudarVisualDeTabela(tabelaAlunos);
    }

    /**
     * Cria um painel padronizado para ser usado em uma aba.
     * @return O painel configurado.
     */
    private JPanel criarPainelAba() {
        JPanel painel = new JPanel();
        // Usar GridLayout para empilhar os botões verticalmente
        painel.setLayout(new GridLayout(0, 1, 10, 10));
        painel.setBackground(Estilos.COR_FUNDO);
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return painel;
    }

    private class OuvinteTrocaDeAba implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            int numeroAba = menuAbas.getSelectedIndex();

            switch (numeroAba) {
                case 0: {
                    // Aba Editais
                    painelTabelaAlunos.setVisible(false);
                    painelTabelaEditais.setVisible(true);
                    break;
                }
                case 1: {
                    // Aba Alunos
                    painelTabelaEditais.setVisible(false);
                    painelTabelaAlunos.setVisible(true);
                    break;
                }
            }
            painelPrincipal.repaint();

        }
    }

    private class OuvinteBotaoDetalharEdital implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int linhaSelecionada = tabelaEditais.getSelectedRow();
            
            if (linhaSelecionada == -1) {
                mostrarErro("Selecione uma linha!");
                return;
            }
            long id = (long) tabelaEditais.getValueAt(linhaSelecionada, 0);

            EditalDeMonitoria edital = getCentral().recuperarEdital(id);

            if (edital != null) {
                TelaDetalharEdital telaDetalhes = new TelaDetalharEdital(edital, getCentral(), getPersistencia(), getNomeArquivo());

                telaDetalhes.inicializar();
                telaDetalhes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            } else {
                mostrarErro("Edital não encontrado");
            }

        }
    }

    public class OuvinteDoFechamentoDaJanela implements WindowListener {

        public void windowOpened(WindowEvent e) {

        }

        public void windowClosing(WindowEvent e) {

        }

        public void windowClosed(WindowEvent e) {
            atualizarValoresDaTabelaEdital();
            tabelaEditais.repaint();
        }

        public void windowIconified(WindowEvent e) {

        }

        public void windowDeiconified(WindowEvent e) {

        }


        public void windowActivated(WindowEvent e) {

        }

        public void windowDeactivated(WindowEvent e) {

        }
    }

    public class OuvinteBotaoCadastrarEdital implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            TelaCadastrarEdital telaCadastrarEdital = new TelaCadastrarEdital(getCentral(), getPersistencia(), getNomeArquivo());
            telaCadastrarEdital.addWindowListener(new OuvinteDoFechamentoDaJanela());

            telaCadastrarEdital.inicializar();
            telaCadastrarEdital.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }

    public void mudarVisualDeTabela(JTable tabela) {
        if (tabela != null) {
            // Cor de fundo e texto
            tabela.setBackground(Color.LIGHT_GRAY);
            tabela.setForeground(Color.BLACK);

            // Cor do cabeçalho
            tabela.getTableHeader().setBackground(Color.DARK_GRAY);
            tabela.getTableHeader().setForeground(Estilos.COR_TEXTO);

            // Fonte
            tabela.setFont(new Font("Arial", Font.PLAIN, 15));
            tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 17));

            tabela.setRowHeight(25);
            tabela.setShowGrid(true);
            tabela.setGridColor(Color.GRAY);

            // Ajustar largura de colunas
            tabela.getColumnModel().getColumn(0).setPreferredWidth(150);
            tabela.getColumnModel().getColumn(1).setPreferredWidth(100);
        }
    }

    /**
     * Cria o rodapé da tela com botão de sair.
     */
    private void criarRodape() {
        JPanel painelRodape = new JPanel();
        painelRodape.setLayout(new FlowLayout(FlowLayout.RIGHT));
        painelRodape.setBackground(Estilos.COR_FUNDO);
        painelRodape.setBounds(0, Estilos.ALTURA_TELA - 80, Estilos.LARGURA_TELA - 20, 60);
        
        botaoSair = criarBotaoSecundario("Sair", e -> {
            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente sair do sistema?",
                    "Confirmar Saída",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            
            if (opcao == JOptionPane.YES_OPTION) {
                sessao.limparSessao();
                System.exit(0);
            }
        });
        
        painelRodape.add(botaoSair);
        painelPrincipal.add(painelRodape);
    }
}
