package br.com.monitoria;

import br.com.monitoria.excecoes.*;

import java.time.LocalDate;

public class EditalServiceImpl implements EditalService {

    private final EditalDeMonitoria editalDeMonitoria;
    private final CalculadoraResultadoService calculadoraResultadoService;

    public EditalServiceImpl(EditalDeMonitoria edital, CalculadoraResultadoService calculadoraResultadoService) {
        this.editalDeMonitoria = edital;
        this.calculadoraResultadoService = calculadoraResultadoService;
    }

    @Override
    public void fecharEdital(Coordenador coordenador) throws PermissaoNegadaException, EditalFechadoException {
        if (coordenador == null) {
            throw new PermissaoNegadaException("fechar o edital");
        }
        if (!editalDeMonitoria.isAberto()) {
            throw new EditalFechadoException(editalDeMonitoria.getNumero(), "O edital já se encontra fechado.");
        }
        editalDeMonitoria.setAberto(false);
        System.out.println("Edital " + editalDeMonitoria.getNumero() + " foi fechado com sucesso.");
    }

    @Override
    public void reabrirEdital(Coordenador coordenador) throws PermissaoNegadaException, EditalAbertoException, PrazoVencidoException {
        if (coordenador == null) {
            throw new PermissaoNegadaException("reabrir o edital");
        }
        if (editalDeMonitoria.isAberto()) {
            throw new EditalAbertoException(editalDeMonitoria.getNumero());
        }
        if (LocalDate.now().isAfter(editalDeMonitoria.getDataLimite())) {
            throw new PrazoVencidoException("reabrir o edital", editalDeMonitoria.getDataLimite());
        }
        editalDeMonitoria.setAberto(true);
        System.out.println("Edital " + editalDeMonitoria.getNumero() + " foi reaberto com sucesso.");
    }

    @Override
    public void encerrarPeriodoDesistencia(Coordenador coordenador) throws PermissaoNegadaException {
        if (coordenador == null) {
            throw new PermissaoNegadaException("encerrar o período de desistências");
        }
        editalDeMonitoria.setPeriodoDesistenciaEncerrado(true);
        System.out.println("Período de desistências do edital " + editalDeMonitoria.getNumero() + " foi encerrado.");
    }

    @Override
    public boolean jaAcabou() {
        return LocalDate.now().isAfter(editalDeMonitoria.getDataLimite());
    }

    @Override
    public void calcularResultado() throws EditalAbertoException, SemInscricoesException {
        calculadoraResultadoService.calcularResultado(editalDeMonitoria);
    }
}
