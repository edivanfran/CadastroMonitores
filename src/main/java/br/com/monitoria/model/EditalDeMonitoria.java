package br.com.monitoria.model;

import br.com.monitoria.PreferenciaInscricao;
import br.com.monitoria.Vaga;
import br.com.monitoria.excecoes.*;
import br.com.monitoria.interfaces.ICalculadoraPontuacao;
import br.com.monitoria.servico.CalculadoraPontuacaoPadrao;
import br.com.monitoria.servico.ServicoDeCalculoDeResultado;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EditalDeMonitoria {

    private String id;
    private String numero;
    private LocalDate dataInicio;
    private LocalDate dataLimite;
    private List<Disciplina> disciplinas = new ArrayList<>();
    private boolean aberto;
    private double pesoCre;
    private double pesoNota;
    private boolean resultadoCalculado;
    private boolean periodoDesistenciaEncerrado;

    // Campos transientes (não persistidos) para lógica de negócio em tempo de execução
    private transient List<Inscricao> inscricoes = new ArrayList<>();
    private transient Map<String, ArrayList<Inscricao>> ranquePorDisciplina = new HashMap<>();
    private transient ICalculadoraPontuacao calculadoraPontuacao = new CalculadoraPontuacaoPadrao();

    // Construtor padrão
    public EditalDeMonitoria() {}

    // Construtor para reconstrução a partir do DAO
    public EditalDeMonitoria(String id, String numero, LocalDate dataInicio, LocalDate dataLimite, List<Disciplina> disciplinas, boolean aberto, double pesoCre, double pesoNota, boolean resultadoCalculado, boolean periodoDesistenciaEncerrado) {
        this.id = id;
        this.numero = numero;
        this.dataInicio = dataInicio;
        this.dataLimite = dataLimite;
        this.disciplinas = disciplinas;
        this.aberto = aberto;
        this.pesoCre = pesoCre;
        this.pesoNota = pesoNota;
        this.resultadoCalculado = resultadoCalculado;
        this.periodoDesistenciaEncerrado = periodoDesistenciaEncerrado;
    }

    /**
     * Construtor completo do edital com pesos para cálculo de pontuação.
     */
    public EditalDeMonitoria(String numero, LocalDate dataInicio, LocalDate dataLimite, double pesoCre, double pesoNota) throws PesosInvalidosException {
        if (Math.abs((pesoCre + pesoNota) - 1.0) > 0.0001) {
            throw new PesosInvalidosException(pesoCre, pesoNota);
        }
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
        this.calculadoraPontuacao = new CalculadoraPontuacaoPadrao();
    }
    
    /**
     * Construtor do edital com valores padrão para os pesos (0.5 cada).
     */
    public EditalDeMonitoria(String numero, LocalDate dataInicio, LocalDate dataLimite) throws PesosInvalidosException {
        this(numero, dataInicio, dataLimite, 0.5, 0.5);
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNumero() { return numero; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataLimite() { return dataLimite; }
    public void setDataLimite(LocalDate dataLimite) { this.dataLimite = dataLimite; }
    public List<Disciplina> getDisciplinas() { return disciplinas; }
    public void setDisciplinas(List<Disciplina> disciplinas) { this.disciplinas = disciplinas; }
    public boolean isAberto() { return aberto; }
    public void setAberto(boolean aberto) { this.aberto = aberto; }
    public double getPesoCre() { return pesoCre; }
    public void setPesoCre(double pesoCre) { this.pesoCre = pesoCre; }
    public double getPesoNota() { return pesoNota; }
    public void setPesoNota(double pesoNota) { this.pesoNota = pesoNota; }
    public boolean isResultadoCalculado() { return resultadoCalculado; }
    public void setResultadoCalculado(boolean resultadoCalculado) { this.resultadoCalculado = resultadoCalculado; }
    public boolean isPeriodoDesistenciaEncerrado() { return periodoDesistenciaEncerrado; }
    public void setPeriodoDesistenciaEncerrado(boolean periodoDesistenciaEncerrado) { this.periodoDesistenciaEncerrado = periodoDesistenciaEncerrado; }
    public List<Inscricao> getInscricoes() { return inscricoes; }
    public void setInscricoes(List<Inscricao> inscricoes) { this.inscricoes = inscricoes; }
    public Map<String, ArrayList<Inscricao>> getRanquePorDisciplina() { return ranquePorDisciplina; }
    public void setRanquePorDisciplina(Map<String, ArrayList<Inscricao>> ranquePorDisciplina) { this.ranquePorDisciplina = ranquePorDisciplina; }

    // Métodos de negócio existentes (mantidos)
    public void adicionarInscricao(Inscricao inscricao) { inscricoes.add(inscricao); }
    public void adicionarDisciplina(Disciplina disciplina) { disciplinas.add(disciplina); }

    public void inscreverAluno(Aluno aluno, Disciplina disciplina, double cre, double nota, Vaga tipoVaga, int ordemPreferencia, PreferenciaInscricao preferenciaVaga)
            throws EditalFechadoException, PrazoInscricaoVencidoException, DisciplinaNaoEncontradaException, ValoresInvalidosException, VagasEsgotadasException {
        if (!aberto) throw new EditalFechadoException(numero);
        if (LocalDate.now().isAfter(dataLimite)) throw new PrazoInscricaoVencidoException(dataLimite);
        if (cre < 0 || cre > 100) throw new ValoresInvalidosException("CRE", cre, 0, 100);
        if (nota < 0 || nota > 100) throw new ValoresInvalidosException("Nota", nota, 0, 100);
        if (!disciplinas.contains(disciplina)) throw new DisciplinaNaoEncontradaException(disciplina.getNomeDisciplina());

        disciplina.adicionarAluno(aluno, tipoVaga);
        Inscricao inscricao = new Inscricao(aluno, disciplina, this, cre, nota, tipoVaga, ordemPreferencia, preferenciaVaga);
        inscricoes.add(inscricao);
        System.out.println("Inscrição de " + aluno.getNome() + " em " + disciplina.getNomeDisciplina() + " (" + tipoVaga + ") confirmada.");
    }

    public boolean jaAcabou() { return LocalDate.now().isAfter(dataLimite); }

    public void calcularResultado() throws EditalAbertoException, SemInscricoesException {
        new ServicoDeCalculoDeResultado().calcular(this);
    }

    public EditalDeMonitoria clonar() {
        List<Disciplina> disciplinasClonadas = this.disciplinas.stream().map(Disciplina::clonar).collect(Collectors.toList());
        try {
            EditalDeMonitoria clone = new EditalDeMonitoria(this.numero + " - Cópia", this.dataInicio, this.dataLimite, this.pesoCre, this.pesoNota);
            clone.setDisciplinas(disciplinasClonadas);
            return clone;
        } catch (PesosInvalidosException e) {
            // Não deve acontecer, pois os pesos são válidos
            throw new RuntimeException("Erro inesperado ao clonar edital", e);
        }
    }

    public void processarDesistencia(Aluno aluno, Disciplina disciplina) throws InscricaoNaoEncontradaException, EditalFechadoException {
        if (periodoDesistenciaEncerrado) throw new EditalFechadoException(numero, "O período de desistências já foi encerrado.");
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
        recalcularResultado();
    }

    private void recalcularResultado() {
        System.out.println("Recalculando resultado do edital " + numero + " após desistência...");
        try {
            calcularResultado();
        } catch (EditalAbertoException | SemInscricoesException e) {
            System.out.println("[Erro Interno] Falha ao recalcular resultado: " + e.getMessage());
        }
        System.out.println("Resultado recalculado com sucesso.");
    }

    public void encerrarPeriodoDesistencia(Coordenador coordenador) throws PermissaoNegadaException {
        if (coordenador == null) throw new PermissaoNegadaException("encerrar o período de desistências");
        this.periodoDesistenciaEncerrado = true;
        System.out.println("Período de desistências do edital " + numero + " foi encerrado.");
    }

    public ArrayList<Inscricao> getRanqueDisciplina(String nomeDisciplina) {
        return ranquePorDisciplina.get(nomeDisciplina);
    }

    public void fecharEdital(Coordenador coordenador) throws PermissaoNegadaException, EditalFechadoException {
        if (coordenador == null) throw new PermissaoNegadaException("fechar o edital");
        if (!aberto) throw new EditalFechadoException(numero, "O edital já se encontra fechado.");
        this.aberto = false;
        System.out.println("Edital " + numero + " foi fechado com sucesso.");
    }

    public void reabrirEdital(Coordenador coordenador) throws PermissaoNegadaException, EditalAbertoException, PrazoVencidoException {
        if (coordenador == null) throw new PermissaoNegadaException("reabrir o edital");
        if (aberto) throw new EditalAbertoException(numero);
        if (LocalDate.now().isAfter(dataLimite)) throw new PrazoVencidoException("reabrir o edital", dataLimite);
        this.aberto = true;
        System.out.println("Edital " + numero + " foi reaberto com sucesso.");
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
