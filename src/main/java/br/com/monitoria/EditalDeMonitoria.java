package br.com.monitoria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import br.com.monitoria.excecoes.*;

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
    // Nome da disciplina -> Lista ordenada de inscrições
    private Map<String, ArrayList<Inscricao>> ranquePorDisciplina;
    private boolean resultadoCalculado;
    private boolean periodoDesistenciaEncerrado;

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
    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }
    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }
    public boolean isAberto() {
        return aberto;
    }
    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    /**
     * Construtor privado para uso interno do método clonar.
     */
    private EditalDeMonitoria(long id, String numero, LocalDate dataInicio, LocalDate dataLimite,
                              ArrayList<Disciplina> disciplinas, boolean aberto, double pesoCre, double pesoNota) {
        this.id = id;
        this.numero = numero;
        this.dataInicio = dataInicio;
        this.dataLimite = dataLimite;
        this.disciplinas = disciplinas;
        this.aberto = aberto;
        this.pesoCre = pesoCre;
        this.pesoNota = pesoNota;
        this.inscricoes = new ArrayList<>();
        this.ranquePorDisciplina = new HashMap<>();
        this.resultadoCalculado = false;
        this.periodoDesistenciaEncerrado = false;
    }

    /**
     * Construtor completo do edital com pesos para cálculo de pontuação.
     * <p>Os pesos devem obrigatoriamente somar 1.0.</p>
     * @param numero O número do edital
     * @param dataInicio A data de início das inscrições
     * @param dataLimite A data limite das inscrições
     * @param pesoCre O peso do CRE no cálculo da pontuação
     * @param pesoNota O peso da nota (média na disciplina) no cálculo da pontuação
     * @throws PesosInvalidosException Se a soma dos pesos não for igual a 1.0
     */
    public EditalDeMonitoria(String numero, LocalDate dataInicio, LocalDate dataLimite, double pesoCre, double pesoNota) throws PesosInvalidosException {
       // Valida que os pesos somem 1
       if (Math.abs((pesoCre + pesoNota) - 1.0) > 0.0001) { // Usa epsilon para comparação de double
           throw new PesosInvalidosException(pesoCre, pesoNota);
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
       this.periodoDesistenciaEncerrado = false;
   }

    /**
     * Construtor do edital com valores padrão para os pesos (0.5 cada).
     * <p>Como os pesos padrão somam 1.0, este construtor nunca lança exceção.</p>
     * @param numero O número do edital
     * @param dataInicio A data de início das inscrições
     * @param dataLimite A data limite das inscrições
     * @throws PesosInvalidosException Se a soma dos pesos não for igual a 1.0 (nunca ocorre com valores padrão)
     */
    public EditalDeMonitoria(String numero, LocalDate dataInicio, LocalDate dataLimite) throws PesosInvalidosException {
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

    public boolean isPeriodoDesistenciaEncerrado() {
        return periodoDesistenciaEncerrado;
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
     * <p>Primeiro testa se o edital continua aberto – se não, lança {@link EditalFechadoException};</p>
     * <p>continuando aberto, verifica se está dentro do prazo de inscrição no edital – se não, lança {@link PrazoInscricaoVencidoException};</p>
     * <p>estando dentro do prazo, cria uma inscrição com o CRE e a nota (média na disciplina) fornecidos pelo aluno.</p>
     * <p>Caso não encontre a disciplina— lança {@link DisciplinaNaoEncontradaException}.</p>
     * @param aluno O aluno que se deseja inscrever no edital
     * @param nomeDisciplina O nome da disciplina do edital a qual aluno deseja concorrer
     * @param cre O CRE do aluno
     * @param nota A média do aluno na disciplina específica
     * @param tipoVaga O tipo de vaga (remunerada ou voluntária)
     * @throws EditalFechadoException Se o edital estiver fechado
     * @throws PrazoInscricaoVencidoException Se o prazo de inscrição tiver vencido
     * @throws DisciplinaNaoEncontradaException Se a disciplina não for encontrada no edital
     * @throws ValoresInvalidosException Se o CRE ou a nota estiverem fora do intervalo válido (0-10)
     */
   public void inscreverAluno(Aluno aluno, String nomeDisciplina, double cre, double nota, Vaga tipoVaga) 
           throws EditalFechadoException, PrazoInscricaoVencidoException, DisciplinaNaoEncontradaException, ValoresInvalidosException {
       if (!aberto) {
           throw new EditalFechadoException(numero);
       }
       if (LocalDate.now().isAfter(dataLimite)) {
           throw new PrazoInscricaoVencidoException(dataLimite);
       }
       
       // Valida valores de CRE e nota
       if (cre < 0 || cre > 10) {
           throw new ValoresInvalidosException("CRE", cre, 0, 10);
       }
       if (nota < 0 || nota > 10) {
           throw new ValoresInvalidosException("Nota", nota, 0, 10);
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
               return;
           }
       }
       throw new DisciplinaNaoEncontradaException(nomeDisciplina);
   }

   public boolean jaAcabou() {
       return LocalDate.now().isAfter(dataLimite);
   }

   /**
    * Calcula o resultado do edital (ranqueamento dos alunos).
    * <p>Para cada disciplina, gera um ranque ordenado pela pontuação obtida usando a fórmula:</p>
    * <p>PONTUAÇÃO = PESO_CRE * CRE_ALUNO + PESO_NOTA * NOTA_ALUNO</p>
    * <p>A verificação de permissão (se o usuário é Coordenador) deve ser feita antes de chamar este método.</p>
    * @throws EditalAbertoException Se o edital ainda estiver aberto
    * @throws SemInscricoesException Se não houver inscrições no edital
    */
   public void calcularResultado() 
           throws EditalAbertoException, SemInscricoesException {
       if (aberto) {
           throw new EditalAbertoException(numero);
       }

       if (inscricoes.isEmpty()) {
           throw new SemInscricoesException();
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
               inscricao.calcularPontuacao(pesoCre, pesoNota);
           }

           // Ordena as inscrições por pontuação (decrescente)
           inscricoesDisciplina.sort((i1, i2) -> Double.compare(i2.getPontuacaoFinal(), i1.getPontuacaoFinal()));

           ranquePorDisciplina.put(nomeDisciplina, inscricoesDisciplina);
       }

       resultadoCalculado = true;
       System.out.println("Resultado calculado com sucesso para " + ranquePorDisciplina.size() + " disciplina(s).");
   }

    /**
     * Cria um clone deste edital.
     * O edital clonado terá um novo ID, um número com o sufixo "- Cópia",
     * e começará com a lista de inscrições e resultados zerados.
     * As disciplinas são clonadas para evitar compartilhamento de referências.
     * @return Uma nova instância de EditalDeMonitoria.
     */
    public EditalDeMonitoria clonar() {
        // Cria uma cópia profunda da lista de disciplinas
        ArrayList<Disciplina> disciplinasClonadas = this.disciplinas.stream()
                .map(Disciplina::clonar)
                .collect(Collectors.toCollection(ArrayList::new));

        // Cria o novo edital com um novo ID e número, mas com as mesmas configurações
        return new EditalDeMonitoria(
                System.currentTimeMillis(),
                this.numero + " - Cópia",
                this.dataInicio,
                this.dataLimite,
                disciplinasClonadas,
                true, // Um edital clonado sempre começa aberto
                this.pesoCre,
                this.pesoNota
        );
    }

    /**
     * Processa a desistência de um aluno de uma vaga.
     * @param aluno O aluno que está desistindo
     * @param disciplina A disciplina da qual o aluno está desistindo
     * @throws InscricaoNaoEncontradaException Se a inscrição não for encontrada
     * @throws EditalFechadoException Se o período de desistência já estiver encerrado
     */
    public void processarDesistencia(Aluno aluno, Disciplina disciplina) throws InscricaoNaoEncontradaException, EditalFechadoException {
        if (periodoDesistenciaEncerrado) {
            throw new EditalFechadoException(numero, "O período de desistências já foi encerrado.");
        }

        Inscricao inscricaoAlvo = null;
        for (Inscricao inscricao : inscricoes) {
            if (inscricao.getAluno().equals(aluno) && inscricao.getDisciplina().equals(disciplina)) {
                inscricaoAlvo = inscricao;
                break;
            }
        }

        if (inscricaoAlvo == null) {
            throw new InscricaoNaoEncontradaException();
        }

        inscricaoAlvo.setDesistiu(true);
        System.out.println("Aluno " + aluno.getNome() + " desistiu da vaga em " + disciplina.getNomeDisciplina());

        // Recalcula o resultado para refletir a desistência
        recalcularResultado();
    }

    /**
     * Recalcula o ranqueamento do edital.
     * Este método é chamado internamente após uma desistência.
     */
    private void recalcularResultado() {
        System.out.println("Recalculando resultado do edital " + numero + " após desistência...");
        ranquePorDisciplina.clear();

        Map<String, ArrayList<Inscricao>> inscricoesPorDisciplina = new HashMap<>();
        for (Inscricao inscricao : inscricoes) {
            if (inscricao.isDesistiu()) {
                continue; // Ignora inscrições desistidas
            }
            String nomeDisciplina = inscricao.getDisciplina().getNomeDisciplina();
            inscricoesPorDisciplina.putIfAbsent(nomeDisciplina, new ArrayList<>());
            inscricoesPorDisciplina.get(nomeDisciplina).add(inscricao);
        }

        for (Map.Entry<String, ArrayList<Inscricao>> entry : inscricoesPorDisciplina.entrySet()) {
            String nomeDisciplina = entry.getKey();
            ArrayList<Inscricao> inscricoesDisciplina = entry.getValue();

            // A pontuação já foi calculada, apenas reordenamos
            inscricoesDisciplina.sort((i1, i2) -> Double.compare(i2.getPontuacaoFinal(), i1.getPontuacaoFinal()));

            ranquePorDisciplina.put(nomeDisciplina, inscricoesDisciplina);
        }
        System.out.println("Resultado recalculado com sucesso.");
    }

    /**
     * Encerra o período de desistências, tornando o resultado final.
     * Apenas coordenadores podem executar esta operação.
     * @param coordenador O coordenador que está executando a operação
     * @throws PermissaoNegadaException Se o usuário não for coordenador
     */
    public void encerrarPeriodoDesistencia(Coordenador coordenador) throws PermissaoNegadaException {
        if (coordenador == null) {
            throw new PermissaoNegadaException("encerrar o período de desistências");
        }
        this.periodoDesistenciaEncerrado = true;
        System.out.println("Período de desistências do edital " + numero + " foi encerrado.");
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
           double pontuacao = inscricao.getPontuacaoFinal();
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
    * @throws PermissaoNegadaException Se o usuário não for coordenador
    * @throws EditalFechadoException Se o edital já estiver fechado
    */
   public void fecharEdital(Coordenador coordenador) throws PermissaoNegadaException, EditalFechadoException {
       if (coordenador == null) {
           throw new PermissaoNegadaException("fechar o edital");
       }
       if (!aberto) {
           throw new EditalFechadoException(numero, "O edital já se encontra fechado.");
       }
       this.aberto = false;
       System.out.println("Edital " + numero + " foi fechado com sucesso.");
   }

    /**
     * Reabre um edital que foi fechado, permitindo novas inscrições.
     * <p>Apenas coordenadores podem executar esta operação.</p>
     * @param coordenador O Coordenador que está executando a operação
     * @throws PermissaoNegadaException Se o usuário não for coordenador
     * @throws EditalAbertoException Se o edital já estiver aberto
     * @throws PrazoVencidoException Se a data limite para inscrições já tiver passado
     */
    public void reabrirEdital(Coordenador coordenador) throws PermissaoNegadaException, EditalAbertoException, PrazoVencidoException {
        if (coordenador == null) {
            throw new PermissaoNegadaException("reabrir o edital");
        }
        if (aberto) {
            throw new EditalAbertoException(numero);
        }
        if (LocalDate.now().isAfter(dataLimite)) {
            throw new PrazoVencidoException("reabrir o edital", dataLimite);
        }
        this.aberto = true;
        System.out.println("Edital " + numero + " foi reaberto com sucesso.");
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
