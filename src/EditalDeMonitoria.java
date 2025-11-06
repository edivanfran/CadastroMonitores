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
       for (Disciplina d : disciplinas) {
           if (d.getNomeDisciplina().equalsIgnoreCase(nomeDisciplina)) {
               if (LocalDate.now().isAfter(dataLimite)) {
                   System.out.println("O prazo para inscrição neste edital já acabou.");
                   return false;
               }
               d.adicionarAluno(aluno);
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


   @Override
   public String toString() {
       return "Edital: " + numero +
               "\nID: " + id +
               "\nData de Início: " + dataInicio +
               "\nData Limite: " + dataLimite +
               "\nDisciplinas cadastradas: " + disciplinas.size() +
               "\nAberto: " + (jaAcabou() ? "Não" : "Sim");
       }
}
