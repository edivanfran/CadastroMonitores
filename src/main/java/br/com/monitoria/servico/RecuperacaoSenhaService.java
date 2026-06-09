package br.com.monitoria.servico;

import br.com.monitoria.NotificadorFactory;
import br.com.monitoria.RecuperadorDeSenhas;

public class RecuperacaoSenhaService {

    public void enviarCodigoRecuperacao(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("Por favor, insira seu endereço de e-mail.");
        }

        String codigo = RecuperadorDeSenhas.gerarCodigoRecuperacao(email);

        if (codigo == null) {
            throw new Exception("O e-mail inserido não foi encontrado em nosso sistema.");
        }

        try {
            String assunto = "Código de Recuperação de Senha";
            String mensagem = "Olá,\\n\\nSeu código para redefinição de senha é: " + codigo +
                              "\\n\\nSe você não solicitou isso, por favor, ignore este e-mail.";
            NotificadorFactory.obterInstancia().enviarEmail(email, assunto, mensagem);
        } catch (Exception e) {
            // Re-lança a exceção para ser tratada pela camada de visão
            throw new Exception("Erro ao enviar e-mail de recuperação: " + e.getMessage());
        }
    }

}
