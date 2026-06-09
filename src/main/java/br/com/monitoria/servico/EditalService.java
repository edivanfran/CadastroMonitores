package br.com.monitoria.servico;

import br.com.monitoria.GerenciadorDeDados;
import br.com.monitoria.SessaoUsuario;
import br.com.monitoria.excecoes.EditalAbertoException;
import br.com.monitoria.excecoes.EditalFechadoException;
import br.com.monitoria.excecoes.PermissaoNegadaException;
import br.com.monitoria.excecoes.PesosInvalidosException;
import br.com.monitoria.excecoes.PrazoVencidoException;
import br.com.monitoria.model.Coordenador;
import br.com.monitoria.model.EditalDeMonitoria;
import br.com.monitoria.model.EditalBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EditalService {

    private GerenciadorDeDados gerenciadorDeDados;
    private SessaoUsuario sessaoUsuario;

    public EditalService() {
        this.gerenciadorDeDados = GerenciadorDeDados.getInstancia();
        this.sessaoUsuario = SessaoUsuario.getInstancia();
    }

    public List<EditalDeMonitoria> listarTodosOsEditais() {
        return gerenciadorDeDados.getTodosOsEditais();
    }

    public void cadastrarEdital(String dataInicioStr, String dataFimStr, Double pesoCre, Double pesoNota) throws Exception {
        if (pesoCre + pesoNota != 1.0) {
            throw new Exception("A soma dos valores dos pesos deve ser igual a 1.");
        }

        LocalDate dataInicio = parseData(dataInicioStr);
        LocalDate dataFim = parseData(dataFimStr);
        validarDatasCadastro(dataInicio, dataFim);

        List<EditalDeMonitoria> editais = gerenciadorDeDados.getTodosOsEditais();
        String numeroEdital = "Edital " + (editais.size() + 1);

        try {
            EditalDeMonitoria novoEdital = new EditalBuilder()
                    .comNumero(numeroEdital)
                    .comDatas(dataInicio, dataFim)
                    .comPesoCre(pesoCre)
                    .comPesoNota(pesoNota)
                    .build();
            gerenciadorDeDados.salvarEdital(novoEdital);
        } catch (PesosInvalidosException ex) {
            throw new Exception("Erro ao cadastrar edital: " + ex.getMessage());
        }
    }

    public void salvarEdital(EditalDeMonitoria edital, String dataInicioStr, String dataFimStr, Double pesoCre, Double pesoNota) throws Exception {
        if (pesoCre + pesoNota != 1.0) {
            throw new Exception("A soma dos valores dos pesos deve ser igual a 1.");
        }

        LocalDate dataInicio = parseData(dataInicioStr);
        LocalDate dataFim = parseData(dataFimStr);
        validarDatasEdicao(dataInicio, dataFim);

        edital.setDataInicio(dataInicio);
        edital.setDataLimite(dataFim);
        edital.setPesoCre(pesoCre);
        edital.setPesoNota(pesoNota);
        gerenciadorDeDados.atualizarEdital(edital);
    }

    public void encerrarEdital(EditalDeMonitoria edital) throws PermissaoNegadaException, EditalFechadoException {
        validarPermissaoCoordenador();
        edital.fecharEdital((Coordenador) sessaoUsuario.getUsuarioLogado());
        gerenciadorDeDados.atualizarEdital(edital);
    }

    public void reabrirEdital(EditalDeMonitoria edital) throws PermissaoNegadaException, EditalAbertoException, PrazoVencidoException {
        validarPermissaoCoordenador();
        edital.reabrirEdital((Coordenador) sessaoUsuario.getUsuarioLogado());
        gerenciadorDeDados.atualizarEdital(edital);
    }

    public EditalDeMonitoria clonarEdital(EditalDeMonitoria edital) throws PermissaoNegadaException {
        validarPermissaoCoordenador();
        EditalDeMonitoria copiaEdital = edital.clonar();
        gerenciadorDeDados.salvarEdital(copiaEdital);
        return copiaEdital;
    }

    private void validarPermissaoCoordenador() throws PermissaoNegadaException {
        if (!(sessaoUsuario.getUsuarioLogado() instanceof Coordenador)) {
            throw new PermissaoNegadaException("Apenas coordenadores podem realizar esta ação.");
        }
    }

    private LocalDate parseData(String dataStr) throws Exception {
        try {
            return LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException ex) {
            throw new Exception("Formato de data inválido. Siga o padrão dd/mm/aaaa.");
        }
    }

    private void validarDatasCadastro(LocalDate dataInicio, LocalDate dataFim) throws Exception {
        if (dataInicio.isBefore(LocalDate.now())) {
            throw new Exception("A data inicial não pode ser uma data que já passou.");
        }
        if (dataFim.isBefore(dataInicio)) {
            throw new Exception("A data final não pode ser antes da data inicial.");
        }
    }

    private void validarDatasEdicao(LocalDate dataInicio, LocalDate dataFim) throws Exception {
        if (dataFim.isBefore(dataInicio)) {
            throw new Exception("A data final não pode ser antes da data inicial.");
        }
    }
}
