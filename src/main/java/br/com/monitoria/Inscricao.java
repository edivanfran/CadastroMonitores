package br.com.monitoria;

import br.com.monitoria.excecoes.ValoresInvalidosException;
import br.com.monitoria.servico.ValidadorInscricao;

/**
 * Representa uma inscrição de um aluno em uma disciplina de um edital de monitoria.
 * Armazena o aluno, a disciplina, o CRE, a nota e o tipo de vaga (remunerada ou voluntária).
 */
public class Inscricao {
    private Aluno aluno;
    private Disciplina disciplina;
    private double cre;
    private double nota;
    private Vaga tipoVaga;
    private int ordemPreferencia;
    private PreferenciaInscricao preferenciaVaga;
    private boolean desistiu;
    private double pontuacaoFinal;

    /**
     * Construtor da inscrição.
     * A validação dos dados é delegada para a classe ValidadorInscricao (SRP/High Cohesion).
     */
    public Inscricao(Aluno aluno, Disciplina disciplina, double cre, double nota, Vaga tipoVaga, int ordemPreferencia, PreferenciaInscricao preferenciaVaga) throws ValoresInvalidosException {
        // Delega a responsabilidade de validação (Pure Fabrication / SRP)
        ValidadorInscricao.validar(aluno, disciplina, cre, nota, tipoVaga, preferenciaVaga);
        
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.cre = cre;
        this.nota = nota;
        this.tipoVaga = tipoVaga;
        this.ordemPreferencia = ordemPreferencia;
        this.preferenciaVaga = preferenciaVaga;
        this.desistiu = false;
        this.pontuacaoFinal = 0; 
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public double getCre() {
        return cre;
    }

    public double getNota() {
        return nota;
    }

    public Vaga getTipoVaga() {
        return tipoVaga;
    }

    public void setTipoVaga(Vaga vaga) {
        this.tipoVaga = vaga;
    }

    public int getOrdemPreferencia() {
        return ordemPreferencia;
    }

    public PreferenciaInscricao getPreferenciaVaga() {
        return preferenciaVaga;
    }

    public boolean isDesistiu() {
        return desistiu;
    }

    public void setDesistiu(boolean desistiu) {
        this.desistiu = desistiu;
    }

    public double getPontuacaoFinal() {
        return pontuacaoFinal;
    }

    /**
     * Define a pontuação final da inscrição.
     * O cálculo deve ser feito externamente por uma ICalculadoraPontuacao.
     * @param pontuacaoFinal O valor calculado.
     */
    public void setPontuacaoFinal(double pontuacaoFinal) {
        this.pontuacaoFinal = pontuacaoFinal;
    }

    /**
     * Retorna o nome do aluno através da referência ao objeto Aluno.
     * @return O nome do aluno
     */
    public String getNomeAluno() {
        return aluno.getNome();
    }

    /**
     * Retorna a matrícula do aluno através da referência ao objeto Aluno.
     * @return A matrícula do aluno
     */
    public String getMatriculaAluno() {
        return aluno.getMatricula();
    }

    /**
     * Retorna o email do aluno através da referência ao objeto Aluno.
     * @return O email do aluno
     */
    public String getEmailAluno() {
        return aluno.getEmail();
    }
}
