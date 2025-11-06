import java.util.ArrayList;

public class Disciplina {
    private String nomeDisciplina;
    private int vagas;
    private ArrayList<Aluno> alunosInscritos;

    public Disciplina(String nomeDisciplina, int vagas) {
        this.nomeDisciplina = nomeDisciplina;
        this.vagas = vagas;
        this.alunosInscritos = new ArrayList<>();
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void adicionarAluno(Aluno aluno) {
        if (alunosInscritos.size() < vagas) {
            alunosInscritos.add(aluno);
            System.out.println("Aluno " + aluno.getNome() + " inscrito em " + nomeDisciplina);
        } else {
            System.out.println("Não há mais vagas em " + nomeDisciplina);
        }
    }

    public ArrayList<Aluno> getAlunosInscritos() {
        return alunosInscritos;
    }
}