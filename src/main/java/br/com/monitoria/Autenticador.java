package br.com.monitoria;
import br.com.monitoria.excecoes.UsuarioNaoEncontradoException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Autenticador {
    private CentralDeInformacoes central;
    private transient Map<String, String> codigosRecuperacao = new HashMap<>();

    /**
     * Gera e armazena um código de recuperação para um determinado e-mail.
     * @param email O e-mail para o qual o código será gerado.
     * @return O código de 6 dígitos gerado, ou {@code null} se o e-mail não for encontrado.
     */
    public String gerarCodigoRecuperacao(String email, CentralDeInformacoes central) throws UsuarioNaoEncontradoException {
        Usuario usuario = central.getUsuarioPorEmail(email);
        if (usuario == null) {
            throw new UsuarioNaoEncontradoException();
        }

        String codigo = String.format("%06d", new Random().nextInt(999999));
        codigosRecuperacao.put(email.toLowerCase(), codigo);
        return codigo;
    }

    public Autenticador(CentralDeInformacoes central){
        this.central = central;
    }

    /**
     * Redefine a senha de um usuário usando um código de recuperação.
     * @param email O e-mail do usuário.
     * @param codigo O código de recuperação enviado ao usuário.
     * @param novaSenha A nova senha a ser definida.
     * @return true se a senha foi redefinida com sucesso, false caso contrário.
     */
    public boolean redefinirSenhaComCodigo(String email, String codigo, String novaSenha, CentralDeInformacoes central) {
        if (codigosRecuperacao == null || !codigosRecuperacao.containsKey(email.toLowerCase())) {
            return false; // Nenhum código foi gerado para esse e-mail
        }

        String codigoArmazenado = codigosRecuperacao.get(email.toLowerCase());
        if (codigoArmazenado.equals(codigo)) {
            Usuario usuario = central.getUsuarioPorEmail(email);
            if (usuario != null) {
                usuario.setSenha(novaSenha);
                // Invalida o código depois de usar
                codigosRecuperacao.remove(email.toLowerCase());
                return true;
            }
        }
        return false;  // TODO: CRIAR EXCEÇÃO
    }

    /**
     * Verifica se as credenciais informadas pelo usuário correspondem a um login válido.
     * @param email O endereço de e-mail do usuário supostamente cadastrado
     * @param senha A senha do usuário
     * @return {@code true} se autorizado, {@code false} caso contrário
     */
    public boolean isLoginPermitido(String email, String senha, CentralDeInformacoes central) {
        Usuario usuario = central.getUsuarioPorEmail(email);
        return usuario != null && usuario.getSenha().equals(senha);
    }
}
