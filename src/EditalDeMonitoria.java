import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Representa editais de monitoria de disciplinas do Curso, registradas em uma central de informações. Possui ID {@code long}, um número {@code String}, uma data de início e uma de limite — ambas {@link LocalDate} —, uma lista de disciplinas que esse edital compreende em seu processo seletivo, e um booleano determinando se o edital se encontra ainda aberto.
 */
public class EditalDeMonitoria {
    private long id;
    private String numero;
    private LocalDate dataInicio;
    private LocalDate dataLimite;
    private ArrayList<Disciplina> disciplinas;
    private boolean aberto;

    public long getId() {
        return id;
    }
    public String getNumero() {
        return numero;
    }
    public LocalDate getDataInicio() {
        return dataInicio;
    }
    public LocalDate getDataLimite() {
        return dataLimite;
    }
    public ArrayList<Disciplina> getDisciplinas() {
        return disciplinas;
    }
    public boolean isAberto() {
        return aberto;
    }
    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public EditalDeMonitoria(String numero, LocalDate dataInicio, LocalDate dataLimite) {
       this.id = System.currentTimeMillis();
       this.numero = numero;
       this.dataInicio = dataInicio;
       this.dataLimite = dataLimite;
       this.disciplinas = new ArrayList<>();
       this.aberto = true;
   }

    /**
     * Adiciona a disciplina especificada à lista de disciplinas que ofertam vagas no edital.
     */
    public void adicionarDisciplina(Disciplina disciplina) {
        disciplinas.add(disciplina);
    }

    /**
     * Primeiro testa se o edital continua aberto – se não, retorna {@code false};
     * <p>continuando aberto, verifica se está dentro do prazo de inscrição no edital – se não, retorna {@code false};</p>
     * <p>estando dentro do prazo, inscreve o aluno na vaga com a modalidade que o aluno deseja – e retorna {@code true}.</p>
     * <p>Caso não encontre a disciplina— retorna {@code false}.</p>
     * @param aluno O aluno que se deseja inscrever no edital
     * @param nomeDisciplina O nome da disciplina do edital a qual aluno deseja concorrer.
     */
   public boolean inscreverAluno(Aluno aluno, String nomeDisciplina) { //TODO| adicionar parâmetro de vaga remunerada ou voluntária
       if (!aberto) { //TODO| é interessante criar exceções específicas ao invés de encadear vários `if`s
           System.out.println("Este edital está fechado. Não é possível inscrever alunos.");
           return false;
       }
       for (Disciplina d : disciplinas) {
           if (d.getNomeDisciplina().equalsIgnoreCase(nomeDisciplina)) {
               if (LocalDate.now().isAfter(dataLimite)) {
                   System.out.println("O prazo para inscrição neste edital já acabou.");
                   return false; //TODO| poderia chamar o `jaAcabou()` aqui
               }
               // TODO: Verificar se o método adicionarAluno precisa de parâmetro Vaga
               // d.adicionarAluno(aluno, Vaga.VOLUNTARIA); // Exemplo
               System.out.println("Aluno " + aluno.getNome() + " inscrito na disciplina " + nomeDisciplina);
               return true;
           }
       }
       System.out.println("Disciplina não encontrada neste edital.");
       return false;
   }

   public boolean jaAcabou() {
       return LocalDate.now().isAfter(dataLimite);
   }

   /**
    * Calcula o resultado do edital (ranqueamento dos alunos).
    * <p>Apenas coordenadores podem executar esta operação.</p>
    * @param coordenador O coordenador que está executando a operação
    * @return {@code true} se o cálculo foi realizado com sucesso, {@code false} caso contrário
    */
   public boolean calcularResultado(Coordenador coordenador) {
       if (coordenador == null) {
           System.out.println("[Erro] Apenas coordenadores podem calcular o resultado do edital.");
           return false;
       }
       // TODO: Implementar lógica de cálculo de resultado/ranqueamento
       System.out.println("Calculando resultado do edital " + numero + "...");
       /* Lógica de cálculo aqui */
       return true;
   }

   /**
    * Fecha o edital, impedindo novas inscrições.
    * <p>Apenas coordenadores podem executar esta operação.</p>
    * @param coordenador O Coordenador que está executando a operação
    * @return {@code true} se o edital foi fechado com sucesso, {@code false} caso contrário
    */
   public boolean fecharEdital(Coordenador coordenador) {
       if (coordenador == null) {
           System.out.println("[Erro] Apenas coordenadores podem fechar o edital.");
           return false;
       }
       if (!aberto) {
           System.out.println("[Aviso] Este edital já está fechado.");
           return false;
       }
       this.aberto = false;
       System.out.println("Edital " + numero + " foi fechado com sucesso.");
       return true;
   }

    /**
     * Representação em {@code String} do objeto.
     * @return Uma listagem dos atributos do edital ({@code numero}, {@code id}, {@code dataInicio}, {@code dataLimite}, {@code disciplinas}, {@code aberto})
     */
   public String toString() {
       return "Edital: " + numero +
               "\nID: " + id +
               "\nData de Início: " + dataInicio +
               "\nData Limite: " + dataLimite +
               "\nDisciplinas cadastradas: " + disciplinas.size() +
               "\nAberto: " + (aberto ? "Sim" : "Não");
       }
}
