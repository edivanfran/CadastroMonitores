import java.util.ArrayList;

/**
 * Descreve o objeto cujas funções são armazenar as alterações feitas na base de informações para posteriormente tê-las serializadas em um arquivo XML por meio de um objeto {@link Persistencia}, assim como receber as informações que forem desserializadas por este para que sejam utilizadas pelo programa.
 * <p>Cada central só pode ter um coordenador — além disso, as centrais armazenam um {@code ArrayList} contendo as informações de todos os alunos cadastrados na central, e outro contendo as informações de todos os editais cadastrados na central.</p>
 * @see Coordenador
 * @see Aluno
 * @see EditalDeMonitoria
 */
public class CentralDeInformacoes {
    private Coordenador coordenador;
    private ArrayList<Aluno> todosOsAlunos = new ArrayList<>();
    private ArrayList<EditalDeMonitoria> todosOsEditais = new ArrayList<>();

    public Coordenador getCoordenador() {
        return this.coordenador;
    }
    public ArrayList<Aluno> getTodosOsAlunos(){
        return this.todosOsAlunos;
    }
    public void setTodosOsAlunos(ArrayList<Aluno> listaAtualizada) {
        this.todosOsAlunos = listaAtualizada;
    }
    public ArrayList<EditalDeMonitoria> getTodosOsEditais() {
        return this.todosOsEditais;
    }
    public void setTodosOsEditais(ArrayList<EditalDeMonitoria> todosOsEditais) {
        this.todosOsEditais = todosOsEditais;
    }

    /**
     * Constrói um {@link Coordenador} a partir dos parâmetros fornecidos e o atribui à central.
     * @param email O endereço de e-mail do Coordenador
     * @param senha A senha do Coordenador
     * @param nome O nome do Coordenador
     */
    public void cadastrarCoordenador(String email, String senha, String nome) {
        this.coordenador = new Coordenador(email, senha, nome);
    }
    //TODO| seria mais interessante que o método só recebesse já o objeto, ex.: `cadastrarCoordenador(Coordenador c)`

    /**
     * Verifica se já foi atribuído algum {@link Coordenador} à central.
     */
    public boolean temCoordenador() {
        return coordenador != null;
    }

    /**
     * Recebe um {@link Aluno}, verifica se não há duplicatas do mesmo, e então adiciona-o à central.
     * @return {@code true} se a adição foi bem sucedida, {@code false} se não
     */
    public boolean adicionarAluno(Aluno aluno) {
        for (Aluno algum : todosOsAlunos) {
            if (algum.getMatricula().equals(aluno.getMatricula())) {
                return false;
            }
        }
        todosOsAlunos.add(aluno);
        return true; //TODO| pode ser interessante criar uma exceção para caso o aluno já exista (duplicata)
    }

    /**
     * Tenta recuperar um {@link Aluno} salvo na central.
     * @param matricula A matrícula do aluno que se deseja obter
     * @return O Aluno, se bem sucedido, ou {@code null} se não
     */
    public Aluno recuperarAluno(String matricula) {
        for (Aluno algum : todosOsAlunos) {
            if (algum.getMatricula().equals(matricula)) {
                return algum;
            }
        }
        return null; //TODO| criar exceção para caso não consiga recuperar o aluno, ao invés de apenas usar `null`
    }

    /**
     * Recupera e imprime a lista de editais.
     * <p>Se a lista estiver vazia, imprime "Lista vazia"</p>
     * @see EditalDeMonitoria
     */
    public void mostrarIdEditais() {
        if (todosOsEditais.isEmpty()) {
            System.out.println("Lista vazia");
            return;
        }
        for (EditalDeMonitoria edital : todosOsEditais) {
            System.out.printf("Disciplina: %s Id: %s \n", edital.getDisciplinas(), edital.getId());
        }
    }

    /**
     * Tenta recuperar um edital específico.
     * @param id ID do edital
     * @return O edital, ou {@code null} caso ele não seja encontrado
     * @see EditalDeMonitoria
     */
    public EditalDeMonitoria recuperarEdital(long id) {
        for (EditalDeMonitoria edital : todosOsEditais) {
            if (edital.getId() == id) {
                return edital;
            }
        }
        return null;
    } //TODO| pode ser interessante criar uma exceção aqui também

