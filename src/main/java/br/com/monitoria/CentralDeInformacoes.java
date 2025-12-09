package br.com.monitoria;

import br.com.monitoria.interfaces.Observador;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import br.com.monitoria.excecoes.*;

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
    private transient Map<String, String> codigosRecuperacao = new HashMap<>();
    private transient List<Observador> observadores = new ArrayList<>();

    /**
     * Adiciona um observador à lista.
     * Observadores são notificados quando os dados da central mudam.
     * @param observador O observador a ser adicionado.
     */
    public void adicionarObservador(Observador observador) {
        if (this.observadores == null) {
            this.observadores = new ArrayList<>();
        }
        this.observadores.add(observador);
    }

    /**
     * Notifica todos os observadores registrados que uma mudança ocorreu.
     */
    public void notificarObservadores() {
        if (this.observadores == null) {
            return; // Nenhum observador para notificar
        }
        for (Observador observador : this.observadores) {
            observador.atualizar();
        }
    }

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
     * @throws PermissaoNegadaException Se o usuário não for coordenador
     */
    public void cadastrarEdital(Coordenador coordenador, EditalDeMonitoria edital) throws PermissaoNegadaException {
        if (coordenador == null) {
            throw new PermissaoNegadaException("cadastrar editais");
        }
        if (!adicionarEdital(edital)) {
            throw new IllegalArgumentException("Já existe um edital com o mesmo ID.");
        }
    }

    /**
     * Lista todos os alunos cadastrados no sistema.
     * Apenas coordenadores podem executar esta operação.
     * @param coordenador O coordenador que está executando a operação
     * @return Lista de alunos
     * @throws PermissaoNegadaException Se o usuário não for coordenador
     */
    public ArrayList<Aluno> listarAlunos(Coordenador coordenador) throws PermissaoNegadaException {
        if (coordenador == null) {
            throw new PermissaoNegadaException("listar alunos");
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
            if (algum.getEmail().equalsIgnoreCase(email)) {
                return algum;
            }
        }
        return null;
    }

    /**
     * Busca um usuário (Aluno ou Coordenador) pelo e-mail.
     * @param email O e-mail do usuário a ser procurado.
     * @return O objeto Usuario correspondente, ou null se não for encontrado.
     */
    public Usuario getUsuarioPorEmail(String email) {
        if (coordenador != null && coordenador.getEmail().equalsIgnoreCase(email)) {
            return coordenador;
        }
        return retornarAlunoPeloEmail(email);
    }

    /**
     * Gera e armazena um código de recuperação para um determinado e-mail.
     * @param email O e-mail para o qual o código será gerado.
     * @return O código de 6 dígitos gerado, ou null se o e-mail não for encontrado.
     */
    public String gerarCodigoRecuperacao(String email) {
        Usuario usuario = getUsuarioPorEmail(email);
        if (usuario == null) {
            return null; // Usuário não encontrado
        }
        
        if (codigosRecuperacao == null) {
            codigosRecuperacao = new HashMap<>();
        }

        String codigo = String.format("%06d", new Random().nextInt(999999));
        codigosRecuperacao.put(email.toLowerCase(), codigo);
        return codigo;
    }

    /**
     * Redefine a senha de um usuário usando um código de recuperação.
     * @param email O e-mail do usuário.
     * @param codigo O código de recuperação enviado ao usuário.
     * @param novaSenha A nova senha a ser definida.
     * @return true se a senha foi redefinida com sucesso, false caso contrário.
     */
    public boolean redefinirSenhaComCodigo(String email, String codigo, String novaSenha) {
        if (codigosRecuperacao == null || !codigosRecuperacao.containsKey(email.toLowerCase())) {
            return false; // Nenhum código foi gerado para este e-mail
        }

        String codigoArmazenado = codigosRecuperacao.get(email.toLowerCase());
        if (codigoArmazenado.equals(codigo)) {
            Usuario usuario = getUsuarioPorEmail(email);
            if (usuario != null) {
                usuario.setSenha(novaSenha);
                codigosRecuperacao.remove(email.toLowerCase()); // Invalida o código após o uso
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se as credenciais informadas pelo usuário correspondem a um login válido.
     * @param email O endereço de e-mail do usuário supostamente cadastrado
     * @param senha A senha do usuário
     * @return {@code true} se autorizado, {@code false} caso contrário
     */
    public boolean isLoginPermitido(String email, String senha) {
        Usuario usuario = getUsuarioPorEmail(email);
        return usuario != null && usuario.getSenha().equals(senha);
    }

    /**
     * Tenta obter o gênero e o primeiro nome do usuário, e então compõe uma mensagem de boas‑vindas.
     */
    public void darBoasVindasUsuario(String email, String senha) {
        Usuario usuario = getUsuarioPorEmail(email);
        if (usuario instanceof Aluno && usuario.getSenha().equals(senha)) {
            Aluno aluno = (Aluno) usuario;
            String mensagem;
            if (aluno.getGenero() == Sexo.MASCULINO) {
                mensagem = "Bem-vindo, ";
            } else if (aluno.getGenero() == Sexo.FEMININO) {
                mensagem = "Bem-vinda, ";
            } else if (aluno.getGenero() == Sexo.NAO_BINARIO) {
                mensagem = "Bem-vinde, ";
            } else {
                mensagem = "Bem-vindo, ";
            }
            String[] nome = aluno.getNome().split(" ");
            String primeiro_nome = nome[0];
            mensagem += primeiro_nome;
            mensagem += "!";
            System.out.println(mensagem);
        } else if (usuario instanceof Coordenador) {
            System.out.println("Bem-vindo, Coordenador!");
        } else {
            System.out.println("Olá usuário!");
        }
    }
}
