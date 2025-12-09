package br.com.monitoria.excecoes;


public class LoginInvalidoException extends Exception {
    public LoginInvalidoException() {
        super("E-mail ou senha inválido");
    }
}
