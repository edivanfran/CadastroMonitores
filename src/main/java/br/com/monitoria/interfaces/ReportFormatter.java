package br.com.monitoria.interfaces;

import br.com.monitoria.model.Disciplina;
import br.com.monitoria.model.Inscricao;

import java.util.ArrayList;

public interface ReportFormatter {

    String getTipoFormmater();

    void iniciarDocumento(String nomeArquivo, boolean landscape) throws Exception;

    void adicionarTituloPrincipal(String texto) throws Exception;

    void adicionarSubtitulo(String texto) throws Exception;

    void adicionarParagrafo(String texto) throws Exception;

    void adicionarTabelaResultado(Disciplina disciplina, ArrayList<Inscricao> ranque) throws Exception;

    void salvarEFecharDocumento();
}
