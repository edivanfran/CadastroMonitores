package excecoes;

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
}

