import java.util.ArrayList;

public class CentralDeInformacoes {
    private Coordenador coordenador;
    private ArrayList<Aluno> todosOsAlunos = new ArrayList<Aluno>();

    public void cadastrarCoordenador(String email, String senha) {
        this.coordenador = new Coordenador(email, senha);
    }
    public Coordenador getCoordenador() {
        return coordenador;
    }
    public boolean temCoordenador() {
        return coordenador != null;
    }

    public boolean adicionarAluno(Aluno outro) {
        for (Aluno algum : todosOsAlunos) {
            if (algum.getMatricula().equals(outro.getMatricula())) {
                return false;
            }
        }
        todosOsAlunos.add(outro);
        return true;
    }

    public Aluno recuperarAluno(String matricula) {
        for (Aluno algum : todosOsAlunos) {
            if (algum.getMatricula().equals(matricula)) {
                return algum;
            }
        }
        return null;
    }

    public ArrayList<Aluno> getTodosOsAlunos(){
        return todosOsAlunos;
    }
    public void setTodosOsAlunos(ArrayList<Aluno> listaAtualizada) {
        this.todosOsAlunos = listaAtualizada;
    }
    public ArrayList<EditalDeMonitoria> getTodosOsEditais() {
        return todosOsEditais;
    }
    public void setTodosOsEditais(ArrayList<EditalDeMonitoria> todosOsEditais) {
        this.todosOsEditais = todosOsEditais;
    }
    private ArrayList<EditalDeMonitoria> todosOsEditais = new ArrayList<EditalDeMonitoria>();

    public void mostrarIdEditais() {
        if (todosOsEditais.isEmpty()) {
            System.out.println("Lista vazia");
            return;
        }
        for (EditalDeMonitoria edital : todosOsEditais) {
            System.out.printf("Disciplina: %s Id: %s \n", edital.getDisciplinas(), edital.getId());
        }
    }

    public EditalDeMonitoria recuperarEdital(long id) {
        for (EditalDeMonitoria edital : todosOsEditais) {
            if (edital.getId() == id) {
                return edital;
            }
        }
        return null;
    }

    public boolean adicionarEdital(EditalDeMonitoria edital) {
        for (EditalDeMonitoria e : todosOsEditais) {
            if (e.getId() == edital.getId()) {
                return false;
            }
        }
        todosOsEditais.add(edital);
        return true;
    }

    public Aluno retornarAlunoPeloEmail(String email) {
        for (Aluno algum: todosOsAlunos) {
            if (algum.getEmail().equals(email)) {
                return algum;
            }
        }
        return null;
    }

    //Poderia fazer uma Classe Usuário e fazer Alunos e Coordenador herdar dela, assim não precisariamos de um
    //um métoodo autenticar em Coordenador, poderia mos fazer um polimorfismo nesse métoodo com um Array de Usuários
    //com isso ele poderia acessar tanto os emails de Coordenador e de Aluno.
    public boolean isLoginPermitido(String email, String senha) {
        for (Aluno algum: todosOsAlunos) {
            if (algum.getEmail().equals(email) && algum.getSenha().equals(senha)) {
                return true;
            }
        }
        return false;
    }

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

    public ArrayList<Disciplina> recuperarInscricoesDeUmAlunoEmUmEdital(String matricula, long idEdital) {
        /*pass*/
        return null;
    }
}
