package br.com.monitoria.model;

import br.com.monitoria.PreferenciaInscricao;
import br.com.monitoria.Vaga;
import br.com.monitoria.excecoes.ValoresInvalidosException;
import br.com.monitoria.servico.ValidadorInscricao;

public class Inscricao {

    private String id;
    private String editalId;
    private String alunoId;
    private String disciplinaNome;
    private double cre;
    private double nota;
    private Vaga tipoVaga;
    private int ordemPreferencia;
    private PreferenciaInscricao preferenciaVaga;
    private boolean desistiu;
    private double pontuacaoFinal;

    // Campos transientes para carregar objetos completos sob demanda
    private transient Aluno aluno;
    private transient Disciplina disciplina;
    private transient EditalDeMonitoria edital;

    // Construtor padrão
    protected Inscricao() {}

    // Construtor para reconstrução a partir do DAO
    public Inscricao(String id, String editalId, String alunoId, String disciplinaNome, double cre, double nota, Vaga tipoVaga, int ordemPreferencia, PreferenciaInscricao preferenciaVaga, boolean desistiu, double pontuacaoFinal) {
        this.id = id;
        this.editalId = editalId;
        this.alunoId = alunoId;
        this.disciplinaNome = disciplinaNome;
        this.cre = cre;
        this.nota = nota;
        this.tipoVaga = tipoVaga;
        this.ordemPreferencia = ordemPreferencia;
        this.preferenciaVaga = preferenciaVaga;
        this.desistiu = desistiu;
        this.pontuacaoFinal = pontuacaoFinal;
    }

    /**
     * Construtor para criar uma nova inscrição na lógica de negócio.
     */
    public Inscricao(Aluno aluno, Disciplina disciplina, EditalDeMonitoria edital, double cre, double nota, Vaga tipoVaga, int ordemPreferencia, PreferenciaInscricao preferenciaVaga) throws ValoresInvalidosException {
        ValidadorInscricao.validar(aluno, disciplina, cre, nota, tipoVaga, preferenciaVaga);
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.edital = edital;
        // Supondo que Aluno e Edital já tenham um ID (String) atribuído
        this.alunoId = String.valueOf(aluno.getId());
        this.editalId = edital.getId();
        this.disciplinaNome = disciplina.getNomeDisciplina();
        this.cre = cre;
        this.nota = nota;
        this.tipoVaga = tipoVaga;
        this.ordemPreferencia = ordemPreferencia;
        this.preferenciaVaga = preferenciaVaga;
        this.desistiu = false;
        this.pontuacaoFinal = 0;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEditalId() { return editalId; }
    public void setEditalId(String editalId) { this.editalId = editalId; }
    public String getAlunoId() { return alunoId; }
    public void setAlunoId(String alunoId) { this.alunoId = alunoId; }
    public String getDisciplinaNome() { return disciplinaNome; }
    public double getCre() { return cre; }
    public double getNota() { return nota; }
    public Vaga getTipoVaga() { return tipoVaga; }
    public void setTipoVaga(Vaga tipoVaga) { this.tipoVaga = tipoVaga; }
    public int getOrdemPreferencia() { return ordemPreferencia; }
    public PreferenciaInscricao getPreferenciaVaga() { return preferenciaVaga; }
    public boolean isDesistiu() { return desistiu; }
    public void setDesistiu(boolean desistiu) { this.desistiu = desistiu; }
    public double getPontuacaoFinal() { return pontuacaoFinal; }
    public void setPontuacaoFinal(double pontuacaoFinal) { this.pontuacaoFinal = pontuacaoFinal; }

    // Getters para os objetos transientes (a lógica de carregamento pode ser adicionada depois)
    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
    public Disciplina getDisciplina() { return disciplina; }
    public void setDisciplina(Disciplina disciplina) { this.disciplina = disciplina; }
    public EditalDeMonitoria getEdital() { return edital; }
    public void setEdital(EditalDeMonitoria edital) { this.edital = edital; }

    // Métodos de negócio que dependem dos objetos carregados
    public String getNomeAluno() { return (aluno != null) ? aluno.getNome() : null; }
    public String getMatriculaAluno() { return (aluno != null) ? aluno.getMatricula() : null; }
    public String getEmailAluno() { return (aluno != null) ? aluno.getEmail() : null; }
}
