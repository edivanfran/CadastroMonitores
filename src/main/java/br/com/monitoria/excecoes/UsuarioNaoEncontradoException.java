package br.com.monitoria.excecoes;

/**
 * Exceção lançada quando um usuário não é encontrado na central de informações pelo seu e-mail, pois este não está registrado.
 */
public class UsuarioNaoEncontradoException extends Exception {
    public UsuarioNaoEncontradoException() {
        super("Usuário não encontrado.");
    }
}
