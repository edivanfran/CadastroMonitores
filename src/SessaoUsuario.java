
/**
 * Classe singleton para gerenciar a sessão do usuário logado.
 * Permite verificar se o usuário é coordenador ou aluno em qualquer parte da aplicação.
 */
public class SessaoUsuario {
    private static SessaoUsuario instancia;
    private Usuario usuarioLogado;
    
    private SessaoUsuario() {
        // Construtor privado para singleton
    }
    
    /**
     * Retorna a instância única da sessão.
     * @return A instância de SessaoUsuario
     */
    public static SessaoUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SessaoUsuario();
        }
        return instancia;
    }
    
    /**
     * Define o usuário logado na sessão.
     * @param usuario O usuário que fez login
     */
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }
    
    /**
     * Retorna o usuário atualmente logado.
     * @return O usuário logado, ou null se não houver ninguém logado
     */
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
    
    /**
     * Verifica se o usuário logado é um coordenador.
     * @return true se for coordenador, false caso contrário
     */
    public boolean isCoordenador() {
        return usuarioLogado instanceof Coordenador;
    }
    
    /**
     * Verifica se o usuário logado é um aluno.
     * @return true se for aluno, false caso contrário
     */
    public boolean isAluno() {
        return usuarioLogado instanceof Aluno;
    }
    
    /**
     * Retorna o nome do usuário logado.
     * @return O nome do usuário, ou "Usuário" se não houver ninguém logado
     */
    public String getNomeUsuario() {
        if (usuarioLogado != null) {
            return usuarioLogado.getNome();
        }
        return "Usuário";
    }
    
    /**
     * Limpa a sessão (faz logout).
     */
    public void limparSessao() {
        this.usuarioLogado = null;
    }
    
    /**
     * Verifica se há um usuário logado.
     * @return true se houver usuário logado, false caso contrário
     */
    public boolean estaLogado() {
        return usuarioLogado != null;
    }
}

