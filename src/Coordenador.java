import java.util.ArrayList;

public class Coordenador extends Usuario {

    public Coordenador(String email, String senha) {
        super(email, senha);
    }

    public boolean autenticar(String email, String senha) {
        return this.email.equals(email) && this.senha.equals(senha);
    }

    /**
     * Cadastra um novo edital de monitoria no sistema.
     * central A central de informações onde o edital será cadastrado
     * @param edital O edital a ser cadastrado
     * @return true se o edital foi cadastrado com sucesso, false caso contrário
     */
    public boolean cadastrarEdital(CentralDeInformacoes central, EditalDeMonitoria edital) {
        return central.cadastrarEdital(this, edital);
    }

    /**
     * Calcula o resultado (ranqueamento) de um edital.
     * @param edital O edital cujo resultado será calculado
     * @return true se o cálculo foi realizado com sucesso, false caso contrário
     */
    public boolean calcularResultadoEdital(EditalDeMonitoria edital) {
        return edital.calcularResultado(this);
    }

    /**
     * Fecha um edital, impedindo novas inscrições.
     * @param edital O edital a ser fechado
     * @return true se o edital foi fechado com sucesso, false caso contrário
     */
    public boolean fecharEdital(EditalDeMonitoria edital) {
        return edital.fecharEdital(this);
    }

    /**
     * Lista todos os alunos cadastrados no sistema.
     * @param central A central de informações
     * @return Lista de alunos cadastrados, ou null em caso de erro
     */
    public ArrayList<Aluno> listarAlunos(CentralDeInformacoes central) {
        return central.listarAlunos(this);
    }
    public boolean fazerLogin(String email, String senha) {
        return super.fazerLogin(email, senha);
    }

    public void editarPerfil(String novoEmail, String novaSenha) {
        super.editarPerfil(novoEmail, novaSenha);
    }
}
