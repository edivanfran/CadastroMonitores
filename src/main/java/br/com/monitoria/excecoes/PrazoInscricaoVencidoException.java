package br.com.monitoria.excecoes;

import java.time.LocalDate;

/**
 * Exceção lançada quando se tenta inscrever um aluno após o prazo de inscrição ter vencido.
 */
public class PrazoInscricaoVencidoException extends Exception {
    public PrazoInscricaoVencidoException(LocalDate dataLimite) {
        super("O prazo para inscrição neste edital já acabou. Data limite: " + dataLimite);
    }
    
    public PrazoInscricaoVencidoException() {
        super("O prazo para inscrição neste edital já acabou.");
    }
}

