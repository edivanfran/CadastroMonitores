package br.com.monitoria.excecoes;


/**
 * Exceção lançada quando um usuário tenta realizar uma operação para a qual não tem permissão.
 */
public class PermissaoNegadaException extends Exception {
    public PermissaoNegadaException(String operacao) {
        super("Apenas coordenadores podem " + operacao + ".");
    }
    
    public PermissaoNegadaException() {
        super("Você não tem permissão para realizar esta operação. Apenas coordenadores podem executá-la.");
    }
}
