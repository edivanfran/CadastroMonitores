import javax.swing.*;
import java.awt.*;

/**
 * Tela principal do sistema.
 * Demonstra como uma mesma tela pode ter diferentes funcionalidades
 * baseadas no tipo de usuário (Coordenador ou Aluno).
 */
public class TelaPrincipal extends TelaBase {
    
    private JButton btnCadastrarEdital;
    private JButton btnListarEditais;
    private JButton btnCalcularResultado;
    private JButton btnFecharEdital;
    private JButton btnListarAlunos;
    private JButton btnInscreverMonitoria;
    private JButton btnVerRanque;
    private JButton btnSair;
    
    public TelaPrincipal() {
        super("Sistema de Cadastro de Monitores");
    }
    
    @Override
    protected void criarComponentes() {
        // Cabeçalho
        criarCabecalho();
        
        // Área de conteúdo
        criarAreaConteudo();
        
        // Rodapé
        criarRodape();
    }
    
    /**
     * Cria o cabeçalho da tela com informações do usuário.
     */
    private void criarCabecalho() {
        JPanel painelCabecalho = new JPanel();
        painelCabecalho.setLayout(new BorderLayout());
        painelCabecalho.setBackground(Estilos.COR_PRIMARIA);
        painelCabecalho.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Título
        JLabel titulo = criarLabel("Sistema de Cadastro de Monitores", Estilos.FONTE_TITULO);
        titulo.setForeground(Estilos.COR_BRANCO);
        painelCabecalho.add(titulo, BorderLayout.WEST);
        
        // Informações do usuário
        JPanel painelUsuario = new JPanel();
        painelUsuario.setBackground(Estilos.COR_PRIMARIA);
        painelUsuario.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        String tipoUsuario = isCoordenador() ? "Coordenador" : "Aluno";
        JLabel lblUsuario = criarLabel("Usuário: " + sessao.getNomeUsuario() + " (" + tipoUsuario + ")", 
                Estilos.FONTE_NORMAL);
        lblUsuario.setForeground(Estilos.COR_BRANCO);
        painelUsuario.add(lblUsuario);
        
        painelCabecalho.add(painelUsuario, BorderLayout.EAST);
        
        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);
    }
    
    /**
     * Cria a área de conteúdo com os botões de funcionalidades.
     */
    private void criarAreaConteudo() {
        JPanel painelConteudo = new JPanel();
        painelConteudo.setLayout(new GridBagLayout());
        painelConteudo.setBackground(Estilos.COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Título da seção
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTituloSecao = criarLabel("Funcionalidades Disponíveis", Estilos.FONTE_SUBTITULO);
        painelConteudo.add(lblTituloSecao, gbc);
        
        // Botões - Funcionalidades do Coordenador
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        
        // Cadastrar Edital (apenas coordenador)
        btnCadastrarEdital = criarBotao("Cadastrar Edital", e -> {
            // TODO: Implementar ação
            mostrarSucesso("Funcionalidade em desenvolvimento");
        });
        gbc.gridx = 0;
        painelConteudo.add(btnCadastrarEdital, gbc);
        definirPermissao(btnCadastrarEdital, isCoordenador());
        
        // Listar Editais (todos)
        btnListarEditais = criarBotao("Listar Editais", e -> {
            // TODO: Implementar ação
            mostrarSucesso("Funcionalidade em desenvolvimento");
        });
        gbc.gridx = 1;
        painelConteudo.add(btnListarEditais, gbc);
        
        // Calcular Resultado (apenas coordenador)
        btnCalcularResultado = criarBotao("Calcular Resultado", e -> {
            // TODO: Implementar ação
            mostrarSucesso("Funcionalidade em desenvolvimento");
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        painelConteudo.add(btnCalcularResultado, gbc);
        definirPermissao(btnCalcularResultado, isCoordenador());
        
        // Fechar Edital (apenas coordenador)
        btnFecharEdital = criarBotao("Fechar Edital", e -> {
            // TODO: Implementar ação
            mostrarSucesso("Funcionalidade em desenvolvimento");
        });
        gbc.gridx = 1;
        painelConteudo.add(btnFecharEdital, gbc);
        definirPermissao(btnFecharEdital, isCoordenador());
        
        // Listar Alunos (apenas coordenador)
        btnListarAlunos = criarBotao("Listar Alunos", e -> {
            // TODO: Implementar ação
            mostrarSucesso("Funcionalidade em desenvolvimento");
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        painelConteudo.add(btnListarAlunos, gbc);
        definirPermissao(btnListarAlunos, isCoordenador());
        
        // Inscrever em Monitoria (todos)
        btnInscreverMonitoria = criarBotao("Inscrever em Monitoria", e -> {
            // TODO: Implementar ação
            mostrarSucesso("Funcionalidade em desenvolvimento");
        });
        gbc.gridx = 1;
        painelConteudo.add(btnInscreverMonitoria, gbc);
        
        // Ver Ranque (todos, mas apenas após cálculo)
        btnVerRanque = criarBotao("Ver Ranque", e -> {
            // TODO: Implementar ação
            mostrarSucesso("Funcionalidade em desenvolvimento");
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        painelConteudo.add(btnVerRanque, gbc);
        
        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);
    }
    
    /**
     * Cria o rodapé da tela com botão de sair.
     */
    private void criarRodape() {
        JPanel painelRodape = new JPanel();
        painelRodape.setLayout(new FlowLayout(FlowLayout.RIGHT));
        painelRodape.setBackground(Estilos.COR_FUNDO);
        painelRodape.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        btnSair = criarBotaoSecundario("Sair", e -> {
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
        
        painelRodape.add(btnSair);
        painelPrincipal.add(painelRodape, BorderLayout.SOUTH);
    }
}

