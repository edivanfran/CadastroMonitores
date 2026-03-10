package br.com.monitoria;

import br.com.monitoria.excecoes.EditalAbertoException;
import br.com.monitoria.excecoes.SemInscricoesException;

public interface CalculadoraResultadoService {

    void calcularResultado(EditalDeMonitoria edital) throws EditalAbertoException, SemInscricoesException;

}
