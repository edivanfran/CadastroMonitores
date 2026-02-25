package br.com.monitoria.servico;

import br.com.monitoria.Aluno;
import br.com.monitoria.Disciplina;
import br.com.monitoria.PreferenciaInscricao;
import br.com.monitoria.Vaga;
import br.com.monitoria.excecoes.ValoresInvalidosException;

/**
 * Classe responsável exclusivamente pela validação dos dados de uma inscrição.
 * Aplica o princípio SRP (Single Responsibility Principle) e High Cohesion (GRASP).
 */
public class ValidadorInscricao {

    public static void validar(Aluno aluno, Disciplina disciplina, double cre, double nota, Vaga tipoVaga, PreferenciaInscricao preferenciaVaga) throws ValoresInvalidosException {
        if (cre < 0 || cre > 100) {
            throw new ValoresInvalidosException("CRE", cre, 0, 100);
        }
        if (nota < 0 || nota > 100) {
            throw new ValoresInvalidosException("Nota", nota, 0, 100);
        }
        if (aluno == null) {
            throw new IllegalArgumentException("O aluno não pode ser nulo.");
        }
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina não pode ser nula.");
        }
        if (tipoVaga == null) {
            throw new IllegalArgumentException("O tipo de vaga não pode ser nulo.");
        }
        if (preferenciaVaga == null) {
            throw new IllegalArgumentException("A preferência de vaga não pode ser nula.");
        }
    }
}
