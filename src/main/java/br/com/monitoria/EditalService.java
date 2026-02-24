package br.com.monitoria;

import br.com.monitoria.excecoes.DisciplinaNaoEncontradaException;
import br.com.monitoria.excecoes.EditalAbertoException;
import br.com.monitoria.excecoes.EditalFechadoException;
import br.com.monitoria.excecoes.InscricaoNaoEncontradaException;
import br.com.monitoria.excecoes.PermissaoNegadaException;
import br.com.monitoria.excecoes.PrazoInscricaoVencidoException;
import br.com.monitoria.excecoes.PrazoVencidoException;
import br.com.monitoria.excecoes.SemInscricoesException;
import br.com.monitoria.excecoes.VagasEsgotadasException;
import br.com.monitoria.excecoes.ValoresInvalidosException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Essa classe serve para fazer os calculos da classe Edital
// Também vão fazer a manipulação dos dados de Edital
public class EditalService {

    private EditalDeMonitoria editalDeMonitoria;

    public EditalService(EditalDeMonitoria edital) {
        this.editalDeMonitoria = edital;
    }

    /**
     * Inscreve um aluno em uma disciplina do edital.
     * @param aluno O aluno que se deseja inscrever no edital
     * @param nomeDisciplina O nome da disciplina do edital a qual aluno deseja concorrer
     * @param cre O CRE do aluno
     * @param nota A média do aluno na disciplina específica
     * @param tipoVaga O tipo de vaga (remunerada ou voluntária)
     * @param ordemPreferencia A ordem de preferência da disciplina para o aluno
     * @param preferenciaVaga A preferência do aluno pelo tipo de vaga
     * @throws EditalFechadoException Se o edital estiver fechado
     * @throws PrazoInscricaoVencidoException Se o prazo de inscrição tiver vencido
     * @throws DisciplinaNaoEncontradaException Se a disciplina não for encontrada no edital
     * @throws ValoresInvalidosException Se o CRE ou a nota estiverem fora do intervalo válido (0-100)
     * @throws VagasEsgotadasException se não houver mais vagas do tipo solicitado.
     */
    public void inscreverAluno(Aluno aluno, String nomeDisciplina, double cre, double nota, Vaga tipoVaga, int ordemPreferencia, PreferenciaInscricao preferenciaVaga)
            throws EditalFechadoException, PrazoInscricaoVencidoException, DisciplinaNaoEncontradaException, ValoresInvalidosException, VagasEsgotadasException {
        if (!aberto) {
            throw new EditalFechadoException(numero);
        }
        if (LocalDate.now().isAfter(dataLimite)) {
            throw new PrazoInscricaoVencidoException(dataLimite);
        }

        // Valida valores de CRE e nota
        if (cre < 0 || cre > 100) {
            throw new ValoresInvalidosException("CRE", cre, 0, 100);
        }
        if (nota < 0 || nota > 100) {
            throw new ValoresInvalidosException("Nota", nota, 0, 100);
        }

        for (Disciplina d : disciplinas) {
            if (d.getNomeDisciplina().equalsIgnoreCase(nomeDisciplina)) {
                // Tenta adicionar o aluno na disciplina, lança exceção se não conseguir
                d.adicionarAluno(aluno, tipoVaga);

                // Se não lançou exceção, a vaga foi garantida. Cria a inscrição.
                Inscricao inscricao = new Inscricao(aluno, d, cre, nota, tipoVaga, ordemPreferencia, preferenciaVaga);
                inscricoes.add(inscricao);

                System.out.println("Inscrição de " + aluno.getNome() + " em " + nomeDisciplina + " (" + tipoVaga + ") confirmada.");
                return;
            }
        }
        throw new DisciplinaNaoEncontradaException(nomeDisciplina);
    }

    /**
     * Fecha o edital, impedindo novas inscrições.
     * <p>Apenas coordenadores podem executar esta operação.</p>
     * @param coordenador O Coordenador que está executando a operação
     * @throws PermissaoNegadaException Se o usuário não for coordenador
     * @throws EditalFechadoException Se o edital já estiver fechado
     */
    public void fecharEdital(Coordenador coordenador) throws PermissaoNegadaException, EditalFechadoException {
        if (coordenador == null) {
            throw new PermissaoNegadaException("fechar o edital");
        }
        if (!aberto) {
            throw new EditalFechadoException(numero, "O edital já se encontra fechado.");
        }
        this.aberto = false;
        System.out.println("Edital " + numero + " foi fechado com sucesso.");
    }

