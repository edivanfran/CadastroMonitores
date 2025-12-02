import javax.swing.*;
import java.awt.*;

/**
 * Tela principal do sistema.
 * Apresenta as funcionalidades em um menu lateral com abas.
 */
public class TelaPrincipal extends TelaBase {
    
    private JButton botaoCadastrarEdital;
    private JButton botaoListarEditais;
    private JButton botaoCalcularResultado;
    private JButton botaoFecharEdital;
    private JButton botaoListarAlunos;
    private JButton botaoInscreverMonitoria;
    private JButton botaoVerRanque;
    private JButton botaoSair;
    
    public TelaPrincipal() {
        super("Sistema de Cadastro de Monitores");
    }
    
    @Override
    protected void criarComponentes() {
        // Cabeçalho
        criarCabecalho();
        
        // Menu lateral com abas
        criarMenuLateral();
        
        // Rodapé
        criarRodape();
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

        if (nomeCompleto != null) {
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
        JTabbedPane menuAbas = new JTabbedPane();
        menuAbas.setTabPlacement(JTabbedPane.TOP);
        menuAbas.setFont(Estilos.FONTE_NORMAL);
        
        // Painel para a aba de Editais
        JPanel painelEditais = criarPainelAba();
        menuAbas.addTab("Editais", null, painelEditais, "Funcionalidades relacionadas a editais");
        
        // Painel para a aba de Alunos
        JPanel painelAlunos = criarPainelAba();
        menuAbas.addTab("Alunos", null, painelAlunos, "Funcionalidades relacionadas a alunos");

        // Adicionar botões à aba de Editais
        botaoCadastrarEdital = criarBotao("Cadastrar Edital",
                e -> mostrarSucesso("Funcionalidade em desenvolvimento"));
        definirPermissao(botaoCadastrarEdital, isCoordenador());
        painelEditais.add(botaoCadastrarEdital);
        
        botaoListarEditais = criarBotao("Listar Editais",
                e -> mostrarSucesso("Funcionalidade em desenvolvimento"));
        painelEditais.add(botaoListarEditais);
        
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
        
        botaoInscreverMonitoria = criarBotao("Inscrever em Monitoria",
                e -> mostrarSucesso("Funcionalidade em desenvolvimento"));
        painelAlunos.add(botaoInscreverMonitoria);
        
        botaoVerRanque = criarBotao("Ver Ranque",
                e -> mostrarSucesso("Funcionalidade em desenvolvimento"));
        painelAlunos.add(botaoVerRanque);
        
        // Posicionar o JTabbedPane na tela
        // A largura precisa acomodar o texto da aba + os botões
        menuAbas.setBounds(0, 70, 200, Estilos.ALTURA_TELA - 150);
        painelPrincipal.add(menuAbas);
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
