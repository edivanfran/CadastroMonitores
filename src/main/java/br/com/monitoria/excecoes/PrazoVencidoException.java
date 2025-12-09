package br.com.monitoria.excecoes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PrazoVencidoException extends Exception {
    public PrazoVencidoException(String acao, LocalDate dataLimite) {
        super("Não é possível " + acao + ", pois o prazo (" + 
              dataLimite.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + 
              ") já foi ultrapassado.");
    }
}
