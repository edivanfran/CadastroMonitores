package br.com.monitoria;

import br.com.monitoria.model.Aluno;
import br.com.monitoria.model.EditalDeMonitoria;
import br.com.monitoria.model.Inscricao;
import br.com.monitoria.servico.AlunoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaPerfilAluno extends TelaBase {

    private Aluno aluno;
    private AlunoService alunoService;

    private JTextField campoNome;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JTextField campoMatricula;
    private JComboBox<Sexo> campoGenero;

    private JButton botaoEditar;
    private JButton botaoVoltar;
    private JButton botaoSalvar;
    private JButton botaoCancelar;
    private JTable tabelaHistorico;
    private DefaultTableModel modeloTabelaHistorico;

    public TelaPerfilAluno(Aluno aluno) {
        super("Perfil do Aluno");
        this.aluno = aluno;
        this.alunoService = new AlunoService();

        setSize(700, 650);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializar();
    }

    @Override
    protected void criarComponentes() {
        criarLabels();
        criarCampos();
        criarTabelaHistorico();
        criarBotoes();
        preencherCampos();
        carregarHistorico();
        alternarModoEdicao(false);
    }

    private void criarLabels() {
        JLabel nome = new JLabel("Nome:");
        nome.setFont(Estilos.FONTE_NORMAL);
        nome.setBounds(40, 40, 100, 40);
        painelPrincipal.add(nome);

        JLabel email = new JLabel("E-mail:");
        email.setFont(Estilos.FONTE_NORMAL);
        email.setBounds(40, 80, 100, 30);
        painelPrincipal.add(email);

        JLabel senha = new JLabel("Senha:");
        senha.setFont(Estilos.FONTE_NORMAL);
        senha.setBounds(40, 130, 100, 30);
        painelPrincipal.add(senha);

        JLabel matricula = new JLabel("Matrícula:");
        matricula.setFont(Estilos.FONTE_NORMAL);
        matricula.setBounds(40, 180, 100, 30);
        painelPrincipal.add(matricula);

        JLabel genero = new JLabel("Gênero:");
        genero.setFont(Estilos.FONTE_NORMAL);
        genero.setBounds(40, 230, 100, 30);
        painelPrincipal.add(genero);

        JLabel historico = new JLabel("Histórico de Monitorias:");
        historico.setFont(new Font("Calibri", Font.BOLD, 20));
        historico.setBounds(40, 290, 300, 30);
        painelPrincipal.add(historico);
    }

    private void criarCampos() {
        campoNome = new JTextField();
        campoNome.setBounds(140, 30, 350, 30);
        painelPrincipal.add(campoNome);

        campoEmail = new JTextField();
        campoEmail.setBounds(140, 80, 350, 30);
        painelPrincipal.add(campoEmail);

        campoSenha = new JPasswordField();
        campoSenha.setBounds(140, 130, 350, 30);
        painelPrincipal.add(campoSenha);

        campoMatricula = new JTextField();
        campoMatricula.setBounds(140, 180, 350, 30);
        painelPrincipal.add(campoMatricula);

        campoGenero = new JComboBox<>(Sexo.values());
        campoGenero.setBounds(140, 230, 180, 30);
        painelPrincipal.add(campoGenero);
    }

    private void criarTabelaHistorico() {
        modeloTabelaHistorico = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        String[] colunas = {"Edital", "Disciplina", "Período", "Vaga"};
        for (String coluna : colunas) {
            modeloTabelaHistorico.addColumn(coluna);
        }

        tabelaHistorico = new JTable(modeloTabelaHistorico);
        tabelaHistorico.setRowHeight(25);
        tabelaHistorico.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane painelTabelaHistorico = new JScrollPane(tabelaHistorico);
        painelTabelaHistorico.setBounds(40, 330, 600, 180);
        painelPrincipal.add(painelTabelaHistorico);
    }

    private void preencherCampos() {
        campoNome.setText(aluno.getNome());
        campoEmail.setText(aluno.getEmail());
        campoSenha.setText(aluno.getSenha());
        campoMatricula.setText(aluno.getMatricula());
        campoGenero.setSelectedItem(aluno.getGenero());
    }

    private void carregarHistorico() {
        modeloTabelaHistorico.setRowCount(0);
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        List<Inscricao> inscricoes = alunoService.getHistoricoInscricoes(this.aluno);

        for (Inscricao inscricao : inscricoes) {
            EditalDeMonitoria edital = inscricao.getEdital();
            String periodo = edital.getDataInicio().format(formatador) + " - " + 
                             edital.getDataLimite().format(formatador);
            
            modeloTabelaHistorico.addRow(new Object[]{
                edital.getNumero(),
                inscricao.getDisciplina().getNomeDisciplina(),
                periodo,
                inscricao.getTipoVaga()
            });
        }
    }

    private void criarBotoes() {
        botaoEditar = new JButton("Editar");
        botaoEditar.setBounds(140, 530, 100, 40);
        botaoEditar.addActionListener(e -> alternarModoEdicao(true));
        painelPrincipal.add(botaoEditar);

        botaoVoltar = new JButton("Voltar");
        botaoVoltar.setBounds(260, 530, 100, 40);
        botaoVoltar.addActionListener(e -> dispose());
        painelPrincipal.add(botaoVoltar);

        botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBounds(140, 530, 100, 40);
        botaoSalvar.setBackground(Estilos.COR_SUCESSO);
        botaoSalvar.addActionListener(new OuvinteBotaoSalvar());
        painelPrincipal.add(botaoSalvar);

        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(260, 530, 100, 40);
        botaoCancelar.setBackground(Estilos.COR_PERIGO);
        botaoCancelar.addActionListener(new OuvinteBotaoCancelar());
        painelPrincipal.add(botaoCancelar);
    }

    private void alternarModoEdicao(boolean editando) {
        // Campos
        campoNome.setEditable(editando);
        campoSenha.setEditable(editando);
        campoGenero.setEnabled(editando);
        // Email e Matrícula nunca são editáveis para aluno
        campoEmail.setEditable(false);
        campoMatricula.setEditable(false);

        // Botões
        botaoEditar.setVisible(!editando);
        botaoVoltar.setVisible(!editando);
        botaoSalvar.setVisible(editando);
        botaoCancelar.setVisible(editando);
    }

    private class OuvinteBotaoCancelar implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Coloca os dados originais
            preencherCampos();
            alternarModoEdicao(false);
        }
    }

    private class OuvinteBotaoSalvar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nome = campoNome.getText().trim();
            String senha = new String(campoSenha.getPassword());
            Sexo genero = (Sexo) campoGenero.getSelectedItem();

            try {
                alunoService.atualizarPerfil(aluno, nome, senha, genero);
                mostrarSucesso("Dados do aluno atualizados com sucesso!");
                alternarModoEdicao(false);
            } catch (Exception ex) {
                mostrarErro(ex.getMessage());
            }
        }
    }
}
