package br.com.monitoria.excecoes;

import br.com.monitoria.Vaga;

public class VagasEsgotadasException extends Exception {
    public VagasEsgotadasException(String nomeDisciplina, Vaga tipoVaga) {
        super("Não há mais vagas do tipo '" + tipoVaga + "' para a disciplina " + nomeDisciplina);
    }
}
