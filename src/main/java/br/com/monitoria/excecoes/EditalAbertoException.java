package br.com.monitoria.excecoes;


/**
 * Exceção lançada quando se tenta calcular o resultado de um edital que ainda está aberto.
 */
public class EditalAbertoException extends Exception {
    public EditalAbertoException(String numeroEdital) {
        super("Não é possível calcular o resultado do edital " + numeroEdital + 
              " enquanto ele estiver aberto. Feche o edital primeiro.");
    }
    
    public EditalAbertoException() {
        super("Não é possível calcular o resultado de um edital aberto. Feche o edital primeiro.");
    }
}

