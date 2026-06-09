package br.com.monitoria;

import br.com.monitoria.model.Disciplina;
import br.com.monitoria.model.EditalDeMonitoria;
import br.com.monitoria.servico.DisciplinaService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TelaGerenciarDisciplinas extends TelaBase {

    private EditalDeMonitoria edital;
    private JList<Disciplina> listaDisciplinas;
    private DefaultListModel<Disciplina> listModel;
    private JTextField campoNomeDisciplina;
    private JSpinner spinnerVagasRemuneradas;
    private JSpinner spinnerVagasVoluntarias;
    private JButton botaoAdicionar;
    private JButton botaoSalvar;
    private JButton botaoApagar;
    private JButton botaoFechar;

    private DisciplinaService disciplinaService;

    public TelaGerenciarDisciplinas(EditalDeMonitoria edital) {
        super("Gerenciar Disciplinas do Edital " + edital.getNumero());
        this.edital = edital;
        this.disciplinaService = new DisciplinaService();
        setSize(700, 500);
    }

    @Override
    public void inicializar() {
        this.edital = disciplinaService.buscarEditalPorId(this.edital.getId());
        super.inicializar();
    }

    @Override
    protected void criarComponentes() {
        // Lista de disciplinas
        criarListaDisciplinas();

        // Campos de edição à direita
        criarCamposDeEdicao();

        // Botões de ação na parte inferior
        criarBotoesDeAcao();

        // Adicionar listener para a lista
        listaDisciplinas.addListSelectionListener(new OuvinteSelecaoLista());
    }

    private void criarListaDisciplinas() {
        JLabel labelLista = criarLabel("Disciplinas do Edital:", Estilos.FONTE_NORMAL);
        labelLista.setBounds(20, 20, 200, 25);
        painelPrincipal.add(labelLista);

        listModel = new DefaultListModel<>();
        List<Disciplina> disciplinasDoEdital = edital.getDisciplinas();
        if (disciplinasDoEdital != null) {
            for (Disciplina disciplina : disciplinasDoEdital) {
                listModel.addElement(disciplina);
            }
        }

        listaDisciplinas = new JList<>(listModel);
        listaDisciplinas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaDisciplinas.setFont(Estilos.FONTE_NORMAL);

        JScrollPane scrollPane = new JScrollPane(listaDisciplinas);
        scrollPane.setBounds(20, 50, 250, 300);
        painelPrincipal.add(scrollPane);
    }

    private void criarCamposDeEdicao() {
        JLabel labelNome = criarLabel("Nome:", Estilos.FONTE_NORMAL);
        labelNome.setBounds(310, 40, 100, 40);
        painelPrincipal.add(labelNome);
        campoNomeDisciplina = new JTextField();
        campoNomeDisciplina.setBounds(360, 40, 200, 40);
        campoNomeDisciplina.setFont(Estilos.FONTE_NORMAL);
        painelPrincipal.add(campoNomeDisciplina);

        JLabel labelRemuneradas = criarLabel("Vagas Remuneradas:", Estilos.FONTE_NORMAL);
        labelRemuneradas.setBounds(310, 130, 150, 40);
        painelPrincipal.add(labelRemuneradas);
        spinnerVagasRemuneradas = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        spinnerVagasRemuneradas.setBounds(450, 130, 100, 40);
        spinnerVagasRemuneradas.setFont(Estilos.FONTE_NORMAL);
        painelPrincipal.add(spinnerVagasRemuneradas);

        JLabel labelVoluntarias = criarLabel("Vagas Voluntárias:", Estilos.FONTE_NORMAL);
        labelVoluntarias.setBounds(310, 180, 150, 40);
        painelPrincipal.add(labelVoluntarias);
        spinnerVagasVoluntarias = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        spinnerVagasVoluntarias.setBounds(450, 180, 100, 40);
        spinnerVagasVoluntarias.setFont(Estilos.FONTE_NORMAL);
        painelPrincipal.add(spinnerVagasVoluntarias);
    }

    private void criarBotoesDeAcao() {
        int y = 400;
        int width = 150;
        int height = 40;
        int gap = 10;
        int x = 20;

        botaoAdicionar = criarBotao("Adicionar Nova", new OuvinteBotaoAdicionar());
        botaoAdicionar.setBounds(x, y, width, height);
        painelPrincipal.add(botaoAdicionar);

        x += width + gap;
        botaoSalvar = criarBotao("Salvar Alterações", new OuvinteBotaoSalvar());
        botaoSalvar.setBounds(x, y, width, height);
        painelPrincipal.add(botaoSalvar);

        x += width + gap;
        botaoApagar = criarBotao("Apagar Selecionada", new OuvinteBotaoApagar());
        botaoApagar.setBackground(Estilos.COR_PERIGO);
        botaoApagar.setBounds(x, y, width, height);
        painelPrincipal.add(botaoApagar);

        x += width + gap;
        botaoFechar = criarBotaoSecundario("Fechar", e -> dispose());
        botaoFechar.setBounds(x, y, 100, height);
        painelPrincipal.add(botaoFechar);
    }

    private void limparCampos() {
        listaDisciplinas.clearSelection();
        campoNomeDisciplina.setText("");
        spinnerVagasRemuneradas.setValue(0);
        spinnerVagasVoluntarias.setValue(0);
        campoNomeDisciplina.setEditable(true);
        campoNomeDisciplina.setBackground(Color.WHITE);
    }

    private class OuvinteSelecaoLista implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                Disciplina selecionada = listaDisciplinas.getSelectedValue();
                if (selecionada != null) {
                    campoNomeDisciplina.setText(selecionada.getNomeDisciplina());
                    spinnerVagasRemuneradas.setValue(selecionada.getVagasRemuneradas());
                    spinnerVagasVoluntarias.setValue(selecionada.getVagasVoluntarias());
                    campoNomeDisciplina.setEditable(false);
                    campoNomeDisciplina.setBackground(Estilos.COR_FUNDO);
                }
            }
        }
    }

    private class OuvinteBotaoAdicionar implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String nome = campoNomeDisciplina.getText().trim();
                int vagasRemuneradas = (int) spinnerVagasRemuneradas.getValue();
                int vagasVoluntarias = (int) spinnerVagasVoluntarias.getValue();

                Disciplina novaDisciplina = disciplinaService.adicionarDisciplina(edital, nome, vagasRemuneradas, vagasVoluntarias);
                
                listModel.addElement(novaDisciplina);
                mostrarSucesso("Disciplina adicionada com sucesso!");
                limparCampos();
            } catch (Exception ex) {
                mostrarErro(ex.getMessage());
            }
        }
    }

    private class OuvinteBotaoSalvar implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Disciplina selecionada = listaDisciplinas.getSelectedValue();
            if (selecionada == null) {
                mostrarErro("Selecione uma disciplina para salvar.");
                return;
            }

            try {
                int novasVagasRem = (int) spinnerVagasRemuneradas.getValue();
                int novasVagasVol = (int) spinnerVagasVoluntarias.getValue();
                
                disciplinaService.salvarDisciplina(edital, selecionada, novasVagasRem, novasVagasVol);
                
                // Atualiza a UI
                mostrarSucesso("Alterações salvas com sucesso!");
                listaDisciplinas.repaint();
                limparCampos();
            } catch (Exception ex) {
                // Em caso de erro de regra de negócio, reseta os spinners para os valores originais
                spinnerVagasRemuneradas.setValue(selecionada.getVagasRemuneradas());
                spinnerVagasVoluntarias.setValue(selecionada.getVagasVoluntarias());
                mostrarErro(ex.getMessage());
            }
        }
    }

    private class OuvinteBotaoApagar implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Disciplina selecionada = listaDisciplinas.getSelectedValue();
            if (selecionada == null) {
                mostrarErro("Selecione uma disciplina para apagar.");
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(
                TelaGerenciarDisciplinas.this,
                "Tem certeza que deseja apagar a disciplina '" + selecionada.getNomeDisciplina() + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    disciplinaService.apagarDisciplina(edital, selecionada);
                    listModel.removeElement(selecionada);
                    mostrarSucesso("Disciplina apagada com sucesso.");
                    limparCampos();
                } catch (Exception ex) {
                    mostrarErro(ex.getMessage());
                }
            }
        }
    }
}
