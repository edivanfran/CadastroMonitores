package br.com.monitoria.excecoes;

public class CampoEmailInvalidadoException extends RuntimeException {
    public CampoEmailInvalidadoException(String message) {
        super(message);
    }
}
