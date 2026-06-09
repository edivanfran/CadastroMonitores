package br.com.monitoria;

import br.com.monitoria.excecoes.ValidacaoException;
import br.com.monitoria.excecoes.VagasEsgotadasException;
import br.com.monitoria.model.Disciplina;
import br.com.monitoria.model.EditalDeMonitoria;
import br.com.monitoria.servico.InscricaoService;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaInscreverEmEditalAluno extends TelaBase {

    private EditalDeMonitoria edital;
    private JFormattedTextField dataInicio;
    private JFormattedTextField dataFinal;
    private JComboBox<Disciplina> campoDeDisciplina;
    private JSpinner campoCRE;
    private JSpinner campoNota;
    private JComboBox<PreferenciaInscricao> campoPreferencia;
    private JSpinner campoOrdemPreferencia;
    private JButton botaoInscrever;

    private InscricaoService inscricaoService;

    public TelaInscreverEmEditalAluno(EditalDeMonitoria edital) {
        super("Inscrever-se no Edital " + edital.getNumero());
        this.edital = edital;
        this.inscricaoService = new InscricaoService();
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void inicializar() {
        // Busca uma instância gerenciada do edital para evitar LazyInitializationException
        // e garantir que a lista de disciplinas no JComboBox seja consistente.
        this.edital = GerenciadorDeDados.getInstancia().buscarEditalPorId(this.edital.getId());
        super.inicializar();
    }

    @Override
    protected void criarComponentes() {
        criarTabel();
        criarCampos();
        criarBotoes();
        preencherCampos();
    }

    public void criarTabel() {
        JLabel dataInicial = new JLabel("Data Inicial:");
        dataInicial.setFont(Estilos.FONTE_NORMAL);
        dataInicial.setBounds(40, 30, 100, 40);
        painelPrincipal.add(dataInicial);

        JLabel dataFinalLabel = new JLabel("Data Final:");
        dataFinalLabel.setFont(Estilos.FONTE_NORMAL);
        dataFinalLabel.setBounds(250, 30, 100, 40);
        painelPrincipal.add(dataFinalLabel);

        JLabel disciplinaLabel = new JLabel("Selecione a Disciplina:");
        disciplinaLabel.setFont(Estilos.FONTE_NORMAL);
        disciplinaLabel.setBounds(40, 90, 178, 40);
        painelPrincipal.add(disciplinaLabel);

        JLabel creLabel = new JLabel("Nota do CRE:");
        creLabel.setFont(Estilos.FONTE_NORMAL);
        creLabel.setBounds(40, 150, 120, 40);
        painelPrincipal.add(creLabel);

        JLabel notaLabel = new JLabel("Nota da Disciplina:");
        notaLabel.setFont(Estilos.FONTE_NORMAL);
        notaLabel.setBounds(40, 210, 160, 40);
        painelPrincipal.add(notaLabel);

        JLabel ordemLabel = new JLabel("Ordem de Preferência:");
        ordemLabel.setFont(Estilos.FONTE_NORMAL);
        ordemLabel.setBounds(40, 270, 200, 40);
        painelPrincipal.add(ordemLabel);

        JLabel tipoVagaLabel = new JLabel("Tipo de Vaga Preferencial:");
        tipoVagaLabel.setFont(Estilos.FONTE_NORMAL);
        tipoVagaLabel.setBounds(40, 330, 220, 40);
        painelPrincipal.add(tipoVagaLabel);
    }

    private void criarCampos() {
        try {
            MaskFormatter mascara = new MaskFormatter("##/##/####");
            dataInicio = new JFormattedTextField(mascara);
            dataInicio.setBounds(150, 30, 80, 40);
            dataInicio.setHorizontalAlignment(SwingConstants.CENTER);
            dataInicio.setEditable(false);
            painelPrincipal.add(dataInicio);

            dataFinal = new JFormattedTextField(mascara);
            dataFinal.setBounds(340, 30, 80, 40);
            dataFinal.setEditable(false);
            painelPrincipal.add(dataFinal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        campoDeDisciplina = new JComboBox<>();
        campoDeDisciplina.setBounds(230, 90, 250, 40);
        painelPrincipal.add(campoDeDisciplina);

        // Campo para colocar o CRE
        SpinnerNumberModel modelCRE = new SpinnerNumberModel(70, 0, 100, 1);
        campoCRE = new JSpinner(modelCRE);
        campoCRE.setBounds(250, 150, 80, 40);
        painelPrincipal.add(campoCRE);

        // Campo para colocar a nota da disciplina
        SpinnerNumberModel modelNota = new SpinnerNumberModel(70, 0, 100, 1);
        campoNota = new JSpinner(modelNota);
        campoNota.setBounds(250, 210, 80, 40);
        painelPrincipal.add(campoNota);

        // Campo para definir a ordem de preferência
        SpinnerNumberModel modelOrdem = new SpinnerNumberModel(1, 1, 10, 1);
        campoOrdemPreferencia = new JSpinner(modelOrdem);
        campoOrdemPreferencia.setBounds(250, 270, 80, 40);
        painelPrincipal.add(campoOrdemPreferencia);

        // Campo para preferencia
        campoPreferencia = new JComboBox<>(PreferenciaInscricao.values());
        campoPreferencia.setBounds(270, 330, 210, 40);
        painelPrincipal.add(campoPreferencia);
    }

    private void criarBotoes() {
        botaoInscrever = criarBotao("Confirmar Inscrição", new OuvinteBotaoInscrever());
        botaoInscrever.setBounds(150, 420, 200, 40);
        botaoInscrever.setBackground(Estilos.COR_SUCESSO);
        painelPrincipal.add(botaoInscrever);

        JButton botaoVoltar = criarBotao("Voltar", e -> dispose());
        botaoVoltar.setBounds(370, 420, 120, 40);
        botaoVoltar.setBackground(Estilos.COR_CINZA);
        painelPrincipal.add(botaoVoltar);
    }

    private void preencherCampos() {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dataInicio.setText(edital.getDataInicio().format(formatador));
        dataFinal.setText(edital.getDataLimite().format(formatador));

        campoDeDisciplina.removeAllItems();
        List<Disciplina> disciplinas = edital.getDisciplinas();
        for (Disciplina disciplina : disciplinas) {
            campoDeDisciplina.addItem(disciplina);
        }
    }

    private class OuvinteBotaoInscrever implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Coleta os dados da tela
            Disciplina disciplinaSelecionada = (Disciplina) campoDeDisciplina.getSelectedItem();
            double cre = ((Number) campoCRE.getValue()).doubleValue();
            double nota = ((Number) campoNota.getValue()).doubleValue();
            int ordemPreferencia = (Integer) campoOrdemPreferencia.getValue();
            PreferenciaInscricao preferenciaVaga = (PreferenciaInscricao) campoPreferencia.getSelectedItem();

            try {
                String mensagem = inscricaoService.inscreverAluno(edital, disciplinaSelecionada, cre, nota, ordemPreferencia, preferenciaVaga);
                mostrarSucesso(mensagem);
                dispose();
            } catch (ValidacaoException | VagasEsgotadasException ex) {
                // Erros esperados e amigáveis para o usuário
                mostrarErro(ex.getMessage());
            } catch (Exception ex) {
                // Erros inesperados
                mostrarErro("Ocorreu um erro inesperado ao realizar a inscrição.");
                ex.printStackTrace(); // Loga o erro para depuração
            }
        }
    }
}
