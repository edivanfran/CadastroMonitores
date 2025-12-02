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
    private JButton botaoCadastrar;
    private JButton botaoVoltar;
    private CentralDeInformacoes central;
    private Persistencia persistencia;
    private String nomeArquivo;
    
    public TelaCadastroCoordenador(CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Cadastro de Coordenador");
        this.central = central;
        this.persistencia = persistencia;
        this.nomeArquivo = nomeArquivo;
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
    //TODO | Padronizar o símbolo do IFPB em todas as telas e também não colocar a logo só aqui
    private JLabel criarLogo() {
        try {
            // Tenta carregar a imagem do logo
            ImageIcon icon = new ImageIcon("IFPB_icon.jpg");
            if (icon.getIconWidth() > 0) {
                // Redimensiona o logo se necessário
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
     * Cria o formulário de cadastro.
     */
    private void criarFormularioCadastro() {
        int yInicial = 280;
        int alturaCampo = 40;
        int espacamento = 15;
        int larguraLabel = 150;
        int larguraCampo = 300;
        int xLabel = (Estilos.LARGURA_TELA - larguraLabel - larguraCampo - 10) / 2;
        int xCampo = xLabel + larguraLabel + 10;

        // Nome
        JLabel labelNome = criarLabel("Nome:", Estilos.FONTE_NORMAL);
        labelNome.setBounds(xLabel, yInicial, larguraLabel, alturaCampo);
        labelNome.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelNome);
        
        campoNome = criarCampoTexto(25);
        campoNome.setBounds(xCampo, yInicial, larguraCampo, alturaCampo);
        painelPrincipal.add(campoNome);
        
        // Email
        yInicial += alturaCampo + espacamento;
        JLabel labelEmail = criarLabel("E-mail:", Estilos.FONTE_NORMAL);
        labelEmail.setBounds(xLabel, yInicial, larguraLabel, alturaCampo);
        labelEmail.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelEmail);
        
        campoEmail = criarCampoTexto(25);
        campoEmail.setBounds(xCampo, yInicial, larguraCampo, alturaCampo);
        painelPrincipal.add(campoEmail);
        
        // Senha
        yInicial += alturaCampo + espacamento;
        JLabel labelSenha = criarLabel("Senha:", Estilos.FONTE_NORMAL);
        labelSenha.setBounds(xLabel, yInicial, larguraLabel, alturaCampo);
        labelSenha.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelSenha);
        
        campoSenha = new JPasswordField(25);
        campoSenha.setFont(Estilos.FONTE_NORMAL);
        campoSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilos.COR_SECUNDARIA, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        campoSenha.setBounds(xCampo, yInicial, larguraCampo, alturaCampo);
        painelPrincipal.add(campoSenha);
        
        // Confirmar Senha
        yInicial += alturaCampo + espacamento;
        JLabel labelConfirmarSenha = criarLabel("Confirmar Senha:", Estilos.FONTE_NORMAL);
        labelConfirmarSenha.setBounds(xLabel, yInicial, larguraLabel, alturaCampo);
        labelConfirmarSenha.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelConfirmarSenha);
        
        campoConfirmarSenha = new JPasswordField(25);
        campoConfirmarSenha.setFont(Estilos.FONTE_NORMAL);
        campoConfirmarSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilos.COR_SECUNDARIA, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        campoConfirmarSenha.setBounds(xCampo, yInicial, larguraCampo, alturaCampo);
        painelPrincipal.add(campoConfirmarSenha);
        
        // Botões
        yInicial += alturaCampo + espacamento + 20;
        int larguraTotalBotoes = Estilos.LARGURA_BOTAO * 2 + Estilos.ESPACAMENTO;
        int xBotoes = (Estilos.LARGURA_TELA - larguraTotalBotoes) / 2;

        botaoCadastrar = criarBotao("Cadastrar", e -> realizarCadastro());
        botaoCadastrar.setBounds(xBotoes, yInicial, Estilos.LARGURA_BOTAO, Estilos.ALTURA_BOTAO);
        painelPrincipal.add(botaoCadastrar);
        
        botaoVoltar = criarBotaoSecundario("Voltar", e -> voltarParaLogin());
        botaoVoltar.setBounds(xBotoes + Estilos.LARGURA_BOTAO + Estilos.ESPACAMENTO, yInicial, Estilos.LARGURA_BOTAO, Estilos.ALTURA_BOTAO);
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
