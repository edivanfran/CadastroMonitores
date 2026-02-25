package br.com.monitoria;

import br.com.monitoria.excecoes.ValoresInvalidosException;

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
    private double pontuacaoFinal; // Armazena a pontuação calculada

    /**
     * Construtor da inscrição.
     * @param aluno O aluno que se inscreveu
     * @param disciplina A disciplina na qual o aluno se inscreveu
     * @param cre O CRE do aluno (deve estar entre 0 e 100)
     * @param nota A média do aluno na disciplina (deve estar entre 0 e 100)
     * @param tipoVaga O tipo de vaga (remunerada ou voluntária) que o aluno conseguiu
     * @param ordemPreferencia A ordem de preferência da disciplina para o aluno
     * @param preferenciaVaga A preferência do aluno pelo tipo de vaga
     * @throws ValoresInvalidosException Se o CRE ou a nota estiverem fora do intervalo válido
     */
    public Inscricao(Aluno aluno, Disciplina disciplina, double cre, double nota, Vaga tipoVaga, int ordemPreferencia, PreferenciaInscricao preferenciaVaga) throws ValoresInvalidosException {
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
