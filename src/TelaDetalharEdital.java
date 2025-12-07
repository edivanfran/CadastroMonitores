import java.awt.Font;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

public class TelaDetalharEdital extends TelaBase {

        private Font fonteLabel = new Font("Calibri", Font.BOLD, 18);
        private JFormattedTextField DataInicio;
        private JFormattedTextField DataFinal;
        private JComboBox campoDeDisciplina;
        private JSpinner pesoCRE;
        private JSpinner pesoNota;
        private JButton botaoEncerrarEdital;
        private JButton botaoEditarEdital;
        private JButton botaoClonarEdital;
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

            // Criar botoes na tela
            criarBotoes();
        }

        public void criarBotoes(){
            botaoEncerrarEdital = criarBotao("Encerrar Edital", e ->
                    mostrarAviso("Funcionalidade em desenvolvimento"));
            botaoEncerrarEdital.setBounds(50, 390, 170, 40);
            botaoEncerrarEdital.setBackground(Estilos.COR_PERIGO);
            painelPrincipal.add(botaoEncerrarEdital);

            botaoEditarEdital = criarBotao("Editar Edital", e ->
                    mostrarAviso("Funcionalidade em desenvolvimento"));
            botaoEditarEdital.setBounds(270, 390, 120, 40);
            painelPrincipal.add(botaoEditarEdital);

            botaoClonarEdital = criarBotao("Clonar Edital", e ->
                    mostrarAviso("Funcionalidade em desenvolvimento"));
            botaoClonarEdital.setBounds(440, 390, 120, 40);
            botaoClonarEdital.setBackground(Estilos.COR_AVISO);
            painelPrincipal.add(botaoClonarEdital);

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
            Disciplina.setBounds(40, 170, 178, 40);
            painelPrincipal.add(Disciplina);

            JLabel pesoCRE = new JLabel("Peso do CRE:");
            pesoCRE.setFont(fonteLabel);
            pesoCRE.setBounds(40, 250, 120, 40);
            painelPrincipal.add(pesoCRE);

            JLabel pesoNota = new JLabel("Peso da Nota:");
            pesoNota.setFont(fonteLabel);
            pesoNota.setBounds(40, 320, 120, 40);
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
            campoDeDisciplina.setBounds(220, 170, 170, 40);
            campoDeDisciplina.setEnabled(true);
            // colocar para detalhar a disciplina selecionada
            painelPrincipal.add(campoDeDisciplina);

            SpinnerNumberModel valor = new SpinnerNumberModel(10, 10, 100, 10);
            pesoCRE = new JSpinner(valor);
            pesoCRE.setValue(edital.getPesoCre());
            pesoCRE.setBounds(150, 250, 80, 40);
            pesoCRE.setEnabled(false);
            painelPrincipal.add(pesoCRE);

            SpinnerNumberModel valor1 = new SpinnerNumberModel(10, 10, 100, 10);
            pesoNota = new JSpinner(valor1);
            pesoNota.setValue(edital.getPesoNota());
            pesoNota.setBounds(150, 320, 80, 40);
            pesoNota.setEnabled(false);
            painelPrincipal.add(pesoNota);
        }

}
