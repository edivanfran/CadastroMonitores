package br.com.monitoria.interfaces;

import br.com.monitoria.Inscricao;

/**
 * Interface que define a estratégia de cálculo de pontuação para uma inscrição.
 * Segue o padrão Strategy para permitir diferentes regras de cálculo no futuro (OCP).
 */
public interface ICalculadoraPontuacao {
    /**
     * Calcula a pontuação final de uma inscrição baseada nos pesos fornecidos.
     * @param inscricao A inscrição a ser avaliada.
     * @param pesoCre O peso do CRE no cálculo.
     * @param pesoNota O peso da nota da disciplina no cálculo.
     * @return A pontuação final calculada.
     */
    double calcular(Inscricao inscricao, double pesoCre, double pesoNota);
}
