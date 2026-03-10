package br.com.monitoria;

import br.com.monitoria.excecoes.*;

public interface EditalService {

    void fecharEdital(Coordenador coordenador) throws PermissaoNegadaException, EditalFechadoException;

    void reabrirEdital(Coordenador coordenador) throws PermissaoNegadaException, EditalAbertoException, PrazoVencidoException;

    void encerrarPeriodoDesistencia(Coordenador coordenador) throws PermissaoNegadaException;

    boolean jaAcabou();

    void calcularResultado() throws EditalAbertoException, SemInscricoesException;

}
