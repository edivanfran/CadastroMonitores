package br.com.monitoria;

import br.com.monitoria.excecoes.*;

public interface InscricaoService {

    void inscreverAluno(EditalDeMonitoria edital, Aluno aluno, String nomeDisciplina, double cre, double nota, Vaga tipoVaga, int ordemPreferencia, PreferenciaInscricao preferenciaVaga)
            throws EditalFechadoException, PrazoInscricaoVencidoException, DisciplinaNaoEncontradaException, ValoresInvalidosException, VagasEsgotadasException;

    void processarDesistencia(EditalDeMonitoria edital, Aluno aluno, Disciplina disciplina)
            throws InscricaoNaoEncontradaException, EditalFechadoException;

}
