package br.com.monitoria;

import br.com.monitoria.excecoes.EditalAbertoException;
import br.com.monitoria.excecoes.SemInscricoesException;
import br.com.monitoria.interfaces.Observador;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.format.DateTimeFormatter;

/**
 * Tela principal do sistema.
 * Apresenta as funcionalidades em um menu lateral com abas.
 */
public class TelaPrincipal extends TelaBase implements Observador {

    private JTabbedPane menuAbas;
    private JScrollPane painelTabelaEditais;
    private JScrollPane painelTabelaAlunos;
    private DefaultTableModel modeloTabelaEditais;
    private JTable tabelaEditais;
    private DefaultTableModel modeloTabelaAlunos;
    private TableRowSorter<DefaultTableModel> sorterTabelaAlunos;
    private JTextField campoFiltro;
    private JLabel labelFiltro;
    private JTable tabelaAlunos;
    private JButton botaoCadastrarEdital;
    private JButton botaoListarEditais;
    private JButton botaoDetalharEdital;
    private JButton botaoCalcularResultado;
    private JButton botaoFecharEdital;
    private JButton botaoDetalharAluno; // Botão original, sem alteração
    private JButton botaoVerPerfil; // Novo botão para coordenador
    private JButton botaoMeuPerfil;
    private JButton botaoInscreverMonitoria;
    private JButton botaoVerRanque;
    private JButton botaoSair;

    public TelaPrincipal(CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Sistema de Cadastro de Monitores", central, persistencia, nomeArquivo);
        // Registra a TelaPrincipal como um observador da CentralDeInformacoes
        getCentral().adicionarObservador(this);
    }

    @Override
    public void atualizar() {
        // Este método será chamado pela CentralDeInformacoes quando os dados mudarem
        atualizarValoresDaTabelaEdital();
        if (isCoordenador()) {
            atualizarValoresDaTabelaAluno();
        }
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
        // Tabela de alunos só é criada e exibida para coordenadores
        if (isCoordenador()) {
            criarPainelTabelaAlunos();
            criarCampoFiltro();
            painelTabelaAlunos.setVisible(false); // Começa invisível
            campoFiltro.setVisible(false);
            if (labelFiltro != null) {
                labelFiltro.setVisible(false);
            }
        }
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

        // Adicionar botões à aba de Editais
        botaoCadastrarEdital = criarBotaoLateral("Cadastrar Edital",
                new OuvinteBotaoCadastrarEdital());
        if (!isCoordenador()) {
            botaoCadastrarEdital.setVisible(false);
        }
        painelEditais.add(botaoCadastrarEdital);

        botaoListarEditais = criarBotaoLateral("Listar Editais", e -> abrirTelaListagemEditais());
        painelEditais.add(botaoListarEditais);

        botaoDetalharEdital = criarBotaoLateral("Detalhar Edital", new OuvinteBotaoDetalharEdital());
        painelEditais.add(botaoDetalharEdital);

        botaoCalcularResultado = criarBotaoLateral("Calcular Resultado", new OuvinteBotaoCalcularResultado());
        if (!isCoordenador()) {
            botaoCalcularResultado.setVisible(false);
        }
        painelEditais.add(botaoCalcularResultado);

        botaoFecharEdital = criarBotaoLateral("Fechar Edital",
                e -> mostrarSucesso("Funcionalidade em desenvolvimento"));
        if (!isCoordenador()) {
            botaoFecharEdital.setVisible(false);
        }
        painelEditais.add(botaoFecharEdital);

        // Painel para a aba de Alunos (apenas para coordenador)
        if (isCoordenador()) {
            JPanel painelAlunos = criarPainelAba();
            menuAbas.addTab("Alunos", null, painelAlunos, "Funcionalidades relacionadas a alunos, pressione Alt + 2 para abrir essa aba");
            menuAbas.setMnemonicAt(1, KeyEvent.VK_2);

            // Botão original "Detalhar Aluno" (sem funcionalidade por enquanto)
            botaoDetalharAluno = criarBotaoLateral("Detalhar Aluno",
                    e -> mostrarSucesso("Funcionalidade em desenvolvimento"));
            painelAlunos.add(botaoDetalharAluno);

            // Novo botão "Ver Perfil" para o coordenador
            botaoVerPerfil = criarBotaoLateral("Ver Perfil", new OuvinteBotaoVerPerfil());
            painelAlunos.add(botaoVerPerfil);

        } else {
            // Para alunos, criar uma aba própria com funcionalidades de aluno
            JPanel painelAluno = criarPainelAba();
            menuAbas.addTab("Minhas Funcionalidades", null, painelAluno, "Funcionalidades disponíveis para alunos");
            menuAbas.setMnemonicAt(1, KeyEvent.VK_2);

            botaoMeuPerfil = criarBotaoLateral("Meu Perfil", new OuvinteBotaoMeuPerfil());
            painelAluno.add(botaoMeuPerfil);

            botaoInscreverMonitoria = criarBotaoLateral("Inscrever em Monitoria", new OuvinteBotaoInscreverMonitoria());
            painelAluno.add(botaoInscreverMonitoria);

            botaoVerRanque = criarBotaoLateral("Ver Ranque",
                    e -> mostrarSucesso("Funcionalidade em desenvolvimento"));
            painelAluno.add(botaoVerRanque);
        }

        // Adicionar um Listener para quando a aba trocar exibir tabelas diferentes.
        menuAbas.addChangeListener(new OuvinteTrocaDeAba());

        // Posicionar o JTabbedPane na tela
        menuAbas.setBounds(0, 70, 200, Estilos.ALTURA_TELA - 150);
        painelPrincipal.add(menuAbas);
    }

