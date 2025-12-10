package br.com.monitoria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaPerfilAluno extends TelaBase {

    private Aluno aluno;
    private JTextField campoNome;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JTextField campoMatricula;
    private JComboBox<Sexo> campoGenero;

    private JButton botaoEditar;
    private JButton botaoVoltar;
    private JButton botaoSalvar;
    private JButton botaoCancelar;

    public TelaPerfilAluno(Aluno aluno, CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super("Perfil do Aluno", central, persistencia, nomeArquivo);
        this.aluno = aluno;

        // Sobrescreve o tamanho padrão da TelaBase
        setSize(700, 500);
        // Fecha apenas esta janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza novamente após mudar o tamanho

        inicializar();
    }

    @Override
    protected void criarComponentes() {
        criarLabels();
        criarCampos();
        criarBotoes();
        preencherCampos();
        alternarModoEdicao(false);
    }

    private void criarLabels() {
        JLabel nome = new JLabel("Nome:");
        nome.setFont(Estilos.FONTE_NORMAL);
        nome.setBounds(40, 40, 100, 40);
        painelPrincipal.add(nome);

        JLabel email = new JLabel("E-mail:");
        email.setFont(Estilos.FONTE_NORMAL);
        email.setBounds(40, 100, 100, 40);
        painelPrincipal.add(email);

        JLabel senha = new JLabel("Senha:");
        senha.setFont(Estilos.FONTE_NORMAL);
        senha.setBounds(40, 160, 100, 40);
        painelPrincipal.add(senha);

        JLabel matricula = new JLabel("Matrícula:");
        matricula.setFont(Estilos.FONTE_NORMAL);
        matricula.setBounds(40, 220, 100, 40);
        painelPrincipal.add(matricula);

        JLabel genero = new JLabel("Gênero:");
        genero.setFont(Estilos.FONTE_NORMAL);
        genero.setBounds(40, 280, 100, 40);
        painelPrincipal.add(genero);
    }

    private void criarCampos() {
        campoNome = new JTextField();
        campoNome.setBounds(140, 40, 350, 40);
        painelPrincipal.add(campoNome);

        campoEmail = new JTextField();
        campoEmail.setBounds(140, 100, 350, 40);
        painelPrincipal.add(campoEmail);

        campoSenha = new JPasswordField();
        campoSenha.setBounds(140, 160, 350, 40);
        painelPrincipal.add(campoSenha);

        campoMatricula = new JTextField();
        campoMatricula.setBounds(140, 220, 350, 40);
        painelPrincipal.add(campoMatricula);

        campoGenero = new JComboBox<>(Sexo.values());
        campoGenero.setBounds(140, 280, 180, 40);
        painelPrincipal.add(campoGenero);
    }

    private void preencherCampos() {
        campoNome.setText(aluno.getNome());
        campoEmail.setText(aluno.getEmail());
        campoSenha.setText(aluno.getSenha());
        campoMatricula.setText(aluno.getMatricula());
        campoGenero.setSelectedItem(aluno.getGenero());
    }

    private void criarBotoes() {
        botaoEditar = new JButton("Editar");
        botaoEditar.setBounds(80, 350, 120, 40);
        botaoEditar.addActionListener(e -> alternarModoEdicao(true));
        botaoEditar.setBackground(Estilos.COR_SUCESSO);
        painelPrincipal.add(botaoEditar);

        botaoVoltar = new JButton("Voltar");
        botaoVoltar.setBounds(350, 350, 120, 40);
        botaoVoltar.addActionListener(e -> dispose());
        botaoVoltar.setBackground(Estilos.COR_CINZA);
        painelPrincipal.add(botaoVoltar);

        botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBounds(100, 350, 120, 40);
        botaoSalvar.setBackground(Estilos.COR_SUCESSO);
        botaoSalvar.addActionListener(new OuvinteBotaoSalvar());
        painelPrincipal.add(botaoSalvar);

        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(400, 350, 120, 40);
        botaoCancelar.setBackground(Estilos.COR_PERIGO);
        botaoCancelar.addActionListener(e -> {
            preencherCampos(); // Restaura os dados originais
            alternarModoEdicao(false);
        });
        painelPrincipal.add(botaoCancelar);
    }

    private void alternarModoEdicao(boolean editando) {
        // Campos
        campoNome.setEditable(editando);
        campoSenha.setEditable(editando);
        campoGenero.setEnabled(editando);
        // Email e Matrícula nunca são editáveis
        if (sessao.getUsuarioLogado() instanceof Coordenador) {
            campoEmail.setEditable(editando);
            campoMatricula.setEditable(editando);
        } else {
            campoEmail.setEditable(false);
            campoMatricula.setEditable(false);
        }

        // Botões
        botaoEditar.setVisible(!editando);
        botaoVoltar.setVisible(!editando);
        botaoSalvar.setVisible(editando);
        botaoCancelar.setVisible(editando);
    }

    private class OuvinteBotaoSalvar implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Validar nome
            String nome = campoNome.getText().strip();
            if (nome.isEmpty() || !nome.matches("^[A-ZÀ-Ÿ][a-zà-ÿ]+(?: (?:[dD]e|[dD]a|[dD]os|[dD]as|[eE])? ?[A-ZÀ-Ÿ]?[a-zà-ÿ]+)+$")) {
                mostrarErro("Nome inválido. Por favor, insira um nome completo válido.");
                return;
            }

            // Validar senha
            String senha = new String(campoSenha.getPassword());
            if (senha.isEmpty()) {
                mostrarErro("A senha não pode estar em branco.");
                return;
            }

            // Atualizar dados do aluno
            aluno.setNome(nome);
            aluno.setSenha(senha);
            aluno.setGenero((Sexo) campoGenero.getSelectedItem());

            // Persistir as alterações
            getPersistencia().salvarCentral(getCentral(), getNomeArquivo());
            mostrarSucesso("Dados do aluno atualizados com sucesso!");
            
            alternarModoEdicao(false);
        }
    }
}
