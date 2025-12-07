import javax.swing.*;
import java.awt.*;

/**
 * Tela para o cadastro de um novo aluno no sistema.
 * Utiliza posicionamento absoluto (setLayout(null)) para organizar os componentes.
 * Permite ao usuário inserir nome, matrícula, e-mail, gênero e senha.
 *
 * @author Seu Nome
 * @version 1.3
 */
public class TelaCadastroAluno extends TelaBase {

    private JTextField campoNome;
    private JTextField campoMatricula;
    private JTextField campoEmail;
    private JComboBox<Sexo> campoSexo;
    private JPasswordField campoSenha;
    private JPasswordField campoConfirmarSenha;
    private JButton botaoCadastrar;
    private JButton botaoVoltar;

    public TelaCadastroAluno(CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Cadastro de Aluno", central, persistencia, nomeArquivo);
    }

    @Override
    protected void criarComponentes() {
        painelPrincipal.setLayout(null);

        // Título
        JLabel titulo = criarLabel("Cadastro de Aluno", Estilos.FONTE_TITULO);
        titulo.setBounds(0, 50, Estilos.LARGURA_TELA, 30);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        painelPrincipal.add(titulo);

        // Campos e Rótulos
        criarFormulario();

        // Botões
        criarBotoes();
    }

    private void criarFormulario() {
        // Nome
        JLabel labelNome = criarLabel("Nome:", Estilos.FONTE_NORMAL);
        labelNome.setBounds(214, 120, 150, 40);
        labelNome.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelNome);
        
        campoNome = criarCampoTexto(25);
        campoNome.setBounds(374, 120, 300, 40);
        painelPrincipal.add(campoNome);

        // Matrícula
        JLabel labelMatricula = criarLabel("Matrícula:", Estilos.FONTE_NORMAL);
        labelMatricula.setBounds(214, 175, 150, 40);
        labelMatricula.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelMatricula);

        campoMatricula = criarCampoTexto(25);
        campoMatricula.setBounds(374, 175, 300, 40);
        painelPrincipal.add(campoMatricula);
        
        // Email
        JLabel labelEmail = criarLabel("E-mail:", Estilos.FONTE_NORMAL);
        labelEmail.setBounds(214, 230, 150, 40);
        labelEmail.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelEmail);
        
        campoEmail = criarCampoTexto(25);
        campoEmail.setBounds(374, 230, 300, 40);
        painelPrincipal.add(campoEmail);

        // Sexo
        JLabel labelSexo = criarLabel("Gênero:", Estilos.FONTE_NORMAL);
        labelSexo.setBounds(214, 285, 150, 40);
        labelSexo.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelSexo);

        campoSexo = new JComboBox<>(Sexo.values());
        campoSexo.setBounds(374, 285, 300, 40);
        campoSexo.setFont(Estilos.FONTE_NORMAL);
        painelPrincipal.add(campoSexo);
        
        // Senha
        JLabel labelSenha = criarLabel("Senha:", Estilos.FONTE_NORMAL);
        labelSenha.setBounds(214, 340, 150, 40);
        labelSenha.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelSenha);
        
        campoSenha = new JPasswordField(25);
        campoSenha.setBounds(374, 340, 300, 40);
        painelPrincipal.add(campoSenha);
        
        // Confirmar Senha
        JLabel labelConfirmarSenha = criarLabel("Confirmar Senha:", Estilos.FONTE_NORMAL);
        labelConfirmarSenha.setBounds(214, 395, 150, 40);
        labelConfirmarSenha.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelConfirmarSenha);
        
        campoConfirmarSenha = new JPasswordField(25);
        campoConfirmarSenha.setBounds(374, 395, 300, 40);
        painelPrincipal.add(campoConfirmarSenha);
    }

    private void criarBotoes() {
        botaoCadastrar = criarBotao("Cadastrar", e -> realizarCadastro());
        botaoCadastrar.setBounds(324, 475, Estilos.LARGURA_BOTAO, Estilos.ALTURA_BOTAO);
        painelPrincipal.add(botaoCadastrar);
        
        botaoVoltar = criarBotaoSecundario("Voltar", e -> voltarParaLogin());
        botaoVoltar.setBounds(494, 475, Estilos.LARGURA_BOTAO, Estilos.ALTURA_BOTAO);
        painelPrincipal.add(botaoVoltar);
        
        campoConfirmarSenha.addActionListener(e -> realizarCadastro());
    }
    
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
            Aluno novoAluno = new Aluno(email, senha, nome, matricula, sexo);
            getCentral().adicionarAluno(novoAluno);
            getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
            
            mostrarSucesso("Aluno cadastrado com sucesso! Agora você pode fazer o login.");
            voltarParaLogin();
            
        } catch (Exception e) {
            mostrarErro("Erro ao cadastrar aluno: " + e.getMessage());
        }
    }
    
    private void voltarParaLogin() {
        TelaLogin telaLogin = new TelaLogin(getCentral(), getPersistencia(), getNomeArquivo());
        telaLogin.inicializar();
        this.dispose();
    }
}
