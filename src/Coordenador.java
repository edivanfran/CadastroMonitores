import java.util.ArrayList;

/**
 * <p>Representa o usuário atribuído na central de informações como o Coordenador do Curso.
 * Possui e-mail, senha e nome, mas não precisa de matrícula.</p>
 * <p>Só pode haver um Coordenador em um mesmo objeto {@link CentralDeInformacoes}.</p>
 */
public class Coordenador extends Usuario {

    public Coordenador(String email, String senha, String nome) {
        super(email, senha, nome);
    }

    /**
     * Cadastra um novo edital de monitoria no sistema.
     * @param central A central de informações no qual o edital será cadastrado
     * @param edital O edital a ser cadastrado
     * @return {@code true} se o edital foi cadastrado com sucesso, {@code false} caso contrário
     */
    public boolean cadastrarEdital(CentralDeInformacoes central, EditalDeMonitoria edital) {
        return central.cadastrarEdital(this, edital); //TODO| duplicado e não utilizado (mas não recomendo remover logo de cara sem antes analisar qual dos dois manter); vide nota em `CentralDeInformacoes.cadastrarEdital()`
    }

    /**
     * Calcula o resultado (ranqueamento) de um edital.
     * @param edital O edital cujo resultado será calculado
     * @return {@code true} se o cálculo foi realizado com sucesso, {@code false} caso contrário
     */
    public boolean calcularResultadoEdital(EditalDeMonitoria edital) {
        return edital.calcularResultado(this);
    } //TODO| duplicado e não utilizado

    /**
     * Fecha um edital, impedindo novas inscrições.
     * @param edital O edital a ser fechado
     * @return {@code true} se o edital foi fechado com sucesso, {@code false} caso contrário
     */
    public boolean fecharEdital(EditalDeMonitoria edital) {
        return edital.fecharEdital(this);
    } //TODO| duplicado e não utilizado

    /**
     * Lista todos os alunos cadastrados em uma dada central de informações.
     * @param central A central de informações
     * @return Lista de alunos cadastrados, ou {@code null} em caso de erro
     */
    public ArrayList<Aluno> listarAlunos(CentralDeInformacoes central) {
        return central.listarAlunos(this);
    } //TODO| duplicado e não utilizado
}
