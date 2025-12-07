import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TelaCadastrarEdital extends TelaEditalBase {

    private JButton botaoSalvar;
    private JButton botaoVoltar;

    public TelaCadastrarEdital(CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        // Passa null porque não tem um edital cadastrado.
        super("Cadastrar Novo Edital", null, central, persistencia, nomeArquivo);
    }

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
            if (((Double) pesoCRE.getValue() + (Double) pesoNota.getValue()) != 1.0) {
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

            if (dataInicioFormatada.isBefore(LocalDate.now())) {
                mostrarErro("A data inicial não pode ser uma data que já passou.");
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

            try {
                String numeroEdital = "Placeholder_";
                EditalDeMonitoria novoEdital = new EditalDeMonitoria(
                        numeroEdital,
                        dataInicioFormatada,
                        dataFinalFormatada,
                        (Double) pesoCRE.getValue(),
                        (Double) pesoNota.getValue()
                );

                // Adicionar disciplinas (funcionalidade a ser expandida)
                // Por enquanto, o edital está sendo criado sem disciplinas.


                getCentral().adicionarEdital(novoEdital);
                getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
                mostrarSucesso("Edital cadastrado com sucesso!");

                voltarParaTelaPrincipal();
            } catch (Exception ex) {
                mostrarErro("Erro ao cadastrar edital: " + ex.getMessage());
            }
        }
    }

}
