import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private double pesoCre;
    private double pesoNota;
    private ArrayList<Inscricao> inscricoes;
    private Map<String, ArrayList<Inscricao>> ranquePorDisciplina; // Nome da disciplina -> Lista ordenada de inscrições
    private boolean resultadoCalculado;

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

    /**
     * Construtor completo do edital com pesos para cálculo de pontuação.
     * <p>Os pesos devem obrigatoriamente somar 1.0.</p>
     * @param numero O número do edital
     * @param dataInicio A data de início das inscrições
     * @param dataLimite A data limite das inscrições
     * @param pesoCre O peso do CRE no cálculo da pontuação
     * @param pesoNota O peso da nota (média na disciplina) no cálculo da pontuação
     * @throws IllegalArgumentException Se a soma dos pesos não for igual a 1.0
     */
    public EditalDeMonitoria(String numero, LocalDate dataInicio, LocalDate dataLimite, double pesoCre, double pesoNota) {
       // Valida que os pesos somem 1
       if (Math.abs((pesoCre + pesoNota) - 1.0) > 0.0001) { // Usa epsilon para comparação de double
           throw new IllegalArgumentException("A soma dos pesos (CRE + Nota) deve ser igual a 1.0. Valores fornecidos: CRE=" + pesoCre + ", Nota=" + pesoNota);
       }
       this.id = System.currentTimeMillis();
       this.numero = numero;
       this.dataInicio = dataInicio;
       this.dataLimite = dataLimite;
       this.disciplinas = new ArrayList<>();
       this.aberto = true;
       this.pesoCre = pesoCre;
       this.pesoNota = pesoNota;
       this.inscricoes = new ArrayList<>();
       this.ranquePorDisciplina = new HashMap<>();
       this.resultadoCalculado = false;
   }

    /**
     * Construtor do edital com valores padrão para os pesos (0.5 cada).
     * @param numero O número do edital
     * @param dataInicio A data de início das inscrições
     * @param dataLimite A data limite das inscrições
     */
    public EditalDeMonitoria(String numero, LocalDate dataInicio, LocalDate dataLimite) {
        this(numero, dataInicio, dataLimite, 0.5, 0.5);
    }

    public double getPesoCre() {
        return pesoCre;
    }

    public void setPesoCre(double pesoCre) {
        this.pesoCre = pesoCre;
    }

    public double getPesoNota() {
        return pesoNota;
    }

    public void setPesoNota(double pesoNota) {
        this.pesoNota = pesoNota;
    }

    public ArrayList<Inscricao> getInscricoes() {
        return inscricoes;
    }

    public Map<String, ArrayList<Inscricao>> getRanquePorDisciplina() {
        return ranquePorDisciplina;
    }

    public boolean isResultadoCalculado() {
        return resultadoCalculado;
    }

    /**
     * Adiciona uma inscrição ao edital.
     * @param inscricao A inscrição a ser adicionada
     */
    public void adicionarInscricao(Inscricao inscricao) {
        inscricoes.add(inscricao);
    }

    /**
     * Adiciona a disciplina especificada à lista de disciplinas que ofertam vagas no edital.
     */
    public void adicionarDisciplina(Disciplina disciplina) {
        disciplinas.add(disciplina);
    }

    /**
     * Inscreve um aluno em uma disciplina do edital.
     * <p>Primeiro testa se o edital continua aberto – se não, retorna {@code false};</p>
     * <p>continuando aberto, verifica se está dentro do prazo de inscrição no edital – se não, retorna {@code false};</p>
     * <p>estando dentro do prazo, cria uma inscrição com o CRE e a nota (média na disciplina) fornecidos pelo aluno.</p>
     * <p>Caso não encontre a disciplina— retorna {@code false}.</p>
     * @param aluno O aluno que se deseja inscrever no edital
     * @param nomeDisciplina O nome da disciplina do edital a qual aluno deseja concorrer
     * @param cre O CRE do aluno
     * @param nota A média do aluno na disciplina específica
     * @param tipoVaga O tipo de vaga (remunerada ou voluntária)
     * @return {@code true} se a inscrição foi realizada com sucesso, {@code false} caso contrário
     */
   public boolean inscreverAluno(Aluno aluno, String nomeDisciplina, double cre, double nota, Vaga tipoVaga) {
       if (!aberto) {
           System.out.println("Este edital está fechado. Não é possível inscrever alunos.");
           return false;
       }
       if (LocalDate.now().isAfter(dataLimite)) {
           System.out.println("O prazo para inscrição neste edital já acabou.");
           return false;
       }
       for (Disciplina d : disciplinas) {
           if (d.getNomeDisciplina().equalsIgnoreCase(nomeDisciplina)) {
               // Cria a inscrição
               Inscricao inscricao = new Inscricao(aluno, d, cre, nota, tipoVaga);
               inscricoes.add(inscricao);
               
               // Adiciona o aluno na lista da disciplina (para compatibilidade com código existente)
               d.adicionarAluno(aluno, tipoVaga);
               
               System.out.println("Aluno " + aluno.getNome() + " inscrito na disciplina " + nomeDisciplina + 
                       " (CRE: " + cre + ", Nota: " + nota + ", Vaga: " + tipoVaga + ")");
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
    * <p>Para cada disciplina, gera um ranque ordenado pela pontuação obtida usando a fórmula:</p>
    * <p>PONTUAÇÃO = PESO_CRE * CRE_ALUNO + PESO_NOTA * NOTA_ALUNO</p>
    * <p>Apenas coordenadores podem executar esta operação.</p>
    * @param coordenador O coordenador que está executando a operação
    * @return {@code true} se o cálculo foi realizado com sucesso, {@code false} caso contrário
    */
   public boolean calcularResultado(Coordenador coordenador) {
       if (coordenador == null) {
           System.out.println("[Erro] Apenas coordenadores podem calcular o resultado do edital.");
           return false;
       }

       if (inscricoes.isEmpty()) {
           System.out.println("[Aviso] Não há inscrições para calcular o resultado.");
           return false;
       }

       System.out.println("Calculando resultado do edital " + numero + "...");
       ranquePorDisciplina.clear();

       // Agrupa inscrições por disciplina
       Map<String, ArrayList<Inscricao>> inscricoesPorDisciplina = new HashMap<>();
       for (Inscricao inscricao : inscricoes) {
           if (inscricao.isDesistiu()) {
               continue; // Ignora inscrições desistidas
           }
           String nomeDisciplina = inscricao.getDisciplina().getNomeDisciplina();
           inscricoesPorDisciplina.putIfAbsent(nomeDisciplina, new ArrayList<>());
           inscricoesPorDisciplina.get(nomeDisciplina).add(inscricao);
       }

       // Calcula pontuação e ordena por disciplina
       for (Map.Entry<String, ArrayList<Inscricao>> entry : inscricoesPorDisciplina.entrySet()) {
           String nomeDisciplina = entry.getKey();
           ArrayList<Inscricao> inscricoesDisciplina = entry.getValue();

           // Calcula a pontuação para cada inscrição
           for (Inscricao inscricao : inscricoesDisciplina) {
               double pontuacao = inscricao.calcularPontuacao(pesoCre, pesoNota);
               // Armazena a pontuação temporariamente (poderia adicionar um campo em Inscricao)
           }

           // Ordena as inscrições por pontuação (decrescente)
           inscricoesDisciplina.sort((i1, i2) -> {
               double pontuacao1 = i1.calcularPontuacao(pesoCre, pesoNota);
               double pontuacao2 = i2.calcularPontuacao(pesoCre, pesoNota);
               return Double.compare(pontuacao2, pontuacao1); // Ordem decrescente
           });

           ranquePorDisciplina.put(nomeDisciplina, inscricoesDisciplina);
       }

       resultadoCalculado = true;
       System.out.println("Resultado calculado com sucesso para " + ranquePorDisciplina.size() + " disciplina(s).");
       return true;
   }

   /**
    * Retorna o ranque de uma disciplina específica.
    * @param nomeDisciplina O nome da disciplina
    * @return Lista ordenada de inscrições (ranque), ou {@code null} se a disciplina não tiver ranque calculado
    */
   public ArrayList<Inscricao> getRanqueDisciplina(String nomeDisciplina) {
       return ranquePorDisciplina.get(nomeDisciplina);
   }

   /**
    * Retorna uma representação em String do ranque de uma disciplina.
    * @param nomeDisciplina O nome da disciplina
    * @return String formatada com o ranque, ou mensagem de erro se não houver ranque calculado
    */
   public String exibirRanqueDisciplina(String nomeDisciplina) {
       if (!resultadoCalculado) {
           return "[Erro] O resultado do edital ainda não foi calculado.";
       }

       ArrayList<Inscricao> ranque = ranquePorDisciplina.get(nomeDisciplina);
       if (ranque == null || ranque.isEmpty()) {
           return "[Aviso] Não há inscrições para a disciplina " + nomeDisciplina + ".";
       }

       StringBuilder sb = new StringBuilder();
       sb.append("=== RANQUE - ").append(nomeDisciplina).append(" ===\n");
       sb.append(String.format("%-5s %-30s %-10s %-10s %-10s\n", "Pos.", "Aluno", "CRE", "Nota", "Pontuação"));
       sb.append("------------------------------------------------------------\n");

       int posicao = 1;
       for (Inscricao inscricao : ranque) {
           double pontuacao = inscricao.calcularPontuacao(pesoCre, pesoNota);
           sb.append(String.format("%-5d %-30s %-10.2f %-10.2f %-10.2f\n",
                   posicao++,
                   inscricao.getAluno().getNome(),
                   inscricao.getCre(),
                   inscricao.getNota(),
                   pontuacao));
       }

       return sb.toString();
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
