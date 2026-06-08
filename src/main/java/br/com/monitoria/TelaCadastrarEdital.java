package br.com.monitoria;

import br.com.monitoria.servico.EditalService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaCadastrarEdital extends TelaEditalBase {

    private JButton botaoSalvar;
    private JButton botaoVoltar;
    private EditalService editalService;

    public TelaCadastrarEdital() {
        // Passa null porque não tem um edital cadastrado.
        super("Cadastrar Novo Edital", null);
        this.editalService = new EditalService();
    }

    @Override
    protected void criarComponentes() {
        criarLabels();
        criarCampos();
        criarBotoes();
        // Deixa que os campos sejam editáveis
        tornarCamposEditaveis(true);
    }

    private void criarBotoes() {
        botaoSalvar = criarBotao("Salvar", new OuvinteBotaoSalvar());
        botaoSalvar.setBounds(200, 390, 120, 40);
        botaoSalvar.setBackground(Estilos.COR_SUCESSO);
        painelPrincipal.add(botaoSalvar);

        botaoVoltar = criarBotaoSecundario("Voltar",
                e -> voltarParaTelaPrincipal());
        botaoVoltar.setBounds(350, 390, 120, 40);
        painelPrincipal.add(botaoVoltar);
    }

    private void voltarParaTelaPrincipal() {
        this.dispose();
    }

    private class OuvinteBotaoSalvar implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String dataInicioStr = dataInicio.getText();
            String dataFimStr = dataFinal.getText();
            Double cre = (Double) pesoCRE.getValue();
            Double nota = (Double) pesoNota.getValue();

            try {
                editalService.cadastrarEdital(dataInicioStr, dataFimStr, cre, nota);
                mostrarSucesso("Edital cadastrado com sucesso!");
                voltarParaTelaPrincipal();
            } catch (Exception ex) {
                mostrarErro(ex.getMessage());
            }
        }
    }

}
