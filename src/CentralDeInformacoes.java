import java.util.ArrayList;


public class CentralDeInformacoes {
   private ArrayList<Aluno> todosOsAlunos = new ArrayList<Aluno>();


   public boolean adicionarAluno(Aluno outro) {
       for (Aluno algum : todosOsAlunos) {
           if (algum.getMatricula().equals(outro.getMatricula())) {
               return false;
           }
       }
       todosOsAlunos.add(outro);
       return true;
   }


   public Aluno recuperarAluno(String matricula) {
       for (Aluno algum : todosOsAlunos) {
           if (algum.getMatricula().equals(matricula)) {
               return algum;
           }
       }
       return null;
   }


   public ArrayList<Aluno> getTodosOsAlunos(){
       return todosOsAlunos;
   }
   public void setTodosOsAlunos(ArrayList<Aluno> listaAtualizada) {
       this.todosOsAlunos = listaAtualizada;
   }
   public ArrayList<EditalDeMonitoria> getTodosOsEditais() {
       return todosOsEditais;
   }
   public void setTodosOsEditais(ArrayList<EditalDeMonitoria> todosOsEditais) {
       this.todosOsEditais = todosOsEditais;
   }
   private ArrayList<EditalDeMonitoria> todosOsEditais = new ArrayList<EditalDeMonitoria>();


   public EditalDeMonitoria recuperarEdital(long id) {
       for (EditalDeMonitoria edital : todosOsEditais) {
           if (edital.getId() == id) {
               return edital;
           }
       }
       return null;
   }


   public boolean adicionarEdital(EditalDeMonitoria edital) {
       for (EditalDeMonitoria e : todosOsEditais) {
           if (e.getId() == edital.getId()) {
               return false;
           }
       }
       todosOsEditais.add(edital);
       return true;
   }


   public ArrayList<Disciplina> recuperarInscricoesDeUmAlunoEmUmEdital(String matricula, long idEdital) {
       /*pass*/
       return null;
   }
}
