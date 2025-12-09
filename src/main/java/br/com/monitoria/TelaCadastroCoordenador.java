package br.com.monitoria;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Tela de cadastro do coordenador.
 * Exibida quando não há coordenador cadastrado no sistema.
 */
public class TelaCadastroCoordenador extends TelaBase {
    
    private JTextField campoNome;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JPasswordField campoConfirmarSenha;
    private JButton botaoCadastrar;
    private JButton botaoVoltar;

    public TelaCadastroCoordenador(CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Cadastro de Coordenador",
                central,
                persistencia,
                nomeArquivo);
    }

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
        // Logo (se existir)
        JLabel logo = criarLogo();
        if (logo != null) {
            logo.setBounds(375, 30, 150, 150); // Centralizado horizontalmente
            painelPrincipal.add(logo);
        }
        
        // Título
        JLabel titulo = criarLabel("Cadastro do Coordenador", Estilos.FONTE_TITULO);
        titulo.setBounds(0, 200, Estilos.LARGURA_TELA, 30);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        painelPrincipal.add(titulo);
        
        // Subtítulo
        JLabel subtitulo = criarLabel("O coordenador atua como administrador do sistema", Estilos.FONTE_PEQUENA);
        subtitulo.setBounds(0, 230, Estilos.LARGURA_TELA, 20);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        subtitulo.setForeground(Estilos.COR_SECUNDARIA);
        painelPrincipal.add(subtitulo);
    }
    
    /**
     * Cria o logo da aplicação.
     * @return JLabel com o logo, ou null se não houver imagem
     */
    private JLabel criarLogo() {
        try {
            // Tenta carregar a imagem do logo a partir do classpath
            URL resource = getClass().getResource("/IFPB_icon.jpg");
            if (resource != null) {
                ImageIcon icon = new ImageIcon(resource);
                // Redimensiona o logo se necessário
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImg);
                
                return new JLabel(icon);
            }
        } catch (Exception e) {
            // Se ocorrer um erro, continua sem logo, mas imprime o erro para depuração
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Cria o formulário de cadastro.
     */
    private void criarFormularioCadastro() {
        // Nome
        JLabel labelNome = criarLabel("Nome:", Estilos.FONTE_NORMAL);
        labelNome.setBounds(220, 280, 150, 40);
        labelNome.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelNome);
        
        campoNome = criarCampoTexto(25);
        campoNome.setBounds(380, 280, 300, 40);
        painelPrincipal.add(campoNome);
        
        // Email
        JLabel labelEmail = criarLabel("E-mail:", Estilos.FONTE_NORMAL);
        labelEmail.setBounds(220, 335, 150, 40);
        labelEmail.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelEmail);
        
        campoEmail = criarCampoTexto(25);
        campoEmail.setBounds(380, 335, 300, 40);
        painelPrincipal.add(campoEmail);
        
        // Senha
        JLabel labelSenha = criarLabel("Senha:", Estilos.FONTE_NORMAL);
        labelSenha.setBounds(220, 390, 150, 40);
        labelSenha.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelSenha);
        
        campoSenha = new JPasswordField(25);
        campoSenha.setFont(Estilos.FONTE_NORMAL);
        campoSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilos.COR_SECUNDARIA, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        campoSenha.setBounds(380, 390, 300, 40);
        painelPrincipal.add(campoSenha);
        
        // Confirmar Senha
        JLabel labelConfirmarSenha = criarLabel("Confirmar Senha:", Estilos.FONTE_NORMAL);
        labelConfirmarSenha.setBounds(220, 445, 150, 40);
        labelConfirmarSenha.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelConfirmarSenha);
        
        campoConfirmarSenha = new JPasswordField(25);
        campoConfirmarSenha.setFont(Estilos.FONTE_NORMAL);
        campoConfirmarSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilos.COR_SECUNDARIA, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        campoConfirmarSenha.setBounds(380, 445, 300, 40);
        painelPrincipal.add(campoConfirmarSenha);
        
        // Botões
        botaoCadastrar = criarBotao("Cadastrar", e -> realizarCadastro());
        botaoCadastrar.setBounds(320, 510, Estilos.LARGURA_BOTAO, Estilos.ALTURA_BOTAO);
        painelPrincipal.add(botaoCadastrar);
        
        botaoVoltar = criarBotaoSecundario("Voltar", e -> voltarParaLogin());
        botaoVoltar.setBounds(535, 510, Estilos.LARGURA_BOTAO, Estilos.ALTURA_BOTAO);
        painelPrincipal.add(botaoVoltar);
        
        // Adiciona listener para Enter no campo de confirmar senha
        campoConfirmarSenha.addActionListener(e -> realizarCadastro());
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
            getCentral().cadastrarCoordenador(email, senha, nome);
            getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
            
            // Define o coordenador como usuário logado
            Coordenador coordenador = getCentral().getCoordenador();
            sessao.setUsuarioLogado(coordenador);
            
            mostrarSucesso("Coordenador cadastrado com sucesso!");
            
            // Abre a tela principal
            TelaPrincipal telaPrincipal = new TelaPrincipal(getCentral(), getPersistencia(), getNomeArquivo());
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
        TelaLogin telaLogin = new TelaLogin(getCentral(), getPersistencia(), getNomeArquivo());
        telaLogin.inicializar();
        this.dispose();
    }
}
