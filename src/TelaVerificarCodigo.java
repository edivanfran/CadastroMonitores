import javax.swing.*;
import java.awt.*;

/**
 * Tela para o usuário inserir o código de recuperação e definir uma nova senha.
 * Utiliza posicionamento absoluto (setLayout(null)).
 */
public class TelaVerificarCodigo extends TelaBase {

    private String email; // E-mail do usuário que está redefinindo a senha
    private JTextField campoCodigo;
    private JPasswordField campoNovaSenha;
    private JPasswordField campoConfirmarSenha;
    private JButton botaoRedefinir;
    private JButton botaoVoltar;

    public TelaVerificarCodigo(CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo, String email) {
        super("Redefinir Senha", central, persistencia, nomeArquivo);
        this.email = email;
    }

    @Override
    protected void criarComponentes() {
        painelPrincipal.setLayout(null);

        // Título
        JLabel titulo = criarLabel("Redefinir Senha", Estilos.FONTE_TITULO);
        titulo.setBounds(0, 120, Estilos.LARGURA_TELA, 30);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        painelPrincipal.add(titulo);

        // Subtítulo
        JLabel subtitulo = criarLabel("Insira o código enviado para " + email, Estilos.FONTE_NORMAL);
        subtitulo.setBounds(0, 160, Estilos.LARGURA_TELA, 30);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        painelPrincipal.add(subtitulo);

        // Formulário
        // Código
        JLabel labelCodigo = criarLabel("Código:", Estilos.FONTE_NORMAL);
        labelCodigo.setBounds(214, 210, 150, 40);
        labelCodigo.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelCodigo);

        campoCodigo = criarCampoTexto(10);
        campoCodigo.setBounds(374, 210, 300, 40);
        painelPrincipal.add(campoCodigo);

        // Nova Senha
        JLabel labelNovaSenha = criarLabel("Nova Senha:", Estilos.FONTE_NORMAL);
        labelNovaSenha.setBounds(214, 265, 150, 40);
        labelNovaSenha.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelNovaSenha);

        campoNovaSenha = new JPasswordField(20);
        campoNovaSenha.setBounds(374, 265, 300, 40);
        painelPrincipal.add(campoNovaSenha);

        // Confirmar Senha
        JLabel labelConfirmar = criarLabel("Confirmar Senha:", Estilos.FONTE_NORMAL);
        labelConfirmar.setBounds(214, 320, 150, 40);
        labelConfirmar.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelConfirmar);

        campoConfirmarSenha = new JPasswordField(20);
        campoConfirmarSenha.setBounds(374, 320, 300, 40);
        painelPrincipal.add(campoConfirmarSenha);

        // Botões
        botaoRedefinir = criarBotao("Redefinir Senha", e -> redefinirSenha());
        botaoRedefinir.setBounds(324, 420, Estilos.LARGURA_BOTAO, Estilos.ALTURA_BOTAO);
        painelPrincipal.add(botaoRedefinir);

        botaoVoltar = criarBotaoSecundario("Voltar", e -> voltarParaLogin());
        botaoVoltar.setBounds(494, 420, Estilos.LARGURA_BOTAO, Estilos.ALTURA_BOTAO);
        painelPrincipal.add(botaoVoltar);
    }

    private void redefinirSenha() {
        String codigo = campoCodigo.getText().trim();
        String novaSenha = new String(campoNovaSenha.getPassword());
        String confirmarSenha = new String(campoConfirmarSenha.getPassword());

        if (codigo.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
            mostrarErro("Por favor, preencha todos os campos.");
            return;
        }

        if (!novaSenha.equals(confirmarSenha)) {
            mostrarErro("As novas senhas não coincidem.");
            return;
        }

        boolean sucesso = getCentral().redefinirSenhaComCodigo(email, codigo, novaSenha);

        if (sucesso) {
            try {
                getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
                mostrarSucesso("Senha redefinida com sucesso! Você já pode fazer o login com a nova senha.");
                voltarParaLogin();
            } catch (Exception e) {
                mostrarErro("Erro ao salvar a nova senha: " + e.getMessage());
            }
        } else {
            mostrarErro("Código de recuperação inválido ou expirado. Tente novamente.");
        }
    }

    private void voltarParaLogin() {
        TelaLogin telaLogin = new TelaLogin(getCentral(), getPersistencia(), getNomeArquivo());
        telaLogin.inicializar();
        this.dispose();
    }
}
