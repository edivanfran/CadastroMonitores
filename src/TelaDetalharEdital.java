import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import excecoes.EditalAbertoException;
import excecoes.EditalFechadoException;
import excecoes.PermissaoNegadaException;
import excecoes.PrazoVencidoException;

public class TelaDetalharEdital extends TelaEditalBase {

    private JButton botaoSelecionarDisciplina;
    private JButton botaoEncerrarReabrir;
    private JButton botaoEditarEdital;
    private JButton botaoClonarEdital;
    private JButton botaoEditarDisciplina;
    private JButton botaoSalvar;
    private JButton botaoCancelar;

    public TelaDetalharEdital(EditalDeMonitoria edital, CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Detalhar Edital", edital, central, persistencia, nomeArquivo);
    }

    public void inicializar() {
        super.inicializar();
        atualizarBotaoEncerramento();
    }

    protected void criarComponentes() {
        criarLabels();
        criarCampos();
        criarBotoes();
        tornarCamposEditaveis(false);
    }

    private void criarBotoes() {
        botaoSelecionarDisciplina = criarBotao("Ver Lista da Disciplina", e ->
                mostrarAviso("Funcionalidade em desenvolvimento"));
        botaoSelecionarDisciplina.setBounds(430, 170, 180, 40);
        botaoSelecionarDisciplina.setBackground(Estilos.COR_AVISO);
        painelPrincipal.add(botaoSelecionarDisciplina);

        botaoEncerrarReabrir = criarBotao("", new OuvinteBotaoEncerrarReabrir());
        botaoEncerrarReabrir.setBounds(50, 390, 200, 40);
        painelPrincipal.add(botaoEncerrarReabrir);

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

    private void atualizarBotaoEncerramento() {
        if (edital.jaAcabou()) {
            botaoEncerrarReabrir.setText("Encerrado Definitivamente");
            botaoEncerrarReabrir.setBackground(Estilos.COR_CINZA);
            botaoEncerrarReabrir.setEnabled(false);
        } else if (edital.isAberto()) {
            botaoEncerrarReabrir.setText("Encerrar Inscrições");
            botaoEncerrarReabrir.setBackground(Estilos.COR_PERIGO);
            botaoEncerrarReabrir.setEnabled(true);
        } else {
            botaoEncerrarReabrir.setText("Reabrir Inscrições");
            botaoEncerrarReabrir.setBackground(Estilos.COR_SUCESSO);
            botaoEncerrarReabrir.setEnabled(true);
        }
    }

    private void mudarVisibilidadeBotoes(boolean editando) {
        botaoEncerrarReabrir.setVisible(!editando);
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

    public class OuvinteBotaoEncerrarReabrir implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isCoordenador()) {
                mostrarErro("Apenas coordenadores podem alterar o estado do edital.");
                return;
            }

            Coordenador coordenador = (Coordenador) sessao.getUsuarioLogado();
            
            if (edital.isAberto()) {
                // Ação de Encerrar
                int confirmacao = JOptionPane.showConfirmDialog(TelaDetalharEdital.this,
                        "Tem certeza que deseja encerrar as inscrições para este edital?",
                        "Confirmar Encerramento", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (confirmacao == JOptionPane.YES_OPTION) {
                    try {
                        edital.fecharEdital(coordenador);
                        getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
                        mostrarSucesso("Edital encerrado com sucesso!");
                        atualizarBotaoEncerramento();
                    } catch (PermissaoNegadaException | EditalFechadoException ex) {
                        mostrarErro(ex.getMessage());
                    }
                }
            } else {
                // Ação de Reabrir
                int confirmacao = JOptionPane.showConfirmDialog(TelaDetalharEdital.this,
                        "Deseja reabrir as inscrições para este edital?",
                        "Confirmar Reabertura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (confirmacao == JOptionPane.YES_OPTION) {
                    try {
                        edital.reabrirEdital(coordenador);
                        getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
                        mostrarSucesso("Edital reaberto para inscrições!");
                        atualizarBotaoEncerramento();
                    } catch (PermissaoNegadaException | EditalAbertoException | PrazoVencidoException ex) {
                        mostrarErro(ex.getMessage());
                    }
                }
            }
        }
    }

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
                        public void windowOpened(WindowEvent e) {}
                        public void windowClosing(WindowEvent e) {}
                        public void windowClosed(WindowEvent e) {
                            preencherCamposComDados(edital);
                        }
                        public void windowIconified(WindowEvent e) {}
                        public void windowDeiconified(WindowEvent e) {}
                        public void windowActivated(WindowEvent e) {}
                        public void windowDeactivated(WindowEvent e) {}
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

            edital.setDataInicio(dataInicioFormatada);
            edital.setDataLimite(dataFinalFormatada);
            edital.setPesoCre((Double) pesoCRE.getValue());
            edital.setPesoNota((Double) pesoNota.getValue());

            getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
            mostrarSucesso("Edital salvo com sucesso!");

            tornarCamposEditaveis(false);
            mudarVisibilidadeBotoes(false);
        }
    }
}
