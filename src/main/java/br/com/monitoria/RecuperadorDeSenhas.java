package br.com.monitoria;

import br.com.monitoria.model.Usuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RecuperadorDeSenhas {
    private static transient Map<String, String> codigosRecuperacao = new HashMap<>();

    /**
     * Gera e armazena um código de recuperação para um determinado e-mail.
     * @param email O e-mail para o qual o código será gerado.
     * @return O código de 6 dígitos gerado, ou null se o e-mail nã
     * o for encontrado.
     */
    public static String gerarCodigoRecuperacao(String email) { // ESPECIFICO
        Usuario usuario = GerenciadorDeDados.getInstancia().getUsuarioPorEmail(email);
        if (usuario == null) {
            return null;
        }

        if (codigosRecuperacao == null) {
            codigosRecuperacao = new HashMap<>();
        }

        String codigo = String.format("%06d", new Random().nextInt(999999));
        codigosRecuperacao.put(email.toLowerCase(), codigo);
        return codigo;
    }

    /**
     * Redefine a senha de um usuário usando um código de recuperação.
     * @param email O e-mail do usuário.
     * @param codigo O código de recuperação enviado ao usuário.
     * @param novaSenha A nova senha a ser definida.
     * @return true se a senha foi redefinida com sucesso, false caso contrário.
     */
    public static boolean redefinirSenhaComCodigo(String email, String codigo, String novaSenha) { // ESPECIFICO
        if (codigosRecuperacao == null || !codigosRecuperacao.containsKey(email.toLowerCase())) {
            return false;
        }

        String codigoArmazenado = codigosRecuperacao.get(email.toLowerCase());
        if (codigoArmazenado.equals(codigo)) {
            Usuario usuario = GerenciadorDeDados.getInstancia().getUsuarioPorEmail(email);
            if (usuario != null) {
                try {
                    usuario.setSenha(novaSenha);
                    GerenciadorDeDados.getInstancia().atualizarUsuario(usuario); // Salva a alteração no banco
                    // Invalida o código depois de usar
                    codigosRecuperacao.remove(email.toLowerCase());
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }

}