    public boolean adicionarEdital(EditalDeMonitoria edital) { //TODO| recomendado fundir método com `cadastrarEdital()` (pode ser necessário refatorar algumas coisas)
        for (EditalDeMonitoria e : todosOsEditais) {
            if (e.getId() == edital.getId()) {
                return false;
            }
        }
        todosOsEditais.add(edital);
        return true;
    }

    /**
     * Cadastra um novo edital no sistema.
     * Apenas coordenadores podem executar esta operação.
     * @param coordenador O coordenador que está executando a operação
     * @param edital O edital a ser cadastrado
     * @return {@code true} se o edital foi cadastrado com sucesso, {@code false} caso contrário
     */
    public boolean cadastrarEdital(Coordenador coordenador, EditalDeMonitoria edital) {
        if (coordenador == null) {
            System.out.println("[Erro] Apenas coordenadores podem cadastrar editais.");
            return false; //TODO| melhorar lógica — ao invés de fazer esse método chamar `adicionarEdital()`, pode ser que seja mais interessante fundir os dois
        } //TODO| esse método tá duplicado, `Coordenador.cadastrarEdital()` chama esse lá a toa (o de lá passa os seguintes parâmetros: `central` e `edital`; esse daqui, por sua vez, passa `coordenador`)
        return adicionarEdital(edital);
    }

    /**
     * Lista todos os alunos cadastrados no sistema.
     * Apenas coordenadores podem executar esta operação.
     * @param coordenador O coordenador que está executando a operação
     * @return Lista de alunos, ou {@code null} se o usuário não for coordenador
     */
    public ArrayList<Aluno> listarAlunos(Coordenador coordenador) {
        if (coordenador == null) {
            System.out.println("[Erro] Apenas coordenadores podem listar alunos.");
            return null;
        }
        return new ArrayList<>(todosOsAlunos);
    }

    /**
     * A partir do endereço de e-mail fornecido, busca na central o aluno portador de tal endereço de e‑mail.
     * @param email O endereço de e-mail
     * @return O portador do e-mail, ou {@code null} caso o portador não tenha sido encontrado
     */
    public Aluno retornarAlunoPeloEmail(String email) {
        for (Aluno algum: todosOsAlunos) {
            if (algum.getEmail().equals(email)) {
                return algum;
            }
        }
        return null;
    }

    /**
     * Verifica se as credenciais informadas pelo usuário correspondem a um login válido.
     * @param email O endereço de e-mail do usuário supostamente cadastrado
     * @param senha A senha do usuário
     * @return {@code true} se autorizado, {@code false} caso contrário
     */
    public boolean isLoginPermitido(String email, String senha) {
        for (Aluno algum: todosOsAlunos) {
            if (algum.getEmail().equals(email) && algum.getSenha().equals(senha)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tenta obter o gênero e o primeiro nome do usuário, e então compõe uma mensagem de boas‑vindas:
     * <ul>
     *     Bem-vind_<sup>[1]</sup>, _____<sup>[2]</sup>
     * </ul>
     * onde
     * <ol>
     *     1. Corresponde ao gênero do usuário<br>
     *     2. É o primeiro nome do usuário
     * </ol>
     * Caso não consiga obter, resulta em:
     * <ul>
     *     Olá, usuário!
     * </ul>
     * @param email O endereço de e-mail do usuário
     * @param senha A senha do usuário
     */
    public void darBoasVindasUsuario(String email, String senha) {
        for (Aluno algum: todosOsAlunos) {
            if (algum.getEmail().equals(email) && algum.getSenha().equals(senha)) {
                String mensagem;
                if (algum.getGenero() == Sexo.MASCULINO) {
                    mensagem = "Bem-vindo, ";
                } else if (algum.getGenero() == Sexo.FEMININO) {
                    mensagem = "Bem-vinda, ";
                } else if (algum.getGenero() == Sexo.NAO_BINARIO) {
                    mensagem = "Bem-vinde, ";
                } else {
                    mensagem = "Bem-vindo, ";
                }
                String[] nome = algum.getNome().split(" ");
                String primeiro_nome = nome[0];
                mensagem += primeiro_nome;
                mensagem += "!";
                System.out.println(mensagem);
                return;
            }
        }
        System.out.println("Olá usuário!");
    }
}
