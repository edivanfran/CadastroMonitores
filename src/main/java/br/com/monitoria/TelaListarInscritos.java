package br.com.monitoria;

import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.swing.ListSelectionModel;

public class TelaListarInscritos extends TelaBase {

    private EditalDeMonitoria edital;
    private Disciplina disciplina;
    private JTable tabelaInscritos;
    private JButton botaoEnviarEmail;
    private JButton botaoFechar;

    public TelaListarInscritos(EditalDeMonitoria edital, Disciplina disciplina, CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Inscritos em " + disciplina.getNomeDisciplina(), central, persistencia, nomeArquivo);
        this.edital = edital;
        this.disciplina = disciplina;
        setSize(600, 400);
    }

    protected void criarComponentes() {
        criarTabela();
        criarBotoes();
    }

    private void criarTabela() {
        DefaultTableModel model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Nome do Aluno");
        model.addColumn("E-mail");
        model.addColumn("Tipo de Vaga");

        ArrayList<Inscricao> inscricoesDaDisciplina = edital.getInscricoes().stream()
                .filter(insc -> insc.getDisciplina().equals(disciplina))
                .collect(Collectors.toCollection(ArrayList::new));

        for (Inscricao insc : inscricoesDaDisciplina) {
            model.addRow(new Object[]{
                    insc.getAluno().getNome(),
                    insc.getAluno().getEmail(),
                    insc.getTipoVaga().toString()
            });
        }

        tabelaInscritos = new JTable(model);
        tabelaInscritos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaInscritos.setFont(Estilos.FONTE_NORMAL);
        tabelaInscritos.getTableHeader().setFont(Estilos.FONTE_BOTAO);

        JScrollPane scrollPane = new JScrollPane(tabelaInscritos);
        scrollPane.setBounds(20, 20, 540, 250);
        painelPrincipal.add(scrollPane);
    }

    private void criarBotoes() {
        botaoEnviarEmail = criarBotao("Enviar E-mail ao Selecionado", new OuvinteEnviarEmail());
        botaoEnviarEmail.setBounds(50, 300, 250, 40);
        painelPrincipal.add(botaoEnviarEmail);

        botaoFechar = criarBotaoSecundario("Fechar", e -> dispose());
        botaoFechar.setBounds(350, 300, 150, 40);
        painelPrincipal.add(botaoFechar);
    }

    private class OuvinteEnviarEmail implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int linhaSelecionada = tabelaInscritos.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarErro("Por favor, selecione um aluno na tabela para enviar um e-mail.");
                return;
            }
            if (!isCoordenador()) {
                mostrarErro("Apenas coordenadores podem enviar e-mails.");
                return;
            }

            String emailDestinatario = (String) tabelaInscritos.getValueAt(linhaSelecionada, 1);
            String nomeAluno = (String) tabelaInscritos.getValueAt(linhaSelecionada, 0);

            String assunto = JOptionPane.showInputDialog(
                    TelaListarInscritos.this,
                    "Digite o assunto do e-mail para " + nomeAluno + ":",
                    "Assunto do E-mail",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (assunto == null || assunto.trim().isEmpty()) {
                mostrarAviso("Envio de e-mail cancelado.");
                return;
            }

            JTextArea areaTextoMensagem = new JTextArea(10, 30);
            areaTextoMensagem.setLineWrap(true);
            areaTextoMensagem.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(areaTextoMensagem);
            
            int resultado = JOptionPane.showConfirmDialog(
                    TelaListarInscritos.this,
                    scrollPane,
                    "Digite a mensagem",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (resultado == JOptionPane.OK_OPTION) {
                String mensagem = areaTextoMensagem.getText();
                if (mensagem.trim().isEmpty()) {
                    mostrarAviso("A mensagem não pode estar vazia. Envio cancelado.");
                    return;
                }
                
                try {
                    Mensageiro.enviarEmail(emailDestinatario, assunto, mensagem);
                    mostrarSucesso("E-mail enviado com sucesso para " + nomeAluno + ".");
                } catch (Exception ex) {
                    mostrarErro("Falha ao enviar o e-mail: " + ex.getMessage());
                }
            } else {
                mostrarAviso("Envio de e-mail cancelado.");
            }
        }
    }
}
