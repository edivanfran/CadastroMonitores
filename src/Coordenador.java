import java.util.ArrayList;
import excecoes.*;

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
     * @throws PermissaoNegadaException Se o usuário não for coordenador
     */
    public void cadastrarEdital(CentralDeInformacoes central, EditalDeMonitoria edital) throws PermissaoNegadaException {
        central.cadastrarEdital(this, edital);
    }

    /**
     * Calcula o resultado (ranqueamento) de um edital.
     * @param edital O edital cujo resultado será calculado
     * @throws PermissaoNegadaException Se o usuário não for coordenador
     * @throws EditalAbertoException Se o edital ainda estiver aberto
     * @throws SemInscricoesException Se não houver inscrições no edital
     */
    public void calcularResultadoEdital(EditalDeMonitoria edital)
            throws PermissaoNegadaException, EditalAbertoException, SemInscricoesException {
        edital.calcularResultado();
    }

    /**
     * Fecha um edital, impedindo novas inscrições.
     * @param edital O edital a ser fechado
     * @throws PermissaoNegadaException Se o usuário não for coordenador
     * @throws EditalFechadoException Se o edital já estiver fechado
     */
    public void fecharEdital(EditalDeMonitoria edital) throws PermissaoNegadaException, EditalFechadoException {
        edital.fecharEdital(this);
    }

    /**
     * Lista todos os alunos cadastrados em uma dada central de informações.
     * @param central A central de informações
     * @return Lista de alunos cadastrados
     * @throws PermissaoNegadaException Se o usuário não for coordenador
     */
    public ArrayList<Aluno> listarAlunos(CentralDeInformacoes central) throws PermissaoNegadaException {
        return central.listarAlunos(this);
    }
}
