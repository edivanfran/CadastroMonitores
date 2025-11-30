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

    public Inscricao(Aluno aluno, Disciplina disciplina, double cre, double nota, Vaga tipoVaga) {
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.cre = cre;
        this.nota = nota;
        this.tipoVaga = tipoVaga;
        this.desistiu = false;
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

    /**
     * Calcula a pontuação do aluno usando a fórmula: PONTUAÇÃO = PESO_CRE * CRE + PESO_NOTA * NOTA
     * @param pesoCre O peso do CRE no cálculo
     * @param pesoNota O peso da nota no cálculo
     * @return A pontuação calculada
     */
    public double calcularPontuacao(double pesoCre, double pesoNota) {
        return (pesoCre * cre) + (pesoNota * nota);
    }

    /**
     * Retorna o nome do aluno através da referência ao objeto Aluno.
     * Exemplo de como a Inscricao acessa informações do Aluno sem herdar dele.
     * @return O nome do aluno
     */
    public String getNomeAluno() {
        return aluno.getNome(); // Acessa método do Aluno através da referência
    }

    /**
     * Retorna a matrícula do aluno através da referência ao objeto Aluno.
     * @return A matrícula do aluno
     */
    public String getMatriculaAluno() {
        return aluno.getMatricula(); // Acessa atributo do Aluno através da referência
    }

    /**
     * Retorna o email do aluno através da referência ao objeto Aluno.
     * @return O email do aluno
     */
    public String getEmailAluno() {
        return aluno.getEmail(); // Acessa atributo do Aluno através da referência
    }
}

