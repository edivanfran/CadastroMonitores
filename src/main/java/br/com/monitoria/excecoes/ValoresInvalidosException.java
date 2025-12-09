package br.com.monitoria.excecoes;

/**
 * Exceção lançada quando valores inválidos são fornecidos (ex: CRE ou nota fora do intervalo válido).
 */
public class ValoresInvalidosException extends Exception {
    public ValoresInvalidosException(String campo, double valor, double min, double max) {
        super(String.format("Valor inválido para %s: %.2f. O valor deve estar entre %.2f e %.2f.", 
                campo, valor, min, max));
    }
    
    public ValoresInvalidosException(String mensagem) {
        super(mensagem);
    }
}

