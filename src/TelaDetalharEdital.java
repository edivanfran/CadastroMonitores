import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TelaDetalharEdital extends TelaEditalBase {

    private JButton botaoSelecionarDisciplina;
    private JButton botaoEncerrarEdital;
    private JButton botaoEditarEdital;
    private JButton botaoClonarEdital;
    private JButton botaoEditarDisciplina;
    private JButton botaoSalvar;
    private JButton botaoCancelar;

    public TelaDetalharEdital(EditalDeMonitoria edital, CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Detalhar Edital", edital, central, persistencia, nomeArquivo);
    }

    protected void criarComponentes() {
        criarLabels();
        criarCampos();
        criarBotoes();
        // Não deixa os campos serem editáveis
        tornarCamposEditaveis(false);
    }

    private void criarBotoes() {
        botaoSelecionarDisciplina = criarBotao("Ver Lista da Disciplina", e ->
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

        botaoClonarEdital = criarBotao("Clonar Edital", new OuvinteBotaoClonarEdital());
        botaoClonarEdital.setBounds(440, 390, 120, 40);
        botaoClonarEdital.setBackground(Estilos.COR_AVISO);
        painelPrincipal.add(botaoClonarEdital);

        // Botões do modo de edição (inicialmente invisíveis)
        botaoEditarDisciplina = criarBotao("Gerenciar Disciplinas", new OuvinteBotaoEditarDisciplina());
        botaoEditarDisciplina.setBounds(430, 170, 180, 40);
        botaoEditarDisciplina.setBackground(Estilos.COR_AVISO);
        botaoEditarDisciplina.setVisible(false);
        painelPrincipal.add(botaoEditarDisciplina);

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

    private void mudarVisibilidadeBotoes(boolean editando) {
        botaoEncerrarEdital.setVisible(!editando);
        botaoEditarEdital.setVisible(!editando);
        botaoClonarEdital.setVisible(!editando);
        botaoSelecionarDisciplina.setVisible(!editando);

        botaoEditarDisciplina.setVisible(editando);
        botaoSalvar.setVisible(editando);
        botaoCancelar.setVisible(editando);
    }

    private class OuvinteBotaoEditar implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            tornarCamposEditaveis(true);
            mudarVisibilidadeBotoes(true);
        }
    }

//    public class OuvinteBotaoEncerrarEdital implements ActionListener {

    public class OuvinteBotaoClonarEdital implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            EditalDeMonitoria copiaEdital = edital.clonar();
            getCentral().adicionarEdital(copiaEdital);
            getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
            mostrarSucesso("Edital clonado com sucesso!");
        }
    }
    
    private class OuvinteBotaoEditarDisciplina implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            TelaGerenciarDisciplinas telaDisciplinas = new TelaGerenciarDisciplinas(edital, getCentral(), getPersistencia(), getNomeArquivo());
            telaDisciplinas.inicializar();
            telaDisciplinas.addWindowListener(
                    new WindowListener() {
                        public void windowOpened(WindowEvent e) {

                        }

                        public void windowClosing(WindowEvent e) {

                        }

                        public void windowClosed(WindowEvent e) {
                            preencherCamposComDados(edital);
                        }

                        public void windowIconified(WindowEvent e) {

                        }

                        public void windowDeiconified(WindowEvent e) {

                        }

                        public void windowActivated(WindowEvent e) {

                        }

                        public void windowDeactivated(WindowEvent e) {

                        }
                    });
            telaDisciplinas.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }

    private class OuvinteBotaoCancelar implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            preencherCamposComDados(edital);
            tornarCamposEditaveis(false);
            mudarVisibilidadeBotoes(false);
        }
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
            } catch (Exception ex) {
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

            // Atualiza o edital existente
            edital.setDataInicio(dataInicioFormatada);
            edital.setDataLimite(dataFinalFormatada);
            edital.setPesoCre((Double) pesoCRE.getValue());
            edital.setPesoNota((Double) pesoNota.getValue());

            // Salva as alterações
            getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
            mostrarSucesso("Edital salvo com sucesso!");

            // Retorna ao modo de visualização
            tornarCamposEditaveis(false);
            mudarVisibilidadeBotoes(false);
        }
    }
}
