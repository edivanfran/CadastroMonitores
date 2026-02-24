package br.com.monitoria;

import br.com.monitoria.excecoes.UsuarioNaoEncontradoException;

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

    public TelaEsqueciSenha(CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Recuperação de Senha", central, persistencia, nomeArquivo);
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

        if (email.isEmpty()) {
            mostrarErro("Por favor, insira seu endereço de e-mail.");
            return;
        }

        String codigo;
        try {
            codigo = getCentral().getAutenticador().gerarCodigoRecuperacao(email, getCentral());
        } catch (UsuarioNaoEncontradoException e) {
            mostrarErro("O e-mail inserido não foi encontrado em nosso sistema.");
            return;
        }

        if (codigo == null) {
            mostrarErro("O e-mail inserido não foi encontrado em nosso sistema.");
            return;
        }

        new Thread(() -> {
            try {
                String assunto = "Código de Recuperação de Senha";
                String mensagem = "Olá,\\n\\nSeu código para redefinição de senha é: " + codigo +
                                  "\\n\\nSe você não solicitou isso, por favor, ignore este e-mail.";
                Mensageiro.enviarEmail(email, assunto, mensagem);

                SwingUtilities.invokeLater(() -> {
                    mostrarSucesso("Um código de recuperação foi enviado para o seu e-mail.");
                    abrirTelaVerificacao(email);
                });

            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> mostrarErro("Erro ao enviar e-mail: " + e.getMessage()));
            }
        }).start();
    }

    private void abrirTelaVerificacao(String email) {
        TelaVerificarCodigo tela = new TelaVerificarCodigo(getCentral(), getPersistencia(), getNomeArquivo(), email);
        tela.inicializar();
        this.dispose();
    }

    private void voltarParaLogin() {
        TelaLogin telaLogin = new TelaLogin(getCentral(), getPersistencia(), getNomeArquivo());
        telaLogin.inicializar();
        this.dispose();
    }
}
