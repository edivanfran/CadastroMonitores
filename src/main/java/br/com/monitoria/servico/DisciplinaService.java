package br.com.monitoria.servico;

import br.com.monitoria.GerenciadorDeDados;
import br.com.monitoria.model.Disciplina;
import br.com.monitoria.model.EditalDeMonitoria;
import br.com.monitoria.model.Inscricao;

import java.time.LocalDate;

public class DisciplinaService {

    private GerenciadorDeDados gerenciadorDeDados;

    public DisciplinaService() {
        this.gerenciadorDeDados = GerenciadorDeDados.getInstancia();
    }

    public Disciplina adicionarDisciplina(EditalDeMonitoria edital, String nome, int vagasRemuneradas, int vagasVoluntarias) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("O nome da disciplina não pode ser vazio.");
        }

        EditalDeMonitoria editalAtualizado = gerenciadorDeDados.buscarEditalPorId(edital.getId());

        for (Disciplina d : editalAtualizado.getDisciplinas()) {
            if (d.getNomeDisciplina().equalsIgnoreCase(nome)) {
                throw new Exception("Já existe uma disciplina com este nome no edital.");
            }
        }

        Disciplina novaDisciplina = new Disciplina(nome, vagasVoluntarias, vagasRemuneradas);
        gerenciadorDeDados.adicionarDisciplinaAoEdital(editalAtualizado, novaDisciplina);
        
        return novaDisciplina;
    }

    public void salvarDisciplina(EditalDeMonitoria edital, Disciplina disciplina, int novasVagasRem, int novasVagasVol) throws Exception {
        if (disciplina == null) {
            throw new Exception("Nenhuma disciplina selecionada para salvar.");
        }

        boolean editalAberto = edital.isAberto() && edital.getDataInicio().isBefore(LocalDate.now().plusDays(1));
        if (editalAberto) {
            if (novasVagasRem < disciplina.getVagasRemuneradas() || novasVagasVol < disciplina.getVagasVoluntarias()) {
                throw new Exception("Com o edital aberto, você só pode aumentar o número de vagas.");
            }
        }

        disciplina.setVagasRemuneradas(novasVagasRem);
        disciplina.setVagasVoluntarias(novasVagasVol);
        gerenciadorDeDados.atualizarDisciplina(disciplina);
    }

    public void apagarDisciplina(EditalDeMonitoria edital, Disciplina disciplina) throws Exception {
        if (disciplina == null) {
            throw new Exception("Nenhuma disciplina selecionada para apagar.");
        }

        EditalDeMonitoria editalAtualizado = gerenciadorDeDados.buscarEditalPorId(edital.getId());

        for (Inscricao insc : editalAtualizado.getInscricoes()) {
            if (insc.getDisciplina().getId().equals(disciplina.getId())) {
                throw new Exception("Não é possível apagar uma disciplina que já possui alunos inscritos.");
            }
        }

        gerenciadorDeDados.removerDisciplinaDoEdital(editalAtualizado, disciplina);
    }
}
