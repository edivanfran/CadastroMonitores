import javax.swing.*;
import java.awt.*;

/**
 * Tela de cadastro do coordenador.
 * Exibida quando não há coordenador cadastrado no sistema.
 */
public class TelaCadastroCoordenador extends TelaBase {
    
    private JTextField campoNome;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JPasswordField campoConfirmarSenha;
    private JButton btnCadastrar;
    private JButton btnVoltar;
    private CentralDeInformacoes central;
    private Persistencia persistencia;
    private String nomeArquivo;
    
    public TelaCadastroCoordenador(CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Cadastro de Coordenador");
        this.central = central;
        this.persistencia = persistencia;
        this.nomeArquivo = nomeArquivo;
    }
    
    @Override
    protected void criarComponentes() {
        // Logo e título
        criarCabecalho();
        
        // Formulário de cadastro
        criarFormularioCadastro();
    }
    
    /**
     * Cria o cabeçalho com logo e título.
     */
    private void criarCabecalho() {
        JPanel painelCabecalho = new JPanel();
        painelCabecalho.setLayout(new BoxLayout(painelCabecalho, BoxLayout.Y_AXIS));
        painelCabecalho.setBackground(Estilos.COR_FUNDO);
        painelCabecalho.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));
        painelCabecalho.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Logo (se existir)
        JLabel logo = criarLogo();
        if (logo != null) {
            painelCabecalho.add(logo);
            painelCabecalho.add(Box.createVerticalStrut(20));
        }
        
        // Título
        JLabel titulo = criarLabel("Cadastro do Coordenador", Estilos.FONTE_TITULO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelCabecalho.add(titulo);
        
        // Subtítulo
        JLabel subtitulo = criarLabel("O coordenador atua como administrador do sistema", Estilos.FONTE_PEQUENA);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setForeground(Estilos.COR_SECUNDARIA);
        painelCabecalho.add(Box.createVerticalStrut(5));
        painelCabecalho.add(subtitulo);
        
        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);
    }
    
    /**
     * Cria o logo da aplicação.
     * @return JLabel com o logo, ou null se não houver imagem
     */
    private JLabel criarLogo() {
        try {
            // Tenta carregar a imagem do logo
            ImageIcon icon = new ImageIcon("logo.png");
            if (icon.getIconWidth() > 0) {
                // Redimensiona o logo se necessário
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImg);
                
                JLabel logo = new JLabel(icon);
                logo.setAlignmentX(Component.CENTER_ALIGNMENT);
                return logo;
            }
        } catch (Exception e) {
            // Se não encontrar a imagem, continua sem logo
        }
        return null;
    }
    
    /**
     * Cria o formulário de cadastro.
     */
    private void criarFormularioCadastro() {
        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new GridBagLayout());
        painelFormulario.setBackground(Estilos.COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblNome = criarLabel("Nome:", Estilos.FONTE_NORMAL);
        painelFormulario.add(lblNome, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        campoNome = criarCampoTexto(25);
        painelFormulario.add(campoNome, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel lblEmail = criarLabel("E-mail:", Estilos.FONTE_NORMAL);
        painelFormulario.add(lblEmail, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        campoEmail = criarCampoTexto(25);
        painelFormulario.add(campoEmail, gbc);
        
        // Senha
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel lblSenha = criarLabel("Senha:", Estilos.FONTE_NORMAL);
        painelFormulario.add(lblSenha, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        campoSenha = new JPasswordField(25);
        campoSenha.setFont(Estilos.FONTE_NORMAL);
        campoSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilos.COR_SECUNDARIA, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        painelFormulario.add(campoSenha, gbc);
        
        // Confirmar Senha
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel lblConfirmarSenha = criarLabel("Confirmar Senha:", Estilos.FONTE_NORMAL);
        painelFormulario.add(lblConfirmarSenha, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        campoConfirmarSenha = new JPasswordField(25);
        campoConfirmarSenha.setFont(Estilos.FONTE_NORMAL);
        campoConfirmarSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilos.COR_SECUNDARIA, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        painelFormulario.add(campoConfirmarSenha, gbc);
        
        // Botões
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(20, 20, 10, 20);
        
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, Estilos.ESPACAMENTO, 0));
        painelBotoes.setBackground(Estilos.COR_FUNDO);
        
        btnCadastrar = criarBotao("Cadastrar", e -> realizarCadastro());
        painelBotoes.add(btnCadastrar);
        
        btnVoltar = criarBotaoSecundario("Voltar", e -> voltarParaLogin());
        painelBotoes.add(btnVoltar);
        
        painelFormulario.add(painelBotoes, gbc);
        
        // Adiciona listener para Enter no campo de confirmar senha
        campoConfirmarSenha.addActionListener(e -> realizarCadastro());
        
        painelPrincipal.add(painelFormulario, BorderLayout.CENTER);
    }
    
    /**
     * Realiza o cadastro do coordenador.
     */
    private void realizarCadastro() {
        String nome = campoNome.getText().trim();
        String email = campoEmail.getText().trim();
        String senha = new String(campoSenha.getPassword());
        String confirmarSenha = new String(campoConfirmarSenha.getPassword());
        
        // Validações
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            mostrarErro("Por favor, preencha todos os campos.");
            return;
        }
        
        if (!senha.equals(confirmarSenha)) {
            mostrarErro("As senhas não coincidem. Tente novamente.");
            campoSenha.setText("");
            campoConfirmarSenha.setText("");
            campoSenha.requestFocus();
            return;
        }
        
        // Validação de email básica
        if (!email.matches("(?i)^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")) {
            mostrarErro("E-mail inválido. Por favor, insira um e-mail válido.");
            campoEmail.requestFocus();
            return;
        }
        
        // Cadastra o coordenador
        try {
            central.cadastrarCoordenador(email, senha, nome);
            persistencia.salvarCentral(central, nomeArquivo);
            
            // Define o coordenador como usuário logado
            Coordenador coordenador = central.getCoordenador();
            sessao.setUsuarioLogado(coordenador);
            
            mostrarSucesso("Coordenador cadastrado com sucesso!");
            
            // Abre a tela principal
            TelaPrincipal telaPrincipal = new TelaPrincipal();
            telaPrincipal.inicializar();
            this.dispose();
            
        } catch (Exception e) {
            mostrarErro("Erro ao cadastrar coordenador: " + e.getMessage());
        }
    }
    
    /**
     * Volta para a tela de login.
     */
    private void voltarParaLogin() {
        TelaLogin telaLogin = new TelaLogin(central, persistencia, nomeArquivo);
        telaLogin.inicializar();
        this.dispose();
    }
}

