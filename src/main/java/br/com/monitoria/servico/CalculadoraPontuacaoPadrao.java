package br.com.monitoria.servico;

import br.com.monitoria.Inscricao;
import br.com.monitoria.interfaces.ICalculadoraPontuacao;

/**
 * Implementação padrão do cálculo de pontuação.
 * Fórmula: (CRE * PesoCRE) + (Nota * PesoNota)
 */
public class CalculadoraPontuacaoPadrao implements ICalculadoraPontuacao {

    @Override
    public double calcular(Inscricao inscricao, double pesoCre, double pesoNota) {
        return (inscricao.getCre() * pesoCre) + (inscricao.getNota() * pesoNota);
    }
}
