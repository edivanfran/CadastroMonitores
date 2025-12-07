import java.awt.Color;
import java.awt.Font;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.MaskFormatter;

public class TelaDetalharEdital extends TelaBase {

        private Font fonteLabel = new Font("Calibri", Font.BOLD, 18);
        private JFormattedTextField DataInicio;
        private JFormattedTextField DataFinal;
        private JComboBox campoDeDisciplina;
        private JSpinner PesoCRE;
        private JSpinner pesoNotaSpinner; // Renomeado para evitar conflito com a variável local
        private EditalDeMonitoria edital;

        public TelaDetalharEdital(EditalDeMonitoria edital, CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
            super("Detalhar Edital", central, persistencia, nomeArquivo);
            setSize(700, 500);
            this.edital = edital;

        }

        protected void criarComponentes() {
            // Criar Labels na tela
            criarLabel();

            // Criar Campos na tela
            criarCampos();

        }

        public void criarLabel() {
            JLabel dataInicial = new JLabel("Data Inicial:");
            dataInicial.setFont(fonteLabel);
            dataInicial.setBounds(40, 30, 100, 40);
            painelPrincipal.add(dataInicial);

            JLabel dataFinal = new JLabel("Data Final:");
            dataFinal.setFont(fonteLabel);
            dataFinal.setBounds(40, 100, 100, 40);
            painelPrincipal.add(dataFinal);

            JLabel Disciplina = new JLabel("Selecione a Disciplina:");
            Disciplina.setFont(fonteLabel);
            Disciplina.setBounds(40, 190, 178, 40);
            painelPrincipal.add(Disciplina);

            JLabel pesoCRE = new JLabel("Peso do CRE:");
            pesoCRE.setFont(fonteLabel);
            pesoCRE.setBounds(40, 270, 120, 40);
            painelPrincipal.add(pesoCRE);

            JLabel pesoNota = new JLabel("Peso da Nota:");
            pesoNota.setFont(fonteLabel);
            pesoNota.setBounds(40, 340, 120, 40);
            painelPrincipal.add(pesoNota);
        }

        public void criarCampos() {
            try {
                MaskFormatter mascara = new MaskFormatter("##/##/####");
                DataInicio = new JFormattedTextField(mascara);
                DataInicio.setBounds(170, 30, 80, 40);
                painelPrincipal.add(DataInicio);

                DataFinal = new JFormattedTextField(mascara);
                DataFinal.setBounds(170, 100, 80, 40);
                painelPrincipal.add(DataFinal);
            } catch (ParseException e) {
                mostrarAviso("Não é possível inserir isso.");
                e.printStackTrace();
            }

            // Preenche os campos com os dados do edital e os desabilita para edição
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DataInicio.setText(edital.getDataInicio().format(formatador));
            DataInicio.setEditable(false);

            DataFinal.setText(edital.getDataLimite().format(formatador));
            DataFinal.setEditable(false);

            campoDeDisciplina = new JComboBox<>();
            ArrayList<Disciplina> disciplinas = edital.getDisciplinas();
            for (Disciplina disciplina: disciplinas) {
                campoDeDisciplina.addItem(disciplina);
            }
            campoDeDisciplina.setBounds(255, 190, 170, 40);
            campoDeDisciplina.setEnabled(true);
            // colocar para detalhar a disciplina selecionada
            painelPrincipal.add(campoDeDisciplina);

            SpinnerNumberModel valor = new SpinnerNumberModel(10, 10, 100, 10);
            PesoCRE = new JSpinner(valor);
            PesoCRE.setValue(edital.getPesoCre());
            PesoCRE.setBounds(150, 270, 80, 40);
            PesoCRE.setEnabled(false);
            painelPrincipal.add(PesoCRE);

            SpinnerNumberModel valor1 = new SpinnerNumberModel(10, 10, 100, 10);
            pesoNotaSpinner = new JSpinner(valor1);
            pesoNotaSpinner.setValue(edital.getPesoNota());
            pesoNotaSpinner.setBounds(150, 340, 80, 40);
            pesoNotaSpinner.setEnabled(false);
            painelPrincipal.add(pesoNotaSpinner);
        }

}
