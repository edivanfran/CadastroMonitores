package br.com.monitoria.servico;

import br.com.monitoria.GerenciadorDeDados;
import br.com.monitoria.PreferenciaInscricao;
import br.com.monitoria.SessaoUsuario;
import br.com.monitoria.Vaga;
import br.com.monitoria.excecoes.ValidacaoException;
import br.com.monitoria.excecoes.VagasEsgotadasException;
import br.com.monitoria.model.Aluno;
import br.com.monitoria.model.Disciplina;
import br.com.monitoria.model.EditalDeMonitoria;

public class InscricaoService {

    private GerenciadorDeDados gerenciadorDeDados;
    private SessaoUsuario sessaoUsuario;

    public InscricaoService() {
        this.gerenciadorDeDados = GerenciadorDeDados.getInstancia();
        this.sessaoUsuario = SessaoUsuario.getInstancia();
    }

    public String inscreverAluno(EditalDeMonitoria edital, Disciplina disciplina, double cre, double nota, int ordemPreferencia, PreferenciaInscricao preferenciaVaga) throws Exception {
        if (disciplina == null) {
            throw new ValidacaoException("Por favor, selecione uma disciplina.");
        }

        Aluno alunoLogado = (Aluno) sessaoUsuario.getUsuarioLogado();
        String mensagemSucesso;

        switch (preferenciaVaga) {
            case SOMENTE_REMUNERADA:
                realizarInscricao(edital, alunoLogado, disciplina, cre, nota, Vaga.REMUNERADA, ordemPreferencia, preferenciaVaga);
                mensagemSucesso = "Inscrição para vaga REMUNERADA realizada com sucesso!";
                break;
            case SOMENTE_VOLUNTARIA:
                realizarInscricao(edital, alunoLogado, disciplina, cre, nota, Vaga.VOLUNTARIA, ordemPreferencia, preferenciaVaga);
                mensagemSucesso = "Inscrição para vaga VOLUNTÁRIA realizada com sucesso!";
                break;
            case REMUNERADA_OU_VOLUNTARIA:
                try {
                    realizarInscricao(edital, alunoLogado, disciplina, cre, nota, Vaga.REMUNERADA, ordemPreferencia, preferenciaVaga);
                    mensagemSucesso = "Inscrição para vaga REMUNERADA realizada com sucesso!";
                } catch (VagasEsgotadasException eRemunerada) {
                    try {
                        realizarInscricao(edital, alunoLogado, disciplina, cre, nota, Vaga.VOLUNTARIA, ordemPreferencia, preferenciaVaga);
                        mensagemSucesso = "Vagas remuneradas esgotadas. Inscrição para vaga VOLUNTÁRIA realizada com sucesso!";
                    } catch (VagasEsgotadasException eVoluntaria) {
                        throw new VagasEsgotadasException("Não há mais vagas remuneradas ou voluntárias para esta disciplina.");
                    }
                }
                break;
            default:
                throw new IllegalStateException("Opção de preferência de vaga inesperada: " + preferenciaVaga);
        }
        return mensagemSucesso;
    }

    private void realizarInscricao(EditalDeMonitoria edital, Aluno aluno, Disciplina disciplina, double cre, double nota, Vaga tipoVaga, int ordem, PreferenciaInscricao pref) throws Exception {
        gerenciadorDeDados.inscreverAlunoEmEdital(edital, aluno, disciplina, cre, nota, tipoVaga, ordem, pref);
    }
}