    private void criarPainelTabelaEditais() {

        modeloTabelaEditais = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        String[] colunas = {"ID", "Número", "Data Início", "Data Limite", "Disciplinas", "Aberto"};
        for (String coluna : colunas) {
            modeloTabelaEditais.addColumn(coluna);
        }

        // Cria a JTable com o modelo
        tabelaEditais = new JTable(modeloTabelaEditais);
        mudarVisualDeTabela(tabelaEditais);

        // Preenche os dados iniciais
        atualizarValoresDaTabelaEdital();

        // Adiciona a tabela (dentro de um JScrollPane) ao painel principal
        painelTabelaEditais = new JScrollPane(tabelaEditais);
        painelTabelaEditais.setBounds(210, 125, 650, 400);
        painelPrincipal.add(painelTabelaEditais);
    }

    private void criarPainelTabelaAlunos() {
        modeloTabelaAlunos = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        String[] colunas = {"Matrícula", "Nome", "Email"};
        for (String coluna : colunas) {
            modeloTabelaAlunos.addColumn(coluna);
        }
        tabelaAlunos = new JTable(modeloTabelaAlunos);
        sorterTabelaAlunos = new TableRowSorter<>(modeloTabelaAlunos);
        tabelaAlunos.setRowSorter(sorterTabelaAlunos);

        mudarVisualDeTabela(tabelaAlunos);
        atualizarValoresDaTabelaAluno();

        painelTabelaAlunos = new JScrollPane(tabelaAlunos);
        painelTabelaAlunos.setBounds(210, 125, 650, 400);
        painelPrincipal.add(painelTabelaAlunos);
    }

