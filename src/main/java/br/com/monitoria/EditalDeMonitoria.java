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
       if (Math.abs((pesoCre + pesoNota) - 1.0) > 0.0001) {
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
     * Cria um clone deste edital.
     * O edital clonado terá um novo ID, um número com o sufixo "- Cópia",
     * e começará com a lista de inscrições e resultados zerados.
     * As disciplinas são clonadas para evitar compartilhamento de referências.
     * @return Uma nova instância de EditalDeMonitoria.
     */
    public EditalDeMonitoria clonar() {
        // Cria uma cópia da lista de disciplinas
        ArrayList<Disciplina> disciplinasClonadas = this.disciplinas.stream()
                .map(Disciplina::clonar)
                .collect(Collectors.toCollection(ArrayList::new));

        // Cria o novo edital com um novo ID e número
        return new EditalDeMonitoria(
                System.currentTimeMillis(),
                this.numero + " - Cópia",
                this.dataInicio,
                this.dataLimite,
                disciplinasClonadas,
                true,
                this.pesoCre,
                this.pesoNota
        );
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
       sb.append(String.format("%-5s %-30s %-10s %-10s %-10s %-15s\n", "Pos.", "Aluno", "CRE", "Nota", "Pontuação", "Vaga"));
       sb.append("--------------------------------------------------------------------------\n");

       int posicao = 1;
       for (Inscricao inscricao : ranque) {
           double pontuacao = inscricao.getPontuacaoFinal();
           String status = inscricao.getTipoVaga() != null ? inscricao.getTipoVaga().toString() : "Excedente";
           sb.append(String.format("%-5d %-30s %-10.2f %-10.2f %-10.2f %-15s\n",
                   posicao++,
                   inscricao.getAluno().getNome(),
                   inscricao.getCre(),
                   inscricao.getNota(),
                   pontuacao,
                   status));
       }

       return sb.toString();
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
