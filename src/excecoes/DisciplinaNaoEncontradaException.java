package excecoes;

/**
 * Exceção lançada quando uma disciplina não é encontrada em um edital.
 */
public class DisciplinaNaoEncontradaException extends Exception {
    public DisciplinaNaoEncontradaException(String nomeDisciplina) {
        super("Disciplina '" + nomeDisciplina + "' não encontrada neste edital.");
    }
    
    public DisciplinaNaoEncontradaException() {
        super("Disciplina não encontrada neste edital.");
    }
}

