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
        //Email
        JLabel lblEmail = criarLabel("Email:", Estilos.FONTE_NORMAL);
        lblEmail.setBounds(225, 300, 80, 40);
        lblEmail.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(lblEmail);

        campoEmail = criarCampoTexto(25);
        campoEmail.setToolTipText("Insira aqui seu e-mail.");
        campoEmail.setBounds(315, 300, 350, 40);
        painelPrincipal.add(campoEmail);

        //Senha
        JLabel lblSenha = criarLabel("Senha:", Estilos.FONTE_NORMAL);
        lblSenha.setBounds(225, 355, 80, 40);
        lblSenha.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(lblSenha);

        campoSenha = new JPasswordField();
        campoSenha.setToolTipText("Insira aqui sua senha.");
        campoSenha.setFont(Estilos.FONTE_NORMAL);
        campoSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilos.COR_SECUNDARIA, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        campoSenha.setBounds(315, 355, 350, 40);
        painelPrincipal.add(campoSenha);

        // Link para esqueci senha
        criarLinkEsqueciSenha();

        // Botão de Login
        botaoLogin = criarBotaoLogin("Entrar");
        botaoLogin.setBounds(350, 430, Estilos.LARGURA_BOTAO, Estilos.ALTURA_BOTAO);
        painelPrincipal.add(botaoLogin);

        // Link para cadastro de aluno
        criarLinkCadastroAluno();

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
     * Cria um link para esqueci senha.
     */
    private void criarLinkEsqueciSenha() {
        JLabel linkEsqueciSenha = new JLabel("<html><u>Esqueci minha senha</u></html>");
        linkEsqueciSenha.setFont(Estilos.FONTE_PEQUENA);
        linkEsqueciSenha.setForeground(Estilos.COR_PRIMARIA);
        linkEsqueciSenha.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkEsqueciSenha.setHorizontalAlignment(SwingConstants.RIGHT);
        linkEsqueciSenha.setBounds(315, 400, 350, 20);
        
        // Efeito hover
        linkEsqueciSenha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                linkEsqueciSenha.setForeground(Estilos.COR_PRIMARIA.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                linkEsqueciSenha.setForeground(Estilos.COR_PRIMARIA);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirTelaEsqueciSenha();
            }
        });
        
        painelPrincipal.add(linkEsqueciSenha);
    }
    
    /**
     * Cria um link para cadastro de aluno.
     */
    private void criarLinkCadastroAluno() {
        JLabel linkCadastro = new JLabel("<html><u>Não tem conta? Cadastre-se aqui</u></html>");
        linkCadastro.setFont(Estilos.FONTE_PEQUENA);
        linkCadastro.setForeground(Estilos.COR_PRIMARIA);
        linkCadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkCadastro.setHorizontalAlignment(SwingConstants.CENTER);
        linkCadastro.setBounds(350, 485, 200, 25);
        
        // Efeito hover
        linkCadastro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                linkCadastro.setForeground(Estilos.COR_PRIMARIA.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                linkCadastro.setForeground(Estilos.COR_PRIMARIA);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirTelaCadastroAluno();
            }
        });
        
        painelPrincipal.add(linkCadastro);
    }
    
    /**
     * Abre a tela de esqueci senha.
     */
    private void abrirTelaEsqueciSenha() {
        TelaEsqueciSenha telaEsqueciSenha = new TelaEsqueciSenha(getCentral(), getPersistencia(), getNomeArquivo());
        telaEsqueciSenha.inicializar();
        this.dispose();
    }
    
    /**
     * Abre a tela de cadastro de aluno.
     */
    private void abrirTelaCadastroAluno() {
        TelaCadastroAluno telaCadastro = new TelaCadastroAluno(getCentral(), getPersistencia(), getNomeArquivo());
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