    public void criarCampoFiltro() {
        labelFiltro = new JLabel("Filtrar por nome:");
        labelFiltro.setBounds(210, 95, 120, 30);
        painelPrincipal.add(labelFiltro);

        campoFiltro = new JTextField();
        campoFiltro.setBounds(330, 95, 200, 30);
        painelPrincipal.add(campoFiltro);

        campoFiltro.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarTabelaAlunos();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarTabelaAlunos();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarTabelaAlunos();
            }
        });
    }

    private void filtrarTabelaAlunos() {
        String texto = campoFiltro.getText();
        if (texto.trim().length() == 0) {
            sorterTabelaAlunos.setRowFilter(null);
        } else {
            sorterTabelaAlunos.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 1));
        }
    }

    public void atualizarValoresDaTabelaEdital() {
        modeloTabelaEditais.setRowCount(0);
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (getCentral().getTodosOsEditais() == null) {
            return;
        }

        //Filtra as colunas que deseja colocar na tabela
        for (EditalDeMonitoria item : getCentral().getTodosOsEditais()) {
            modeloTabelaEditais.addRow(new Object[]{item.getId(), item.getNumero(), item.getDataInicio().format(formatador),
                    item.getDataLimite().format(formatador), item.getDisciplinas(), item.isAberto() ? "aberto" : "FECHADO"});
        }
    }

    public void atualizarValoresDaTabelaAluno() {
        if (modeloTabelaAlunos == null) return;
        modeloTabelaAlunos.setRowCount(0);

        // Adiciona uma verificação para evitar NullPointerException se a lista for nula.
        if (getCentral().getTodosOsAlunos() == null) {
            return;
        }

        //Filtra as colunas que deseja colocar na tabela
        for (Aluno alguem : getCentral().getTodosOsAlunos()) {
            modeloTabelaAlunos.addRow(new Object[]{alguem.getMatricula(), alguem.getNome(), alguem.getEmail()});
        }
    }

    /**
     * Cria um painel padronizado para ser usado em uma aba.
     * @return O painel configurado.
     */
    private JPanel criarPainelAba() {
        JPanel painel = new JPanel();
        // Usar GridLayout para empilhar os botões verticalmente
        painel.setLayout(new GridLayout(0, 1, 8, 8));
        painel.setBackground(Estilos.COR_FUNDO);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return painel;
    }

    /**
     * Cria um botão lateral moderno e compacto.
     * @param texto O texto do botão
     * @param listener O ActionListener do botão
     * @return O botão criado
     */
    private JButton criarBotaoLateral(String texto, ActionListener listener) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        botao.setMaximumSize(new Dimension(160, 35));
        botao.setMinimumSize(new Dimension(160, 35));
        botao.setPreferredSize(new Dimension(160, 35));
        botao.setBackground(Estilos.COR_PRIMARIA);
        botao.setForeground(Estilos.COR_BRANCO);
        botao.setOpaque(true);
        botao.setContentAreaFilled(true);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Estilos.COR_PRIMARIA.darker(), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            private Color corOriginal = Estilos.COR_PRIMARIA;
            
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                corOriginal = botao.getBackground();
                botao.setBackground(corOriginal.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(corOriginal);
            }
        });
        
        if (listener != null) {
            botao.addActionListener(listener);
        }
        
        return botao;
    }

    private class OuvinteTrocaDeAba implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            int numeroAba = menuAbas.getSelectedIndex();

            if (isCoordenador()) {
                switch (numeroAba) {
                    case 0: {
                        // Aba Editais
                        if (painelTabelaAlunos != null) {
                            painelTabelaAlunos.setVisible(false);
                            campoFiltro.setVisible(false);
                            labelFiltro.setVisible(false);
                        }
                        painelTabelaEditais.setVisible(true);
                        break;
                    }
                    case 1: {
                        // Aba Alunos
                        painelTabelaEditais.setVisible(false);
                        if (painelTabelaAlunos != null) {
                            campoFiltro.setVisible(true);
                            labelFiltro.setVisible(true);
                            painelTabelaAlunos.setVisible(true);
                        }
                        break;
                    }
                }
            } else {
                painelTabelaEditais.setVisible(true);
                if (painelTabelaAlunos != null) {
                    painelTabelaAlunos.setVisible(false);
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
                // O WindowListener não é mais necessário aqui para atualizar a tabela
                telaDetalhes.inicializar();
                telaDetalhes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            } else {
                mostrarErro("Edital não encontrado");
            }
        }
    }

    private class OuvinteBotaoCalcularResultado implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int linhaSelecionada = tabelaEditais.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarErro("Por favor, selecione um edital na tabela para calcular o resultado.");
                return;
            }

            long id = (long) tabelaEditais.getValueAt(linhaSelecionada, 0);
            EditalDeMonitoria edital = getCentral().recuperarEdital(id);

            if (edital == null) {
                mostrarErro("Edital não encontrado. Selecione novamente.");
                return;
            }

            try {
                edital.calcularResultado();
                getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
                mostrarSucesso("Resultado do edital '" + edital.getNumero() + "' calculado e vagas alocadas com sucesso!");
            } catch (EditalAbertoException | SemInscricoesException ex) {
                mostrarErro(ex.getMessage());
            } catch (Exception ex) {
                mostrarErro("Ocorreu um erro inesperado ao calcular o resultado: " + ex.getMessage());
            }
        }
    }

    private class OuvinteBotaoVerPerfil implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int linhaSelecionada = tabelaAlunos.getSelectedRow();

            if (linhaSelecionada == -1) {
                mostrarErro("Selecione um aluno na tabela para ver o perfil!");
                return;
            }

            // Obter a matrícula do aluno da linha selecionada
            String matricula = (String) tabelaAlunos.getValueAt(linhaSelecionada, 0);
            Aluno aluno = getCentral().recuperarAluno(matricula);

            if (aluno != null) {
                TelaPerfilAluno telaPerfil = new TelaPerfilAluno(aluno, getCentral(), getPersistencia(), getNomeArquivo());
                telaPerfil.setVisible(true);
            } else {
                mostrarErro("Aluno não encontrado!");
            }
        }
    }

    private class OuvinteBotaoInscreverMonitoria implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int linhaSelecionada = tabelaEditais.getSelectedRow();

            if (linhaSelecionada == -1) {
                mostrarErro("Selecione uma linha!");
                return;
            }
            long id = (long) tabelaEditais.getValueAt(linhaSelecionada, 0);

            EditalDeMonitoria edital = getCentral().recuperarEdital(id);

            if (edital != null) {
                TelaInscreverEmEditalAluno telaDetalhes = new TelaInscreverEmEditalAluno(edital, getCentral(), getPersistencia(), getNomeArquivo());
                telaDetalhes.inicializar();
                telaDetalhes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            } else {
                mostrarErro("Edital não encontrado");
            }
        }
    }

    private class OuvinteBotaoMeuPerfil implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            // O aluno logado está na sessão
            Aluno alunoLogado = (Aluno) sessao.getUsuarioLogado();
            if (alunoLogado != null) {
                TelaPerfilAluno telaPerfil = new TelaPerfilAluno(alunoLogado, getCentral(), getPersistencia(), getNomeArquivo());
                telaPerfil.setVisible(true);
            } else {
                // Isso não deve acontecer se a lógica de sessão estiver correta
                mostrarErro("Não foi possível encontrar os dados do seu perfil.");
            }
        }
    }

    public class OuvinteBotaoCadastrarEdital implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            TelaCadastrarEdital telaCadastrarEdital = new TelaCadastrarEdital(getCentral(), getPersistencia(), getNomeArquivo());
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

            // Impede que o usuário reordene as colunas, o que poderia quebrar a lógica de pegar valores por índice
            tabela.getTableHeader().setReorderingAllowed(false);
        }
    }

    private void abrirTelaListagemEditais() {
        TelaListagemEditais telaListagem = new TelaListagemEditais(getCentral(), getPersistencia(), getNomeArquivo());
        telaListagem.inicializar();
        this.dispose();
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
