import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

public class TelaDetalharEdital extends TelaBase {

        private Font fonteLabel = new Font("Calibri", Font.BOLD, 18);
        private JFormattedTextField dataInicio;
        private JFormattedTextField dataFinal;
        private JComboBox<Disciplina> campoDeDisciplina;
        private JSpinner pesoCRE;
        private JSpinner pesoNota;
        private JButton botaoSelecionarDisciplina;
        private JButton botaoEncerrarEdital;
        private JButton botaoEditarEdital;
        private JButton botaoClonarEdital;
        private JButton botaoSalvar;
        private JButton botaoCancelar;
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
            botaoSelecionarDisciplina = criarBotao("Selecionar Disciplina", e ->
                    mostrarAviso("Funcionalidade em desenvolvimento"));
            botaoSelecionarDisciplina.setBounds(430, 170, 180, 40);
            botaoSelecionarDisciplina.setBackground(Estilos.COR_AVISO);
            painelPrincipal.add(botaoSelecionarDisciplina);

            botaoEncerrarEdital = criarBotao("Encerrar Edital", e ->
                    mostrarAviso("Funcionalidade em desenvolvimento"));
            botaoEncerrarEdital.setBounds(50, 390, 170, 40);
            botaoEncerrarEdital.setBackground(Estilos.COR_PERIGO);
            painelPrincipal.add(botaoEncerrarEdital);

            botaoEditarEdital = criarBotao("Editar Edital", new OuvinteBotaoEditar());
            botaoEditarEdital.setBounds(270, 390, 120, 40);
            painelPrincipal.add(botaoEditarEdital);

            botaoClonarEdital = criarBotao("Clonar Edital", e ->
                    mostrarAviso("Funcionalidade em desenvolvimento"));
            botaoClonarEdital.setBounds(440, 390, 120, 40);
            botaoClonarEdital.setBackground(Estilos.COR_AVISO);
            painelPrincipal.add(botaoClonarEdital);

            botaoSalvar = criarBotao("Salvar", new OuvinteBotaoSalvar());
            botaoSalvar.setBounds(200, 390, 120, 40);
            botaoSalvar.setBackground(Estilos.COR_SUCESSO);
            botaoSalvar.setVisible(false);
            painelPrincipal.add(botaoSalvar);

            botaoCancelar = criarBotao("Cancelar", new OuvinteBotaoCancelar());
            botaoCancelar.setBounds(440, 390, 120, 40);
            botaoCancelar.setBackground(Estilos.COR_CINZA);
            botaoCancelar.setVisible(false);
            painelPrincipal.add(botaoCancelar);

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

            // Desabilitar os campos para edição
            dataInicio.setEditable(false);
            dataFinal.setEditable(false);

            campoDeDisciplina = new JComboBox<>();
            campoDeDisciplina.setBounds(220, 170, 170, 40);
            campoDeDisciplina.setEnabled(true);
            painelPrincipal.add(campoDeDisciplina);

            SpinnerNumberModel valor = new SpinnerNumberModel(0.5, 0.1, 1, 0.1);
            pesoCRE = new JSpinner(valor);
            pesoCRE.setBounds(150, 250, 80, 40);
            pesoCRE.setEnabled(false);
            painelPrincipal.add(pesoCRE);

            SpinnerNumberModel valor1 = new SpinnerNumberModel(0.5, 0.1, 1, 0.1);
            pesoNota = new JSpinner(valor1);
            pesoNota.setBounds(150, 320, 80, 40);
            pesoNota.setEnabled(false);
            painelPrincipal.add(pesoNota);

            preencherCamposComDados(edital);
        }

        public void preencherCamposComDados(EditalDeMonitoria edital) {
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dataInicio.setText(edital.getDataInicio().format(formatador));
            dataFinal.setText(edital.getDataLimite().format(formatador));

            ArrayList<Disciplina> disciplinas = edital.getDisciplinas();
            for (Disciplina disciplina: disciplinas) {
                campoDeDisciplina.addItem(disciplina);
            }

            pesoCRE.setValue(edital.getPesoCre());
            pesoNota.setValue(edital.getPesoNota());

        }

        private void tornarCamposNaoEditaveis() {
            dataFinal.setEditable(false);
            campoDeDisciplina.setEnabled(true);
            pesoCRE.setEnabled(false);
            pesoNota.setEnabled(false);

            botaoEncerrarEdital.setVisible(true);
            botaoEditarEdital.setVisible(true);
            botaoClonarEdital.setVisible(true);
            botaoSalvar.setVisible(false);
            botaoCancelar.setVisible(false);
        }

        private class OuvinteBotaoEditar implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                dataFinal.setEditable(true);
                campoDeDisciplina.setEnabled(true);
                pesoCRE.setEnabled(true);
                pesoNota.setEnabled(true);

                botaoEncerrarEdital.setVisible(false);
                botaoEditarEdital.setVisible(false);
                botaoClonarEdital.setVisible(false);

                botaoSalvar.setVisible(true);
                botaoCancelar.setVisible(true);
            }

        }

        private class OuvinteBotaoCancelar implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                preencherCamposComDados(edital);

                tornarCamposNaoEditaveis();
            }
        }

        private class OuvinteBotaoSalvar implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if ( ((Double) pesoCRE.getValue() + (Double) pesoNota.getValue()) != 1.0) {
                    mostrarErro("A soma dos valores dos pesos deve ser igual a 1.");
                    return;
                }

                DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dataInicioFormatada;
                LocalDate dataFinalFormatada;
                try {
                    dataInicioFormatada = LocalDate.parse(dataInicio.getText(), formatador);
                    dataFinalFormatada = LocalDate.parse(dataFinal.getText(), formatador);
                } catch (java.time.format.DateTimeParseException ex) {
                    mostrarErro("Formato de data inválido. Siga o padrão dd/mm/aaaa.");
                    return;
                }

                if (dataFinalFormatada.isBefore(dataInicioFormatada)) {
                    mostrarErro("A data final não pode ser antes da data inicial.");
                    return;
                }
                if (dataFinalFormatada.isBefore(LocalDate.now())) {
                    mostrarErro("A data final não pode ser uma data que já passou.");
                    return;
                }

                edital.setDataLimite(dataFinalFormatada);
                edital.setPesoCre((Double) pesoCRE.getValue());
                edital.setPesoNota((Double) pesoNota.getValue());

                // Salvar no arquivo
                getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
                mostrarSucesso("Edital salvo com sucesso!");

                tornarCamposNaoEditaveis();
            }

        }

        private class OuvinteBotaoClonar implements ActionListener {

            public void actionPerformed(ActionEvent e) {

            }

        }

}
