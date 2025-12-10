package br.com.monitoria;

import br.com.monitoria.excecoes.*;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

    public TelaInscreverEmEditalAluno(EditalDeMonitoria edital, CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Inscrever-se no Edital " + edital.getNumero(), central, persistencia, nomeArquivo);
        this.edital = edital;
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        criarComponentes();
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

        JLabel dataFinal = new JLabel("Data Final:");
        dataFinal.setFont(Estilos.FONTE_NORMAL);
        dataFinal.setBounds(250, 30, 100, 40);
        painelPrincipal.add(dataFinal);

        JLabel Disciplina = new JLabel("Selecione a Disciplina:");
        Disciplina.setFont(Estilos.FONTE_NORMAL);
        Disciplina.setBounds(40, 90, 178, 40);
        painelPrincipal.add(Disciplina);

        JLabel CRELabel = new JLabel("Nota do CRE:");
        CRELabel.setFont(Estilos.FONTE_NORMAL);
        CRELabel.setBounds(40, 150, 120, 40);
        painelPrincipal.add(CRELabel);

        JLabel NotaLabel = new JLabel("Nota da Disciplina:");
        NotaLabel.setFont(Estilos.FONTE_NORMAL);
        NotaLabel.setBounds(40, 210, 160, 40);
        painelPrincipal.add(NotaLabel);

        JLabel OrdemLabel = new JLabel("Ordem de Preferência:");
        OrdemLabel.setFont(Estilos.FONTE_NORMAL);
        OrdemLabel.setBounds(40, 270, 200, 40);
        painelPrincipal.add(OrdemLabel);

        JLabel TipoVagaLabel = new JLabel("Tipo de Vaga Preferencial:");
        TipoVagaLabel.setFont(Estilos.FONTE_NORMAL);
        TipoVagaLabel.setBounds(40, 330, 220, 40);
        painelPrincipal.add(TipoVagaLabel);
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

        SpinnerNumberModel modelCRE = new SpinnerNumberModel(70, 0, 100, 1);
        campoCRE = new JSpinner(modelCRE);
        campoCRE.setBounds(250, 150, 80, 40);
        painelPrincipal.add(campoCRE);

        SpinnerNumberModel modelNota = new SpinnerNumberModel(70, 0, 100, 1);
        campoNota = new JSpinner(modelNota);
        campoNota.setBounds(250, 210, 80, 40);
        painelPrincipal.add(campoNota);

        // Campo para ordem de preferência
        SpinnerNumberModel modelOrdem = new SpinnerNumberModel(1, 1, 10, 1);
        campoOrdemPreferencia = new JSpinner(modelOrdem);
        campoOrdemPreferencia.setBounds(250, 270, 80, 40);
        painelPrincipal.add(campoOrdemPreferencia);

        // Campo para preferência de vaga
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
        ArrayList<Disciplina> disciplinas = edital.getDisciplinas();
        for (Disciplina disciplina : disciplinas) {
            campoDeDisciplina.addItem(disciplina);
        }
    }

    private class OuvinteBotaoInscrever implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Coleta dos dados da tela
            Disciplina disciplinaSelecionada = (Disciplina) campoDeDisciplina.getSelectedItem();
            if (disciplinaSelecionada == null) {
                mostrarErro("Por favor, selecione uma disciplina.");
                return;
            }

            double cre = (Integer) campoCRE.getValue();
            double nota = (Integer) campoNota.getValue();
            int ordemPreferencia = (Integer) campoOrdemPreferencia.getValue();
            PreferenciaInscricao preferenciaVaga = (PreferenciaInscricao) campoPreferencia.getSelectedItem();
            Aluno alunoLogado = (Aluno) sessao.getUsuarioLogado();

            try {
                switch (preferenciaVaga) {
                    case SOMENTE_REMUNERADA:
                        realizarInscricao(alunoLogado, disciplinaSelecionada, cre, nota, Vaga.REMUNERADA, ordemPreferencia, preferenciaVaga);
                        break;
                    case SOMENTE_VOLUNTARIA:
                        realizarInscricao(alunoLogado, disciplinaSelecionada, cre, nota, Vaga.VOLUNTARIA, ordemPreferencia, preferenciaVaga);
                        break;
                    case REMUNERADA_OU_VOLUNTARIA:
                        try {
                            // Tenta primeiro a vaga remunerada
                            realizarInscricao(alunoLogado, disciplinaSelecionada, cre, nota, Vaga.REMUNERADA, ordemPreferencia, preferenciaVaga);
                        } catch (VagasEsgotadasException eRemunerada) {
                            // Se não conseguiu, tenta a voluntária
                            try {
                                mostrarAviso("Vagas remuneradas esgotadas. Tentando vaga voluntária...");
                                realizarInscricao(alunoLogado, disciplinaSelecionada, cre, nota, Vaga.VOLUNTARIA, ordemPreferencia, preferenciaVaga);
                            } catch (VagasEsgotadasException eVoluntaria) {
                                // Se também não conseguiu, informa o erro final
                                mostrarErro("Não há mais vagas remuneradas ou voluntárias para esta disciplina.");
                            }
                        }
                        break;
                }
            } catch (Exception ex) {
                // Captura outras exceções como EditalFechado, PrazoVencido, etc.
                mostrarErro(ex.getMessage());
            }
        }

        private void realizarInscricao(Aluno aluno, Disciplina disciplina, double cre, double nota, Vaga tipoVaga, int ordem, PreferenciaInscricao pref) throws EditalFechadoException, PrazoInscricaoVencidoException, DisciplinaNaoEncontradaException, ValoresInvalidosException, VagasEsgotadasException {
            edital.inscreverAluno(aluno, disciplina.getNomeDisciplina(), cre, nota, tipoVaga, ordem, pref);
            getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
            mostrarSucesso("Inscrição para vaga " + tipoVaga + " realizada com sucesso!");
            dispose();
        }
    }
}