    /**
     * Reabre um edital que foi fechado, permitindo novas inscrições.
     * <p>Apenas coordenadores podem executar esta operação.</p>
     * @param coordenador O Coordenador que está executando a operação
     * @throws PermissaoNegadaException Se o usuário não for coordenador
     * @throws EditalAbertoException Se o edital já estiver aberto
     * @throws PrazoVencidoException Se a data limite para inscrições já tiver passado
     */
    public void reabrirEdital(Coordenador coordenador) throws PermissaoNegadaException, EditalAbertoException, PrazoVencidoException {
        if (coordenador == null) {
            throw new PermissaoNegadaException("reabrir o edital");
        }
        if (aberto) {
            throw new EditalAbertoException(numero);
        }
        if (LocalDate.now().isAfter(dataLimite)) {
            throw new PrazoVencidoException("reabrir o edital", dataLimite);
        }
        this.aberto = true;
        System.out.println("Edital " + numero + " foi reaberto com sucesso.");
    }


    /**
     * Processa a desistência de um aluno de uma vaga.
     * @param aluno O aluno que está desistindo
     * @param disciplina A disciplina da qual o aluno está desistindo
     * @throws InscricaoNaoEncontradaException Se a inscrição não for encontrada
     * @throws EditalFechadoException Se o período de desistência já estiver encerrado
     */
    public void processarDesistencia(Aluno aluno, Disciplina disciplina) throws InscricaoNaoEncontradaException, EditalFechadoException {
        if (periodoDesistenciaEncerrado) {
            throw new EditalFechadoException(numero, "O período de desistências já foi encerrado.");
        }

        Inscricao inscricaoAlvo = null;
        for (Inscricao inscricao : inscricoes) {
            if (inscricao.getAluno().equals(aluno) && inscricao.getDisciplina().equals(disciplina)) {
                inscricaoAlvo = inscricao;
                break;
            }
        }

        if (inscricaoAlvo == null) {
            throw new InscricaoNaoEncontradaException();
        }

        inscricaoAlvo.setDesistiu(true);
        System.out.println("Aluno " + aluno.getNome() + " desistiu da vaga em " + disciplina.getNomeDisciplina());

        // Recalcula o resultado para a desistência
        recalcularResultado();
    }

    /**
     * Recalcula o ranqueamento do edital.
     * Este método é chamado internamente após uma desistência.
     */
    private void recalcularResultado() {
        System.out.println("Recalculando resultado do edital " + numero + " após desistência...");
        try {
            calcularResultado();
        } catch (EditalAbertoException | SemInscricoesException e) {
            System.out.println("[Erro Interno] Falha ao recalcular resultado: " + e.getMessage());
        }
        System.out.println("Resultado recalculado com sucesso.");
    }

    /**
     * Encerra o período de desistências, tornando o resultado final.
     * Apenas coordenadores podem executar esta operação.
     * @param coordenador O coordenador que está executando a operação
     * @throws PermissaoNegadaException Se o usuário não for coordenador
     */
    public void encerrarPeriodoDesistencia(Coordenador coordenador) throws PermissaoNegadaException {
        if (coordenador == null) {
            throw new PermissaoNegadaException("encerrar o período de desistências");
        }
        this.periodoDesistenciaEncerrado = true;
        System.out.println("Período de desistências do edital " + numero + " foi encerrado.");
    }

    public boolean jaAcabou() {
        return LocalDate.now().isAfter(dataLimite);
    }

    /**
     * Calcula o resultado do edital (ranqueamento dos alunos).
     * <p>Para cada disciplina, gera um ranque ordenado pela pontuação obtida usando a fórmula:</p>
     * <p>PONTUAÇÃO = PESO_CRE * CRE_ALUNO + PESO_NOTA * NOTA_ALUNO</p>
     * <p>A verificação de permissão (se o usuário é Coordenador) deve ser feita antes de chamar este método.</p>
     * @throws EditalAbertoException Se o edital ainda estiver aberto
     * @throws SemInscricoesException Se não houver inscrições no edital
     */
    public void calcularResultado()
            throws EditalAbertoException, SemInscricoesException {
        if (aberto) {
            throw new EditalAbertoException(numero);
        }

        if (inscricoes.isEmpty()) {
            throw new SemInscricoesException();
        }

        System.out.println("Calculando resultado do edital " + numero + "...");
        ranquePorDisciplina.clear();

        // Agrupa inscrições por disciplina
        Map<String, ArrayList<Inscricao>> inscricoesPorDisciplina = new HashMap<>();
        for (Inscricao inscricao : inscricoes) {
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
                inscricao.calcularPontuacao(pesoCre, pesoNota);
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

            ranquePorDisciplina.put(nomeDisciplina, inscricoesDisciplina);
        }

        resultadoCalculado = true;
        System.out.println("Resultado calculado com sucesso para " + ranquePorDisciplina.size() + " disciplina(s).");
    }
}
