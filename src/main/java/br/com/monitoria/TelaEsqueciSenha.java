package br.com.monitoria;

import br.com.monitoria.servico.RecuperacaoSenhaService;

import javax.swing.*;

/**
 * Tela para o processo de recuperação de senha.
 * O usuário insere seu e-mail para receber um código de recuperação.
 * Utiliza posicionamento absoluto (setLayout(null)).
 */
public class TelaEsqueciSenha extends TelaBase {

    private JTextField campoEmail;
    private JButton botaoEnviar;
    private JButton botaoVoltar;
    private RecuperacaoSenhaService recuperacaoSenhaService;

    public TelaEsqueciSenha() {
        super("Recuperação de Senha");
        this.recuperacaoSenhaService = new RecuperacaoSenhaService();
    }

    @Override
    protected void criarComponentes() {
        painelPrincipal.setLayout(null);

        // Título
        JLabel titulo = criarLabel("Recuperação de Senha", Estilos.FONTE_TITULO);
        titulo.setBounds(0, 150, Estilos.LARGURA_TELA, 30);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        painelPrincipal.add(titulo);

        // Subtítulo
        JLabel subtitulo = criarLabel("Insira seu e-mail para receber o código de recuperação.", Estilos.FONTE_NORMAL);
        subtitulo.setBounds(0, 190, Estilos.LARGURA_TELA, 30);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        painelPrincipal.add(subtitulo);

        // Campo de E-mail
        JLabel labelEmail = criarLabel("E-mail:", Estilos.FONTE_NORMAL);
        labelEmail.setBounds(254, 240, 80, 40);
        labelEmail.setHorizontalAlignment(SwingConstants.RIGHT);
        painelPrincipal.add(labelEmail);

        campoEmail = criarCampoTexto(25);
        campoEmail.setBounds(344, 240, 300, 40);
        painelPrincipal.add(campoEmail);

        // Botões
        botaoEnviar = criarBotao("Enviar", e -> enviarCodigo());
        botaoEnviar.setBounds(324, 340, Estilos.LARGURA_BOTAO, Estilos.ALTURA_BOTAO);
        painelPrincipal.add(botaoEnviar);
        
        botaoVoltar = criarBotaoSecundario("Voltar", e -> voltarParaLogin());
        botaoVoltar.setBounds(494, 340, Estilos.LARGURA_BOTAO, Estilos.ALTURA_BOTAO);
        painelPrincipal.add(botaoVoltar);
    }

    private void enviarCodigo() {
        String email = campoEmail.getText().trim();

        // Desativa o botão para evitar múltiplos cliques
        botaoEnviar.setEnabled(false);
        botaoEnviar.setText("Enviando...");

        new Thread(() -> {
            try {
                recuperacaoSenhaService.enviarCodigoRecuperacao(email);
                SwingUtilities.invokeLater(() -> {
                    mostrarSucesso("Um código de recuperação foi enviado para o seu e-mail.");
                    abrirTelaVerificacao(email);
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> mostrarErro(e.getMessage()));
            } finally {
                SwingUtilities.invokeLater(() -> {
                    botaoEnviar.setEnabled(true);
                    botaoEnviar.setText("Enviar");
                });
            }
        }).start();
    }

    private void abrirTelaVerificacao(String email) {
        TelaVerificarCodigo tela = new TelaVerificarCodigo(email);
        tela.inicializar();
        this.dispose();
    }

    private void voltarParaLogin() {
        TelaLogin telaLogin = new TelaLogin();
        telaLogin.inicializar();
        this.dispose();
    }
}
