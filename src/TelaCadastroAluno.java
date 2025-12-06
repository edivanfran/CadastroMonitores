import javax.swing.*;
import java.awt.*;

/**
 * Tela para o cadastro de um novo aluno no sistema.
 * Utiliza gerenciadores de layout BoxLayout e FlowLayout para organizar os componentes.
 * Permite ao usuário inserir nome, matrícula, e-mail, gênero e senha.
 *
 * @author Seu Nome
 * @version 1.1
 */
public class TelaCadastroAluno extends TelaBase {

    /** Campo de texto para o nome do aluno. */
    private JTextField campoNome;
    /** Campo de texto para a matrícula do aluno. */
    private JTextField campoMatricula;
    /** Campo de texto para o e-mail do aluno. */
    private JTextField campoEmail;
    /** Caixa de seleção para o gênero do aluno. */
    private JComboBox<Sexo> campoSexo;
    /** Campo de senha para a senha do aluno. */
    private JPasswordField campoSenha;
    /** Campo de senha para confirmação da senha. */
    private JPasswordField campoConfirmarSenha;
    /** Botão para confirmar e realizar o cadastro. */
    private JButton botaoCadastrar;
    /** Botão para cancelar a operação e voltar à tela de login. */
    private JButton botaoVoltar;

    /**
     * Construtor da tela de cadastro de aluno.
     *
     * @param central      A instância da CentralDeInformacoes para manipulação dos dados.
     * @param persistencia A instância da Persistencia para salvar os dados.
     * @param nomeArquivo  O nome do arquivo onde os dados são salvos.
     */
    public TelaCadastroAluno(CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Cadastro de Aluno", central, persistencia, nomeArquivo);
    }

    /**
     * Monta a estrutura principal da tela, organizando os componentes.
     * O painel principal é configurado com BoxLayout para empilhamento vertical.
     */
    @Override
    protected void criarComponentes() {
        // Altera o layout do painel principal para BoxLayout vertical
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Adiciona um espaçamento nas bordas

        // 1. Adiciona o cabeçalho
        criarCabecalho();

        // Adiciona um espaçamento vertical
        painelPrincipal.add(Box.createVerticalStrut(20));

        // 2. Adiciona o formulário
        criarFormularioCadastro();

        // Adiciona um espaçamento flexível para empurrar os botões para baixo
        painelPrincipal.add(Box.createVerticalGlue());

        // 3. Adiciona os botões
        criarPainelDeBotoes();
    }

    /**
     * Cria e adiciona o título principal na parte superior da tela.
     */
    private void criarCabecalho() {
        JLabel titulo = criarLabel("Cadastro de Aluno", Estilos.FONTE_TITULO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza no BoxLayout
        painelPrincipal.add(titulo);
    }

    /**
     * Cria e adiciona o painel do formulário contendo todos os campos de entrada de dados.
     */
    private void criarFormularioCadastro() {
        // Painel para o formulário com BoxLayout vertical
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Estilos.COR_FUNDO);

        // Inicializa os componentes
        campoNome = criarCampoTexto(20);
        campoMatricula = criarCampoTexto(20);
        campoEmail = criarCampoTexto(20);
        campoSexo = new JComboBox<>(Sexo.values());
        campoSenha = new JPasswordField(20);
        campoConfirmarSenha = new JPasswordField(20);

        // Adiciona cada linha (label + campo) ao painel do formulário
        formPanel.add(criarLinhaFormulario("Nome:", campoNome));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(criarLinhaFormulario("Matrícula:", campoMatricula));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(criarLinhaFormulario("E-mail:", campoEmail));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(criarLinhaFormulario("Gênero:", campoSexo));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(criarLinhaFormulario("Senha:", campoSenha));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(criarLinhaFormulario("Confirmar Senha:", campoConfirmarSenha));

        // Adiciona o painel do formulário ao painel principal
        painelPrincipal.add(formPanel);
    }

    /**
     * Método auxiliar para criar uma linha do formulário (um painel com FlowLayout).
     * Cada linha contém um rótulo à esquerda e um componente de entrada à direita.
     *
     * @param textoLabel O texto do rótulo.
     * @param componente O componente de entrada (ex: JTextField, JComboBox).
     * @return Um JPanel configurado que representa uma linha do formulário.
     */
    private JPanel criarLinhaFormulario(String textoLabel, JComponent componente) {
        JPanel linhaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        linhaPanel.setBackground(Estilos.COR_FUNDO);

        JLabel label = criarLabel(textoLabel, Estilos.FONTE_NORMAL);
        label.setPreferredSize(new Dimension(120, 30));
        label.setHorizontalAlignment(SwingConstants.RIGHT);

        componente.setPreferredSize(new Dimension(250, 30));
        // Garante que a fonte seja a padrão para os campos de senha
        if (componente instanceof JPasswordField) {
            componente.setFont(Estilos.FONTE_NORMAL);
        }

        linhaPanel.add(label);
        linhaPanel.add(componente);
        
        // Limita a altura máxima para evitar que estique verticalmente
        linhaPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

        return linhaPanel;
    }

    /**
     * Cria e adiciona o painel inferior com os botões de "Cadastrar" e "Voltar".
     */
    private void criarPainelDeBotoes() {
        botaoCadastrar = criarBotao("Cadastrar", e -> realizarCadastro());
        botaoVoltar = criarBotaoSecundario("Voltar", e -> voltarParaLogin());

        // Usa o método da classe base que já cria um painel com FlowLayout
        JPanel painelBotoes = criarPainelBotoes(botaoCadastrar, botaoVoltar);
        
        painelPrincipal.add(painelBotoes);
    }
    
    /**
     * Valida os dados inseridos e realiza o cadastro do aluno.
     * Coleta os dados dos campos, verifica se são válidos, cria um novo objeto Aluno,
     * o adiciona à central de informações e salva os dados.
     */
    private void realizarCadastro() {
        String nome = campoNome.getText().trim();
        String matricula = campoMatricula.getText().trim();
        String email = campoEmail.getText().trim();
        Sexo sexo = (Sexo) campoSexo.getSelectedItem();
        String senha = new String(campoSenha.getPassword());
        String confirmarSenha = new String(campoConfirmarSenha.getPassword());
        
        if (nome.isEmpty() || matricula.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            mostrarErro("Por favor, preencha todos os campos.");
            return;
        }
        
        if (!senha.equals(confirmarSenha)) {
            mostrarErro("As senhas não coincidem.");
            campoSenha.setText("");
            campoConfirmarSenha.setText("");
            campoSenha.requestFocus();
            return;
        }
        
        if (!email.matches("(?i)^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")) {
            mostrarErro("E-mail inválido. Por favor, insira um e-mail válido.");
            campoEmail.requestFocus();
            return;
        }
        
        try {
            Aluno novoAluno = new Aluno(nome, matricula, email, senha, sexo);
            getCentral().adicionarAluno(novoAluno);
            getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
            
            mostrarSucesso("Aluno cadastrado com sucesso! Agora você pode fazer o login.");
            voltarParaLogin();
            
        } catch (Exception e) {
            mostrarErro("Erro ao cadastrar aluno: " + e.getMessage());
        }
    }
    
    /**
     * Fecha a tela de cadastro e retorna para a tela de login.
     */
    private void voltarParaLogin() {
        TelaLogin telaLogin = new TelaLogin(getCentral(), getPersistencia(), getNomeArquivo());
        telaLogin.inicializar();
        this.dispose();
    }
}
