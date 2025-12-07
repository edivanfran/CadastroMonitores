import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public abstract class TelaEditalBase extends TelaBase {

    protected JFormattedTextField dataInicio;
    protected JFormattedTextField dataFinal;
    protected JComboBox<Disciplina> campoDeDisciplina;
    protected JSpinner pesoCRE;
    protected JSpinner pesoNota;
    protected EditalDeMonitoria edital;

    public TelaEditalBase(String titulo, EditalDeMonitoria edital, CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super(titulo, central, persistencia, nomeArquivo);
        this.edital = edital;
        setSize(700, 500);
    }

    protected void criarLabels() {
        JLabel dataInicial = new JLabel("Data Inicial:");
        dataInicial.setFont(Estilos.FONTE_NORMAL);
        dataInicial.setBounds(40, 30, 100, 40);
        painelPrincipal.add(dataInicial);

        JLabel dataFinalLabel = new JLabel("Data Final:");
        dataFinalLabel.setFont(Estilos.FONTE_NORMAL);
        dataFinalLabel.setBounds(40, 100, 100, 40);
        painelPrincipal.add(dataFinalLabel);

        JLabel disciplinaLabel = new JLabel("Selecione a Disciplina:");
        disciplinaLabel.setFont(Estilos.FONTE_NORMAL);
        disciplinaLabel.setBounds(40, 170, 178, 40);
        painelPrincipal.add(disciplinaLabel);

        JLabel pesoCRELabel = new JLabel("Peso do CRE:");
        pesoCRELabel.setFont(Estilos.FONTE_NORMAL);
        pesoCRELabel.setBounds(40, 250, 120, 40);
        painelPrincipal.add(pesoCRELabel);

        JLabel pesoNotaLabel = new JLabel("Peso da Nota:");
        pesoNotaLabel.setFont(Estilos.FONTE_NORMAL);
        pesoNotaLabel.setBounds(40, 320, 120, 40);
        painelPrincipal.add(pesoNotaLabel);
    }

    protected void criarCampos() {
        try {
            MaskFormatter mascara = new MaskFormatter("##/##/####");
            dataInicio = new JFormattedTextField(mascara);
            dataInicio.setBounds(170, 30, 80, 40);
            painelPrincipal.add(dataInicio);

            dataFinal = new JFormattedTextField(mascara);
            dataFinal.setBounds(170, 100, 80, 40);
            painelPrincipal.add(dataFinal);
        } catch (ParseException e) {
            mostrarAviso("Não é possível inserir isso.");
            e.printStackTrace();
        }

        campoDeDisciplina = new JComboBox<>();
        campoDeDisciplina.setBounds(220, 170, 170, 40);
        painelPrincipal.add(campoDeDisciplina);

        SpinnerNumberModel valorCRE = new SpinnerNumberModel(0.5, 0.1, 1, 0.1);
        pesoCRE = new JSpinner(valorCRE);
        pesoCRE.setBounds(150, 250, 80, 40);
        painelPrincipal.add(pesoCRE);

        SpinnerNumberModel valorNota = new SpinnerNumberModel(0.5, 0.1, 1, 0.1);
        pesoNota = new JSpinner(valorNota);
        pesoNota.setBounds(150, 320, 80, 40);
        painelPrincipal.add(pesoNota);

        if (edital != null) {
            preencherCamposComDados(edital);
        }
    }

    public void preencherCamposComDados(EditalDeMonitoria edital) {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataInicio.setText(edital.getDataInicio().format(formatador));
        dataFinal.setText(edital.getDataLimite().format(formatador));

        campoDeDisciplina.removeAllItems();
        ArrayList<Disciplina> disciplinas = edital.getDisciplinas();
        for (Disciplina disciplina : disciplinas) {
            campoDeDisciplina.addItem(disciplina);
        }

        pesoCRE.setValue(edital.getPesoCre());
        pesoNota.setValue(edital.getPesoNota());
    }

    protected void tornarCamposEditaveis(boolean editavel) {
        dataInicio.setEditable(editavel);
        dataFinal.setEditable(editavel);
        campoDeDisciplina.setEnabled(true);
        pesoCRE.setEnabled(editavel);
        pesoNota.setEnabled(editavel);
    }

    protected abstract void criarComponentes();
}
