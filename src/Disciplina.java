import java.util.ArrayList;

public class Disciplina {
    private String nomeDisciplina;
    private int vagasRemuneradas;
    private int vagasVoluntarias;
    private ArrayList<Aluno> alunosVoluntariasInscritos;
    private ArrayList<Aluno> alunosRemuneradosInscritos;
    private int totalAlunos = alunosVoluntariasInscritos.size() + alunosRemuneradosInscritos.size();
    //vai ter que fazer uma atualização no futuro para setVagas em que vai ter poder alterar para um número acima do atual.

    public Disciplina(String nomeDisciplina, int vagasVoluntarias, int vagasRemuneradas) {
        this.nomeDisciplina = nomeDisciplina;
        this.vagasRemuneradas = vagasRemuneradas;
        this.vagasVoluntarias = vagasVoluntarias;
        this.alunosVoluntariasInscritos = new ArrayList<>();
        this.alunosRemuneradosInscritos = new ArrayList<>();
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void adicionarAluno(Aluno aluno, Vaga vaga) {
        if (vaga == Vaga.REMUNERADA) {
            if (alunosRemuneradosInscritos.size() < vagasRemuneradas) {
                alunosRemuneradosInscritos.add(aluno);
                System.out.println("Aluno " + aluno.getNome() + " inscrito em " + nomeDisciplina);
            } else {
                System.out.println("Não há mais vagas remuneradas em " + nomeDisciplina);
            }
        } else if (vaga == Vaga.VOLUNTARIA) {
            if (alunosVoluntariasInscritos.size() < vagasVoluntarias) {
                alunosVoluntariasInscritos.add(aluno);
                System.out.println("Aluno " + aluno.getNome() + " inscrito em " + nomeDisciplina);
            } else {
                System.out.println("Não há mais vagas voluntarias em " + nomeDisciplina);
            }
        } else {
            System.out.println("Vaga não identificada");
        }
    }

    public int getVagasRemuneradas() {
        return vagasRemuneradas;
    }

    public void setVagasRemuneradas(int vagasRemuneradas) {
        if (this.vagasRemuneradas < vagasRemuneradas) {
            this.vagasRemuneradas = vagasRemuneradas;
            this.totalAlunos = alunosVoluntariasInscritos.size() + alunosRemuneradosInscritos.size();
            // Atualizar a quantidade após mudar redefinir a quantidade.
        }
        // Colocar para lançar um erro no futuro.
    }

    public int getVagasVoluntarias() {
        return vagasVoluntarias;
    }

    public void setVagasVoluntarias(int vagasVoluntarias) {
        if (this.vagasVoluntarias < vagasVoluntarias) {
            this.vagasVoluntarias = vagasVoluntarias;
            this.totalAlunos = alunosVoluntariasInscritos.size() + alunosRemuneradosInscritos.size();
            // Atualizar a quantidade após mudar redefinir a quantidade.
        }
        // Colocar para lançar um erro no futuro.
    }

    public int getTotalAlunos() {
        return totalAlunos;
    }

    public ArrayList<Aluno> getAlunosVoluntariasInscritos() {
        return alunosVoluntariasInscritos;
    }

    public String toString() {
        return nomeDisciplina;
    }
}
