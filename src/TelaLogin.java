import javax.swing.*;
import java.awt.*;

/**
 * Tela de login do sistema.
 * Verifica se há coordenador cadastrado e direciona para cadastro se necessário.
 */
public class TelaLogin extends TelaBase {
    
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton btnLogin;
    private JButton btnCadastrarCoordenador;
    private CentralDeInformacoes central;
    private Persistencia persistencia;
    private String nomeArquivo;
    
    public TelaLogin(CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Sistema de Cadastro de Monitores - Login");
        this.central = central;
        this.persistencia = persistencia;
        this.nomeArquivo = nomeArquivo;
    }
    
    @Override
    protected void criarComponentes() {
        // Logo e título
        criarCabecalho();
        
        // Formulário de login
        criarFormularioLogin();
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
        JLabel titulo = criarLabel("Login", Estilos.FONTE_TITULO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelCabecalho.add(titulo);
        
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
     * Cria o formulário de login.
     */
    private void criarFormularioLogin() {
        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new GridBagLayout());
        painelFormulario.setBackground(Estilos.COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
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
        gbc.gridy = 1;
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
        
        // Botão de Login (verde claro)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(20, 20, 10, 20);
        btnLogin = criarBotaoLogin("Entrar");
        painelFormulario.add(btnLogin, gbc);
        
        // Botão de Cadastrar Coordenador (se não houver coordenador)
        if (!central.temCoordenador()) {
            gbc.gridy = 3;
            gbc.insets = new Insets(10, 20, 20, 20);
            btnCadastrarCoordenador = criarBotaoSecundario("Cadastrar Coordenador", e -> abrirTelaCadastroCoordenador());
            painelFormulario.add(btnCadastrarCoordenador, gbc);
        }
        
        // Adiciona listener para Enter no campo de senha
        campoSenha.addActionListener(e -> realizarLogin());
        
        // Adiciona listener para Enter no campo de email
        campoEmail.addActionListener(e -> campoSenha.requestFocus());
        
        painelPrincipal.add(painelFormulario, BorderLayout.CENTER);
    }
    
    /**
     * Cria o botão de login com cor verde claro.
     * @param texto O texto do botão
     * @return O botão criado
     */
    private JButton criarBotaoLogin(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(Estilos.FONTE_BOTAO);
        botao.setPreferredSize(new Dimension(Estilos.LARGURA_BOTAO, Estilos.ALTURA_BOTAO));
        botao.setBackground(Estilos.COR_VERDE_CLARO);
        botao.setForeground(Estilos.COR_TEXTO);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(Estilos.COR_VERDE_CLARO_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(Estilos.COR_VERDE_CLARO);
            }
        });
        
        botao.addActionListener(e -> realizarLogin());
        
        return botao;
    }
    
    /**
     * Realiza o processo de login.
     */
    private void realizarLogin() {
        String email = campoEmail.getText().trim();
        String senha = new String(campoSenha.getPassword());
        
        // Validação básica
        if (email.isEmpty() || senha.isEmpty()) {
            mostrarErro("Por favor, preencha todos os campos.");
            return;
        }
        
        // Verifica se é coordenador
        if (central.temCoordenador()) {
            Coordenador coordenador = central.getCoordenador();
            if (coordenador.getEmail().equalsIgnoreCase(email) && 
                coordenador.getSenha().equals(senha)) {
                sessao.setUsuarioLogado(coordenador);
                abrirTelaPrincipal();
                return;
            }
        }
        
        // Verifica se é aluno
        if (central.isLoginPermitido(email, senha)) {
            Aluno aluno = central.retornarAlunoPeloEmail(email);
            if (aluno != null) {
                sessao.setUsuarioLogado(aluno);
                abrirTelaPrincipal();
                return;
            }
        }
        
        // Credenciais inválidas
        mostrarErro("E-mail ou senha inválidos. Tente novamente.");
        campoSenha.setText("");
        campoEmail.requestFocus();
    }
    
    /**
     * Abre a tela de cadastro de coordenador.
     */
    private void abrirTelaCadastroCoordenador() {
        TelaCadastroCoordenador telaCadastro = new TelaCadastroCoordenador(central, persistencia, nomeArquivo);
        telaCadastro.inicializar();
        this.dispose();
    }
    
    /**
     * Abre a tela principal após login bem-sucedido.
     */
    private void abrirTelaPrincipal() {
        TelaPrincipal telaPrincipal = new TelaPrincipal();
        telaPrincipal.inicializar();
        this.dispose();
    }
}

