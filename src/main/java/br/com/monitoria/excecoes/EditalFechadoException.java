package br.com.monitoria.excecoes;


/**
 * Exceção lançada quando se tenta realizar uma operação em um edital que está fechado.
 */
public class EditalFechadoException extends Exception {
    public EditalFechadoException(String numeroEdital) {
        super("O edital " + numeroEdital + " está fechado. Não é possível realizar esta operação.");
    }
    
    public EditalFechadoException() {
        super("Este edital está fechado. Não é possível realizar esta operação.");
    }

    /**
     * Construtor que permite uma mensagem personalizada.
     * @param numeroEdital O número do edital.
     * @param mensagem A mensagem específica a ser exibida.
     */
    public EditalFechadoException(String numeroEdital, String mensagem) {
        super("Edital " + numeroEdital + ": " + mensagem);
    }
}
