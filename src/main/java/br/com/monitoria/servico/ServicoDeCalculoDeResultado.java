package br.com.monitoria.servico;

import br.com.monitoria.PreferenciaInscricao;
import br.com.monitoria.Vaga;
import br.com.monitoria.excecoes.EditalAbertoException;
import br.com.monitoria.excecoes.SemInscricoesException;
import br.com.monitoria.interfaces.ICalculadoraPontuacao;
import br.com.monitoria.model.Disciplina;
import br.com.monitoria.model.EditalDeMonitoria;
import br.com.monitoria.model.Inscricao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServicoDeCalculoDeResultado {

    public void calcular(EditalDeMonitoria edital) throws EditalAbertoException, SemInscricoesException {
        if (edital.isAberto()) {
            throw new EditalAbertoException(edital.getNumero());
        }

        if (edital.getInscricoes().isEmpty()) {
            throw new SemInscricoesException();
        }

        System.out.println("Calculando resultado do edital " + edital.getNumero() + "...");
        
        // Usamos um novo mapa para o resultado
        Map<String, ArrayList<Inscricao>> novoRanque = new HashMap<>();

        // Agrupa inscrições por disciplina
        Map<String, ArrayList<Inscricao>> inscricoesPorDisciplina = new HashMap<>();
        for (Inscricao inscricao : edital.getInscricoes()) {
            if (inscricao.isDesistiu()) {
                continue;
            }
            String nomeDisciplina = inscricao.getDisciplina().getNomeDisciplina();
            inscricoesPorDisciplina.computeIfAbsent(nomeDisciplina, k -> new ArrayList<>()).add(inscricao);
        }

        for (Map.Entry<String, ArrayList<Inscricao>> entry : inscricoesPorDisciplina.entrySet()) {
            String nomeDisciplina = entry.getKey();
            ArrayList<Inscricao> inscricoesDisciplina = entry.getValue();
            Disciplina disciplina = inscricoesDisciplina.get(0).getDisciplina();

            // A estratégia de cálculo é obtida dinamicamente a partir do edital
            ICalculadoraPontuacao calculadora = edital.getCalculadoraPontuacao();

            // Calcula a pontuação
            for (Inscricao inscricao : inscricoesDisciplina) {
                double pontuacao = calculadora.calcular(inscricao, edital.getPesoCre(), edital.getPesoNota());
                inscricao.setPontuacaoFinal(pontuacao);
            }
            
            // Ordena a lista de inscritos
            inscricoesDisciplina.sort((i1, i2) -> Double.compare(i2.getPontuacaoFinal(), i1.getPontuacaoFinal()));

            int vagasRemuneradasRestantes = disciplina.getVagasRemuneradas();
            int vagasVoluntariasRestantes = disciplina.getVagasVoluntarias();

            for (Inscricao inscricao : inscricoesDisciplina) {
                PreferenciaInscricao pref = inscricao.getPreferenciaVaga();
                boolean conseguiuVaga = false;

                if (pref == PreferenciaInscricao.SOMENTE_REMUNERADA) {
                    if (vagasRemuneradasRestantes > 0) {
                        inscricao.setTipoVaga(Vaga.REMUNERADA);
                        vagasRemuneradasRestantes--;
                        conseguiuVaga = true;
                    }
                } else if (pref == PreferenciaInscricao.REMUNERADA_OU_VOLUNTARIA) {
                    if (vagasRemuneradasRestantes > 0) {
                        inscricao.setTipoVaga(Vaga.REMUNERADA);
                        vagasRemuneradasRestantes--;
                        conseguiuVaga = true;
                    } else if (vagasVoluntariasRestantes > 0) {
                        inscricao.setTipoVaga(Vaga.VOLUNTARIA);
                        vagasVoluntariasRestantes--;
                        conseguiuVaga = true;
                    }
                } else if (pref == PreferenciaInscricao.SOMENTE_VOLUNTARIA) {
                    if (vagasVoluntariasRestantes > 0) {
                        inscricao.setTipoVaga(Vaga.VOLUNTARIA);
                        vagasVoluntariasRestantes--;
                        conseguiuVaga = true;
                    }
                }

                if (!conseguiuVaga) {
                    inscricao.setTipoVaga(null);
                }
            }

            novoRanque.put(nomeDisciplina, inscricoesDisciplina);
        }

        // Atualiza o estado do edital
        edital.setRanquePorDisciplina(novoRanque);
        edital.setResultadoCalculado(true);
        
        System.out.println("Resultado calculado com sucesso para " + novoRanque.size() + " disciplina(s).");
    }
}
