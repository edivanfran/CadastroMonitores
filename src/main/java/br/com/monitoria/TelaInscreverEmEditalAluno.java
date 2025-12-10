package br.com.monitoria;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TelaInscreverEmEditalAluno extends TelaBase {

    private EditalDeMonitoria edital;
    protected JFormattedTextField dataInicio;
    protected JFormattedTextField dataFinal;
    private JComboBox campoDeDisciplina;
    private JSpinner notaParaCRE;
    private JSpinner notaParaNota;
    private JButton botaoInscrever;

    public TelaInscreverEmEditalAluno(EditalDeMonitoria edital, CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Detalhar Edital - Aluno", central, persistencia, nomeArquivo);
        this.edital = edital;
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        criarComponentes();
    }

    protected void criarComponentes() {
        criarTabel();
        criarCampos();
        criarBotoes();
        preencherCampos();
    }

    public void criarBotoes() {
        botaoInscrever = criarBotao("Inscrever-se", e -> mostrarAviso("Funcionalidade em desenvolvimento"));
        botaoInscrever.setBounds(165, 340, 120, 40);
        botaoInscrever.setBackground(Estilos.COR_SUCESSO);
        painelPrincipal.add(botaoInscrever);

        JButton botaoVoltar = criarBotao("Voltar", e -> dispose());
        botaoVoltar.setBounds(325, 340, 120, 40);
        botaoVoltar.setBackground(Estilos.COR_CINZA);
        painelPrincipal.add(botaoVoltar);
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
        CRELabel.setBounds(40, 200, 120, 40);
        painelPrincipal.add(CRELabel);

        JLabel NotaLabel = new JLabel("Nota da Disciplina:");
        NotaLabel.setFont(Estilos.FONTE_NORMAL);
        NotaLabel.setBounds(40, 270, 160, 40);
        painelPrincipal.add(NotaLabel);
    }

    public void criarCampos() {
        try {
            MaskFormatter mascara = new MaskFormatter("##/##/####");
            dataInicio = new JFormattedTextField(mascara);
            dataInicio.setBounds(150, 30, 80, 40);
            dataInicio.setHorizontalAlignment(SwingConstants.CENTER);
            painelPrincipal.add(dataInicio);

            dataFinal = new JFormattedTextField(mascara);
            dataFinal.setBounds(360, 30, 80, 40);
            painelPrincipal.add(dataFinal);
        } catch (ParseException e) {
            mostrarAviso("Não é possível inserir isso.");
            e.printStackTrace();
        }

        campoDeDisciplina = new JComboBox<>();
        campoDeDisciplina.setBounds(255, 90, 80, 40);
        painelPrincipal.add(campoDeDisciplina);

        SpinnerNumberModel valor = new SpinnerNumberModel(0, 0, 100, 1);
        notaParaCRE = new JSpinner(valor);
        notaParaCRE.setBounds(184, 200, 120, 40);
        painelPrincipal.add(notaParaCRE);

        SpinnerNumberModel valor1 = new SpinnerNumberModel(0, 0, 100, 1);
        notaParaNota = new JSpinner(valor1);
        notaParaNota.setBounds(184, 270, 120, 40);
        painelPrincipal.add(notaParaNota);
    }

    public void preencherCampos() {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dataInicio.setText(edital.getDataInicio().format(formatador));
        dataFinal.setText(edital.getDataLimite().format(formatador));

        campoDeDisciplina.removeAllItems();
        ArrayList<Disciplina> disciplinas = edital.getDisciplinas();
        for (Disciplina disciplina : disciplinas) {
            campoDeDisciplina.addItem(disciplina);
        }

    }

}
