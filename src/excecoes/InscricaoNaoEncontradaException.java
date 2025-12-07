package excecoes;

/**
 * Exceção lançada quando uma inscrição específica não é encontrada no sistema.
 */
public class InscricaoNaoEncontradaException extends Exception {
    
    /**
     * Construtor padrão que define uma mensagem de erro genérica.
     */
    public InscricaoNaoEncontradaException() {
        super("A inscrição especificada não foi encontrada.");
    }

    /**
     * Construtor que permite uma mensagem de erro personalizada.
     * @param mensagem A mensagem de erro específica.
     */
    public InscricaoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
