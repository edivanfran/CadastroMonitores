package br.com.monitoria.excecoes;

public class CredenciaisInvalidasException extends Exception {
    public CredenciaisInvalidasException(String message) {
        super(message);
    }
}
