package br.com.monitoria.excecoes;

/**
 * Exceção lançada quando os pesos do CRE e da Nota não somam 1.0.
 */
public class PesosInvalidosException extends Exception {
    public PesosInvalidosException(double pesoCre, double pesoNota) {
        super(String.format("A soma dos pesos deve ser igual a 1.0. Valores fornecidos: CRE=%.2f, Nota=%.2f, Soma=%.2f", 
                pesoCre, pesoNota, pesoCre + pesoNota));
    }
}

