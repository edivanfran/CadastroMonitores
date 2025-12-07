import excecoes.ValoresInvalidosException;

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
    private boolean desistiu;
    private double pontuacaoFinal; // Armazena a pontuação calculada

    /**
     * Construtor da inscrição.
     * @param aluno O aluno que se inscreveu
     * @param disciplina A disciplina na qual o aluno se inscreveu
     * @param cre O CRE do aluno (deve estar entre 0 e 10)
     * @param nota A média do aluno na disciplina (deve estar entre 0 e 10)
     * @param tipoVaga O tipo de vaga (remunerada ou voluntária)
     * @throws ValoresInvalidosException Se o CRE ou a nota estiverem fora do intervalo válido
     */
    public Inscricao(Aluno aluno, Disciplina disciplina, double cre, double nota, Vaga tipoVaga) throws ValoresInvalidosException {
        if (cre < 0 || cre > 10) {
            throw new ValoresInvalidosException("CRE", cre, 0, 10);
        }
        if (nota < 0 || nota > 10) {
            throw new ValoresInvalidosException("Nota", nota, 0, 10);
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
        
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.cre = cre;
        this.nota = nota;
        this.tipoVaga = tipoVaga;
        this.desistiu = false;
        // A pontuação final será calculada quando o resultado do edital for processado.
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
     * Calcula e define a pontuação final do aluno usando a fórmula: PONTUAÇÃO = PESO_CRE * CRE + PESO_NOTA * NOTA
     * @param pesoCre O peso do CRE no cálculo
     * @param pesoNota O peso da nota no cálculo
     */
    public void calcularPontuacao(double pesoCre, double pesoNota) {
        this.pontuacaoFinal = (pesoCre * cre) + (pesoNota * nota);
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
