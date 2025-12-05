import javax.swing.*;
import java.awt.*;

/**
 * Tela de login do sistema.
 * Verifica se há coordenador cadastrado e direciona para cadastro se necessário.
 */
public class TelaLogin extends TelaBase {
    
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoLogin;
    
    public TelaLogin(CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Sistema de Cadastro de Monitores - Login",
                central,
                persistencia,
                nomeArquivo);
    }

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
        // Logo (se existir)
        JLabel logo = criarLogo();
        if (logo != null) {
            logo.setBounds(375, 50, 150, 150); // Centralizado horizontalmente
            painelPrincipal.add(logo);
        }

        // Título
        JLabel titulo = criarLabel("Login do Sistema", Estilos.FONTE_TITULO);
        titulo.setBounds(0, 220, Estilos.LARGURA_TELA, 30);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        painelPrincipal.add(titulo);
    }
    
    /**
     * Cria o logo da aplicação.
     * @return JLabel com o logo, ou null se não houver imagem
     */
    private JLabel criarLogo() {
        try {
            // Tenta carregar a imagem do logo
            ImageIcon icon = new ImageIcon("IFPB_icon.jpg");
            if (icon.getIconWidth() > 0) {
                // Redimensiona o logo para um tamanho padrão
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImg);
                return new JLabel(icon);
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
        int yInicial = 300;
        int alturaCampo = 40;
        int espacamento = 15;
        int larguraLabel = 80;
        int larguraCampo = 350;
        int xLabel = (Estilos.LARGURA_TELA - larguraLabel - larguraCampo - 10) / 2;
        int xCampo = xLabel + larguraLabel + 10;

        //Email
        JLabel lblEmail = criarLabel("Email:", Estilos.FONTE_NORMAL);
        lblEmail.setBounds(xLabel, yInicial, larguraLabel, alturaCampo);
        lblEmail.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(lblEmail);

        campoEmail = criarCampoTexto(25);
        campoEmail.setToolTipText("Insira aqui seu e-mail.");
        campoEmail.setBounds(xCampo, yInicial, larguraCampo, alturaCampo);
        painelPrincipal.add(campoEmail);

        //Senha
        yInicial += alturaCampo + espacamento;
        JLabel lblSenha = criarLabel("Senha:", Estilos.FONTE_NORMAL);
        lblSenha.setBounds(xLabel, yInicial, larguraLabel, alturaCampo);
        lblSenha.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(lblSenha);

        campoSenha = new JPasswordField();
        campoSenha.setToolTipText("Insira aqui sua senha.");
        campoSenha.setFont(Estilos.FONTE_NORMAL);
        campoSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilos.COR_SECUNDARIA, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        campoSenha.setBounds(xCampo, yInicial, larguraCampo, alturaCampo);
        painelPrincipal.add(campoSenha);

        // Botão de Login
        yInicial += alturaCampo + espacamento + 20;
        botaoLogin = criarBotaoLogin("Entrar");
        botaoLogin.setBounds((Estilos.LARGURA_TELA - Estilos.LARGURA_BOTAO) / 2, yInicial, Estilos.LARGURA_BOTAO, Estilos.ALTURA_BOTAO);
        painelPrincipal.add(botaoLogin);

        // Adiciona listener para Enter no campo de senha
        campoSenha.addActionListener(e -> realizarLogin());
        
        // Adiciona listener para Enter no campo de email para pular para o campo de senha
        campoEmail.addActionListener(e -> campoSenha.requestFocus());
    }
    
    /**
     * Cria o botão de login com cor verde claro.
     * @param texto O texto do botão
     * @return O botão criado
     */
    private JButton criarBotaoLogin(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(Estilos.FONTE_BOTAO);
        botao.setBackground(Estilos.COR_VERDE_CLARO);
        botao.setForeground(Estilos.COR_TEXTO);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover (O efeito de quando o mouse passa por cima)
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
        // Primeiro, verifica se o coordenador precisa ser cadastrado
        if (!getCentral().temCoordenador()) {
            mostrarAviso("Nenhum coordenador encontrado. É necessário cadastrar um administrador primeiro.");
            abrirTelaCadastroCoordenador();
            return;
        }

        String email = campoEmail.getText().trim();
        String senha = new String(campoSenha.getPassword());
        
        // Validação básica
        if (email.isEmpty() || senha.isEmpty()) {
            mostrarErro("Por favor, preencha todos os campos.");
            return;
        }
        
        // Verifica se é coordenador
        Coordenador coordenador = getCentral().getCoordenador();
        if (coordenador.getEmail().equalsIgnoreCase(email) && 
            coordenador.getSenha().equals(senha)) {
            sessao.setUsuarioLogado(coordenador);
            abrirTelaPrincipal();
            return;
        }
        
        // Verifica se é aluno
        if (getCentral().isLoginPermitido(email, senha)) {
            Aluno aluno = getCentral().retornarAlunoPeloEmail(email);
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
        TelaCadastroCoordenador telaCadastro = new TelaCadastroCoordenador(getCentral(), getPersistencia(), getNomeArquivo());
        telaCadastro.inicializar();
        this.dispose();
    }
    
    /**
     * Abre a tela principal após login bem-sucedido.
     */
    private void abrirTelaPrincipal() {
        TelaPrincipal telaPrincipal = new TelaPrincipal(getCentral(), getPersistencia(), getNomeArquivo());
        telaPrincipal.inicializar();
        this.dispose();
    }
}
