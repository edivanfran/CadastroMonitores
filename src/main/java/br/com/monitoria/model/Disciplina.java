package br.com.monitoria.model;

import br.com.monitoria.Vaga;
import br.com.monitoria.excecoes.VagasEsgotadasException;
import br.com.monitoria.interfaces.Prototype;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa uma disciplina em um edital de monitoria, portanto, além de nome, possui como atributos: quantidade de vagas de monitoria que serão remuneradas, quantidade de vagas de monitoria para voluntariado — ambas {@code int} —, uma lista de alunos inscritos para voluntariado e outra para alunos inscritos em vagas com remuneração, e o total de alunos expresso em {@code int}.
 */

@Entity
public class Disciplina implements Prototype {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "nome_disciplina", nullable = false)
    private String nomeDisciplina;

    @Column (name = "vagas_remuneradas", nullable = false)
    private int vagasRemuneradas;

    @Column (name = "vagas_voluntarias", nullable = false)
    private int vagasVoluntarias;

    @ManyToMany
    @JoinTable (name = "disciplina_aluno_voluntario",
                joinColumns = @JoinColumn(name = "disciplina_id"),
                inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    private List<Aluno> alunosVoluntariosInscritos = new ArrayList<>();

    @ManyToMany
    @JoinTable (name = "disciplina_aluno_remunerado",
                joinColumns = @JoinColumn(name = "disciplina_id"),
                inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    private List<Aluno> alunosRemuneradosInscritos = new ArrayList<>();

    @Transient
    private int totalAlunos;

    protected Disciplina() {}

    public Disciplina(String nomeDisciplina, int vagasVoluntarias, int vagasRemuneradas){
        this.nomeDisciplina = nomeDisciplina;
        this.vagasRemuneradas = vagasRemuneradas;
        this.vagasVoluntarias = vagasVoluntarias;
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
    public void setVagasRemuneradas(int vagasRemuneradas) {
        if (vagasRemuneradas < 0) {
            throw new IllegalArgumentException("O número de vagas não pode ser negativo.");
        }
        this.vagasRemuneradas = vagasRemuneradas;
    }
    public int getVagasVoluntarias() {
        return vagasVoluntarias;
    }
    public void setVagasVoluntarias(int vagasVoluntarias) {
        if (vagasVoluntarias < 0) {
            throw new IllegalArgumentException("O número de vagas não pode ser negativo.");
        }
        this.vagasVoluntarias = vagasVoluntarias;
    }
    public List<Aluno> getAlunosVoluntariosInscritos() {
        return this.alunosVoluntariosInscritos;
    }
    public List<Aluno> getAlunosRemuneradosInscritos() {
        return this.alunosRemuneradosInscritos;
    }
    public int getTotalAlunos() {
        return totalAlunos;
    }
    public Long getId() {return id;}


    /**
     * Adiciona um aluno a uma vaga, se houver disponibilidade.
     * @param aluno O aluno que deseja se adicionar
     * @param vaga A modalidade da vaga, podendo ser {@code REMUNERADA} ou {@code VOLUNTARIA}
     * @throws VagasEsgotadasException se não houver mais vagas do tipo solicitado.
     */
    public void adicionarAluno(Aluno aluno, Vaga vaga) throws VagasEsgotadasException {
        if (vaga == Vaga.REMUNERADA) {
            if (alunosRemuneradosInscritos.size() < vagasRemuneradas) {
                alunosRemuneradosInscritos.add(aluno);
                System.out.println("Aluno " + aluno.getNome() + " inscrito em " + nomeDisciplina + " (Remunerada)");
            } else {
                throw new VagasEsgotadasException(nomeDisciplina, vaga);
            }
        } else if (vaga == Vaga.VOLUNTARIA) {
            if (alunosVoluntariosInscritos.size() < vagasVoluntarias) {
                alunosVoluntariosInscritos.add(aluno);
                System.out.println("Aluno " + aluno.getNome() + " inscrito em " + nomeDisciplina + " (Voluntária)");
            } else {
                throw new VagasEsgotadasException(nomeDisciplina, vaga);
            }
        }
    }

    /**
     * Cria uma cópia da disciplina.
     * @return Uma nova instância de Disciplina com os mesmos valores.
     */
    public Disciplina clonar() {
        return new Disciplina(this.nomeDisciplina, this.vagasVoluntarias, this.vagasRemuneradas);
    }

    /**
     * Representação em {@code String} do objeto.
     * @return O nome da disciplina
     */
    public String toString() {
        return nomeDisciplina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disciplina that = (Disciplina) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
