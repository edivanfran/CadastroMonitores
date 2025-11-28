import java.time.LocalDate;
import java.util.ArrayList;


public class EditalDeMonitoria {
   private long id;
   private String numero;
   private LocalDate dataInicio;
   private LocalDate dataLimite;
   private ArrayList<Disciplina> disciplinas;
   private boolean aberto;


   public EditalDeMonitoria(long id, String numero, LocalDate dataInicio, LocalDate dataLimite) {
       this.id = System.currentTimeMillis();
       this.numero = numero;
       this.dataInicio = dataInicio;
       this.dataLimite = dataLimite;
       this.disciplinas = new ArrayList<>();
       this.aberto = true;
   }


   public long getId() {
       return id;
   }
   public String getNumero() {
       return numero;
   }
   public ArrayList<Disciplina> getDisciplinas() {
       return disciplinas;
   }


   public void adicionarDisciplina(Disciplina d) {
       disciplinas.add(d);
   }


   public boolean inscreverAluno(Aluno aluno, String nomeDisciplina) {
       if (!aberto) {
           System.out.println("Este edital está fechado. Não é possível inscrever alunos.");
           return false;
       }
       for (Disciplina d : disciplinas) {
           if (d.getNomeDisciplina().equalsIgnoreCase(nomeDisciplina)) {
               if (LocalDate.now().isAfter(dataLimite)) {
                   System.out.println("O prazo para inscrição neste edital já acabou.");
                   return false;
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

   public boolean isAberto() {
       return aberto;
   }

   public void setAberto(boolean aberto) {
       this.aberto = aberto;
   }

   public LocalDate getDataInicio() {
       return dataInicio;
   }

   public LocalDate getDataLimite() {
       return dataLimite;
   }

   /**
    * Calcula o resultado do edital (ranqueamento dos alunos).
    * Apenas coordenadores podem executar esta operação.
    * @param coordenador O coordenador que está executando a operação
    * @return true se o cálculo foi realizado com sucesso, false caso contrário
    */
   public boolean calcularResultado(Coordenador coordenador) {
       if (coordenador == null) {
           System.out.println("[Erro] Apenas coordenadores podem calcular o resultado do edital.");
           return false;
       }
       // TODO: Implementar lógica de cálculo de resultado/ranqueamento
       System.out.println("Calculando resultado do edital " + numero + "...");
       // Lógica de cálculo aqui
       return true;
   }

   /**
    * Fecha o edital, impedindo novas inscrições.
    * Apenas coordenadores podem executar esta operação.
    * @param coordenador O coordenador que está executando a operação
    * @return true se o edital foi fechado com sucesso, false caso contrário
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

   @Override
   public String toString() {
       return "Edital: " + numero +
               "\nID: " + id +
               "\nData de Início: " + dataInicio +
               "\nData Limite: " + dataLimite +
               "\nDisciplinas cadastradas: " + disciplinas.size() +
               "\nAberto: " + (aberto ? "Sim" : "Não");
       }
}
