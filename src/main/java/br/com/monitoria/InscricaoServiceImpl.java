package br.com.monitoria;

import br.com.monitoria.excecoes.*;

import java.time.LocalDate;

public class InscricaoServiceImpl implements InscricaoService {

    private final CalculadoraResultadoService calculadoraResultadoService;

    public InscricaoServiceImpl(CalculadoraResultadoService calculadoraResultadoService) {
        this.calculadoraResultadoService = calculadoraResultadoService;
    }

    @Override
    public void inscreverAluno(EditalDeMonitoria edital, Aluno aluno, String nomeDisciplina, double cre, double nota, Vaga tipoVaga, int ordemPreferencia, PreferenciaInscricao preferenciaVaga)
            throws EditalFechadoException, PrazoInscricaoVencidoException, DisciplinaNaoEncontradaException, ValoresInvalidosException, VagasEsgotadasException {
        if (!edital.isAberto()) {
            throw new EditalFechadoException(edital.getNumero());
        }
        if (LocalDate.now().isAfter(edital.getDataLimite())) {
            throw new PrazoInscricaoVencidoException(edital.getDataLimite());
        }

        if (cre < 0 || cre > 100) {
            throw new ValoresInvalidosException("CRE", cre, 0, 100);
        }
        if (nota < 0 || nota > 100) {
            throw new ValoresInvalidosException("Nota", nota, 0, 100);
        }

        for (Disciplina d : edital.getDisciplinas()) {
            if (d.getNomeDisciplina().equalsIgnoreCase(nomeDisciplina)) {
                d.adicionarAluno(aluno, tipoVaga);
                Inscricao inscricao = new Inscricao(aluno, d, cre, nota, tipoVaga, ordemPreferencia, preferenciaVaga);
                edital.getInscricoes().add(inscricao);
                System.out.println("Inscrição de " + aluno.getNome() + " em " + nomeDisciplina + " (" + tipoVaga + ") confirmada.");
                return;
            }
        }
        throw new DisciplinaNaoEncontradaException(nomeDisciplina);
    }

    @Override
    public void processarDesistencia(EditalDeMonitoria edital, Aluno aluno, Disciplina disciplina)
            throws InscricaoNaoEncontradaException, EditalFechadoException {
        if (edital.isPeriodoDesistenciaEncerrado()) {
            throw new EditalFechadoException(edital.getNumero(), "O período de desistências já foi encerrado.");
        }

        Inscricao inscricaoAlvo = null;
        for (Inscricao inscricao : edital.getInscricoes()) {
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

        recalcularResultado(edital);
    }

    private void recalcularResultado(EditalDeMonitoria edital) {
        System.out.println("Recalculando resultado do edital " + edital.getNumero() + " após desistência...");
        try {
            calculadoraResultadoService.calcularResultado(edital);
        } catch (EditalAbertoException | SemInscricoesException e) {
            System.out.println("[Erro Interno] Falha ao recalcular resultado: " + e.getMessage());
        }
        System.out.println("Resultado recalculado com sucesso.");
    }
}
