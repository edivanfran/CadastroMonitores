package br.com.monitoria.servico;

import br.com.monitoria.GerenciadorDeDados;
import br.com.monitoria.excecoes.PesosInvalidosException;
import br.com.monitoria.model.EditalDeMonitoria;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EditalService {

    private GerenciadorDeDados gerenciadorDeDados;

    public EditalService() {
        this.gerenciadorDeDados = GerenciadorDeDados.getInstancia();
    }

    public void cadastrarEdital(String dataInicioStr, String dataFimStr, Double pesoCre, Double pesoNota) throws Exception {
        // Validação de pesos
        if (pesoCre + pesoNota != 1.0) {
            throw new Exception("A soma dos valores dos pesos deve ser igual a 1.");
        }

        // Validação e conversão de datas
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataInicio;
        LocalDate dataFim;
        try {
            dataInicio = LocalDate.parse(dataInicioStr, formatador);
            dataFim = LocalDate.parse(dataFimStr, formatador);
        } catch (DateTimeParseException ex) {
            throw new Exception("Formato de data inválido. Siga o padrão dd/mm/aaaa.");
        }

        if (dataInicio.isBefore(LocalDate.now())) {
            throw new Exception("A data inicial não pode ser uma data que já passou.");
        }
        if (dataFim.isBefore(dataInicio)) {
            throw new Exception("A data final não pode ser antes da data inicial.");
        }
        if (dataFim.isBefore(LocalDate.now())) {
            throw new Exception("A data final não pode ser uma data que já passou.");
        }

        // Criação e persistência
        List<EditalDeMonitoria> editais = gerenciadorDeDados.getTodosOsEditais();
        String numeroEdital = "Edital " + (editais.size() + 1);

        try {
            EditalDeMonitoria novoEdital = new EditalDeMonitoria(
                    numeroEdital,
                    dataInicio,
                    dataFim,
                    pesoCre,
                    pesoNota
            );
            gerenciadorDeDados.salvarEdital(novoEdital);
        } catch (PesosInvalidosException ex) {
            // Re-lança como uma exceção mais genérica para a camada de visão
            throw new Exception("Erro ao cadastrar edital: " + ex.getMessage());
        }
    }
}
