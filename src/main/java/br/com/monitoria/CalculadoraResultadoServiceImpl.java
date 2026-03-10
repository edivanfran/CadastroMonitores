package br.com.monitoria;

import br.com.monitoria.excecoes.EditalAbertoException;
import br.com.monitoria.excecoes.SemInscricoesException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CalculadoraResultadoServiceImpl implements CalculadoraResultadoService {

    @Override
    public void calcularResultado(EditalDeMonitoria edital) throws EditalAbertoException, SemInscricoesException {
        if (edital.isAberto()) {
            throw new EditalAbertoException(edital.getNumero());
        }

        if (edital.getInscricoes().isEmpty()) {
            throw new SemInscricoesException();
        }

        System.out.println("Calculando resultado do edital " + edital.getNumero() + "...");
        edital.getRanquePorDisciplina().clear();

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

            // Calcula a pontuação e ordena a lista de inscritos
            for (Inscricao inscricao : inscricoesDisciplina) {
                inscricao.calcularPontuacao(edital.getPesoCre(), edital.getPesoNota());
            }
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

            edital.getRanquePorDisciplina().put(nomeDisciplina, inscricoesDisciplina);
        }

        edital.setResultadoCalculado(true);
        System.out.println("Resultado calculado com sucesso para " + edital.getRanquePorDisciplina().size() + " disciplina(s).");
    }
}
