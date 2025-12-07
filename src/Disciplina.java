import excecoes.TentativaInvalidaReduzirVagasException;

import java.util.ArrayList;

/**
 * Representa uma disciplina em um edital de monitoria, portanto, além de nome, possui como atributos: quantidade de vagas de monitoria que serão remuneradas, quantidade de vagas de monitoria para voluntariado — ambas {@code int} —, uma lista de alunos inscritos para voluntariado e outra para alunos inscritos em vagas com remuneração, e o total de alunos expresso em {@code int}.
 */
public class Disciplina {
    private String nomeDisciplina;
    private int vagasRemuneradas;
    private int vagasVoluntarias;
    private ArrayList<Aluno> alunosVoluntariosInscritos;
    private ArrayList<Aluno> alunosRemuneradosInscritos;
    private int totalAlunos;

    public Disciplina(String nomeDisciplina, int vagasVoluntarias, int vagasRemuneradas) {
        this.nomeDisciplina = nomeDisciplina;
        this.vagasRemuneradas = vagasRemuneradas;
        this.vagasVoluntarias = vagasVoluntarias;
        this.alunosVoluntariosInscritos = new ArrayList<>();
        this.alunosRemuneradosInscritos = new ArrayList<>();
    }


    public String getNomeDisciplina() {
        return nomeDisciplina;
    }
    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }
    public int getVagasRemuneradas() {
        return vagasRemuneradas;
    }
    public void setVagasRemuneradas(int vagasRemuneradas) throws TentativaInvalidaReduzirVagasException {
        if (this.vagasRemuneradas < vagasRemuneradas) {
            this.vagasRemuneradas = vagasRemuneradas;
            this.totalAlunos = alunosVoluntariosInscritos.size() + alunosRemuneradosInscritos.size();
        } else {
            throw new TentativaInvalidaReduzirVagasException();
        }
    }
    public int getVagasVoluntarias() {
        return vagasVoluntarias;
    }
    public void setVagasVoluntarias(int vagasVoluntarias) throws TentativaInvalidaReduzirVagasException {
        if (this.vagasVoluntarias < vagasVoluntarias) {
            this.vagasVoluntarias = vagasVoluntarias;
            this.totalAlunos = alunosVoluntariosInscritos.size() + alunosRemuneradosInscritos.size();
        } else  {
            throw new TentativaInvalidaReduzirVagasException();
        }
    }
    public ArrayList<Aluno> getAlunosVoluntariosInscritos() {
        return this.alunosVoluntariosInscritos;
    }
    public ArrayList<Aluno> getAlunosRemuneradosInscritos() {
        return this.alunosRemuneradosInscritos;
    }
    public int getTotalAlunos() {
        return totalAlunos;
    }


    /**
     * Verifica se a quantidade de vagas é inferior ao número de alunos já inscritos para a modalidade de vagas que o aluno deseja.
     * <p>Se sim, então o aluno é adicionado na lista de alunos inscritos, e imprime uma mensagem de êxito; se não, imprime uma mensagem de erro.</p>
     * @param aluno O aluno que deseja se adicionar
     * @param vaga A modalidade da vaga, podendo ser {@code REMUNERADA} ou {@code VOLUNTARIA}
     */
    public void adicionarAluno(Aluno aluno, Vaga vaga) {
        if (vaga == Vaga.REMUNERADA) {
            if (alunosRemuneradosInscritos.size() < vagasRemuneradas) {
                alunosRemuneradosInscritos.add(aluno);
                System.out.println("Aluno " + aluno.getNome() + " inscrito em " + nomeDisciplina);
            } else {
                System.out.println("Não há mais vagas remuneradas em " + nomeDisciplina);
                //TODO| Se não tiver mais vagas disponíveis tem que lançar.
            }
        } else if (vaga == Vaga.VOLUNTARIA) {
            if (alunosVoluntariosInscritos.size() < vagasVoluntarias) {
                alunosVoluntariosInscritos.add(aluno);
                System.out.println("Aluno " + aluno.getNome() + " inscrito em " + nomeDisciplina);
            } else {
                System.out.println("Não há mais vagas voluntarias em " + nomeDisciplina);
                //TODO| Se não tiver mais vagas disponíveis tem que lançar.
            }
        } else {
            System.out.println("Vaga não identificada");
            //TODO| Se não identificou a vaga escolhida retornar uma exceção.
        }
    }

    /**
     * Representação em {@code String} do objeto.
     * @return O nome da disciplina
     */
    public String toString() {
        return nomeDisciplina;
    }
}
