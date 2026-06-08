package br.com.monitoria.excecoes;

public class CampoSenhaInvalidadoException extends RuntimeException {
    public CampoSenhaInvalidadoException(String message) {
        super(message);
    }
}
