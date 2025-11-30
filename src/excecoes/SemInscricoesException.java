package excecoes;

/**
 * Exceção lançada quando se tenta calcular o resultado de um edital que não possui inscrições.
 */
public class SemInscricoesException extends Exception {
    public SemInscricoesException() {
        super("Não há inscrições para calcular o resultado do edital.");
    }
    
    public SemInscricoesException(String disciplina) {
        super("Não há inscrições para a disciplina '" + disciplina + "'.");
    }
}

