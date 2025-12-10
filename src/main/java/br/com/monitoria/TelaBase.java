package br.com.monitoria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Classe base para todas as telas da aplicação.
 * Fornece funcionalidades comuns e garante consistência visual.
 */
public abstract class TelaBase extends JFrame {
    
    protected JPanel painelPrincipal;
    protected SessaoUsuario sessao;
    private CentralDeInformacoes central;
    private Persistencia persistencia;
    private String nomeArquivo;

    public TelaBase(String titulo, CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        super(titulo);
        this.central = central;
        this.persistencia = persistencia;
        this.nomeArquivo = nomeArquivo;
        this.sessao = SessaoUsuario.getInstancia();
        configurarJanela();
        criarPainelPrincipal();
    }

    /**
     * Configura as propriedades básicas da janela.
     */
    private void configurarJanela() {
        setSize(Estilos.LARGURA_TELA, Estilos.ALTURA_TELA);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        setLayout(null);
        setResizable(false); // Tirar o resizable para manter a tela menos proprícia a erros.
        getContentPane().setBackground(Estilos.COR_FUNDO);
    }

    /**
     * Cria o painel principal com layout e estilo padrão.
     */
    private void criarPainelPrincipal() {
        painelPrincipal = new JPanel();
        painelPrincipal.setLayout(null);
        painelPrincipal.setBackground(Estilos.COR_FUNDO);
        painelPrincipal.setBounds(0, 0, Estilos.LARGURA_TELA, Estilos.ALTURA_TELA);
        add(painelPrincipal);
    }

    /**
     * Cria um botão padronizado.
     * @param texto O texto do botão
     * @param listener O ActionListener do botão
     * @return O botão criado
     */
    protected JButton criarBotao(String texto, ActionListener listener) {
        JButton botao = new JButton(texto);
        botao.setFont(Estilos.FONTE_BOTAO);
        botao.setPreferredSize(new Dimension(Estilos.LARGURA_BOTAO, Estilos.ALTURA_BOTAO));
        botao.setBackground(Estilos.COR_PRIMARIA);
        botao.setForeground(Estilos.COR_BRANCO);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efeito hover (Quando o mouse passa por cima do botão)
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            // Quando mudar a cor do botao.setBackground ele não voltar para a cor primaria
            private Color corOriginal;

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                corOriginal = botao.getBackground();
                botao.setBackground(corOriginal.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(corOriginal);
            }
        });

        if (listener != null) {
            botao.addActionListener(listener);
        }

        return botao;
    }

    /**
     * Cria um botão secundário (estilo diferente).
     * @param texto O texto do botão
     * @param listener O ActionListener do botão
     * @return O botão criado
     */
    protected JButton criarBotaoSecundario(String texto, ActionListener listener) {
        JButton botao = criarBotao(texto, listener);
        botao.setBackground(Estilos.COR_SECUNDARIA);
        return botao;
    }

    /**
     * Cria um label padronizado.
     * @param texto O texto do label
     * @param fonte A fonte a ser usada
     * @return O label criado
     */
    protected JLabel criarLabel(String texto, Font fonte) {
        JLabel label = new JLabel(texto);
        label.setFont(fonte);
        label.setForeground(Estilos.COR_TEXTO);
        return label;
    }

    /**
     * Cria um campo de texto padronizado.
     * @param colunas Número de colunas do campo
     * @return O campo de texto criado
     */
    protected JTextField criarCampoTexto(int colunas) {
        JTextField campo = new JTextField(colunas);
        campo.setFont(Estilos.FONTE_NORMAL);
        // Criar a borda do Campo
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilos.COR_SECUNDARIA, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return campo;
    }

    /**
     * Mostra uma mensagem de sucesso.
     * @param mensagem A mensagem a ser exibida
     */
    protected void mostrarSucesso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostra uma mensagem de erro.
     * @param mensagem A mensagem a ser exibida
     */
    protected void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Mostra uma mensagem de aviso.
     * @param mensagem A mensagem a ser exibida
     */
    // Vai ser muito útil
    protected void mostrarAviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Aviso",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Verifica se o usuário logado é coordenador.
     * @return true se for coordenador, false caso contrário
     */
    protected boolean isCoordenador() {
        return sessao.isCoordenador();
    }

    /**
     * Verifica se o usuário logado é aluno.
     * @return true se for aluno, false caso contrário
     */
    protected boolean isAluno() {
        return sessao.isAluno();
    }

    /**
     * Habilita ou desabilita um componente baseado na permissão.
     * @param componente O componente a ser habilitado/desabilitado
     * @param habilitar true para habilitar, false para desabilitar
     */
    protected void definirPermissao(JComponent componente, boolean habilitar) {
        componente.setEnabled(habilitar);
        if (!habilitar) {
            componente.setToolTipText("Apenas coordenadores podem acessar esta funcionalidade");
        }
    }

    /**
     * Método abstrato que deve ser implementado por cada tela específica.
     * Define os componentes e layout da tela.
     */
    protected abstract void criarComponentes();

    /**
     * Inicializa a tela criando os componentes.
     */
    public void inicializar() {
        criarComponentes();
        setVisible(true);
    }

    public CentralDeInformacoes getCentral() {
        return central;
    }

    public Persistencia getPersistencia() {
        return persistencia;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }
}
