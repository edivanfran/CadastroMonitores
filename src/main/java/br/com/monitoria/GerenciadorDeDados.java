package br.com.monitoria;

import br.com.monitoria.dao.AlunoDAO;
import br.com.monitoria.dao.CoordenadorDAO;
import br.com.monitoria.dao.DisciplinaDAO;
import br.com.monitoria.dao.EditalDeMonitoriaDAO;
import br.com.monitoria.dao.InscricaoDAO;
import br.com.monitoria.model.*;
import jakarta.persistence.*;

import java.util.List;

public class GerenciadorDeDados {

    private static GerenciadorDeDados instancia;
    private final EntityManagerFactory emf;

    private GerenciadorDeDados() {
        this.emf = Persistence.createEntityManagerFactory("monitoriaPU");
    }

    public static synchronized GerenciadorDeDados getInstancia() {
        if (instancia == null){
            instancia = new GerenciadorDeDados();
        }
        return instancia;
    }

    // Métodos para aluno
    public void salvarAluno(Aluno aluno) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new AlunoDAO(em).salvar(aluno);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void atualizarAluno(Aluno aluno) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new AlunoDAO(em).atualizar(aluno);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void removerAluno(Aluno aluno) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new AlunoDAO(em).excluir(aluno);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Aluno buscarAlunoPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return new AlunoDAO(em).buscarPorId(id);
        } finally {
            em.close();
        }
    }

    public Aluno buscarAlunoPorMatricula(String matricula) {
        EntityManager em = emf.createEntityManager();
        try {
            return new AlunoDAO(em).buscarPorMatricula(matricula);
        } finally {
            em.close();
        }
    }

    public List<Aluno> getTodosOsAlunos() {
        EntityManager em = emf.createEntityManager();
        try {
            return new AlunoDAO(em).retornarTodos();
        } finally {
            em.close();
        }
    }

    // Métodos para coordenador

    public void salvarCoordenador(Coordenador coordenador) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new CoordenadorDAO(em).salvar(coordenador);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void atualizarCoordenador(Coordenador coordenador) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new CoordenadorDAO(em).atualizar(coordenador);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void removerCoordenador(Coordenador coordenador) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new CoordenadorDAO(em).excluir(coordenador);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Coordenador buscarCoordenadorPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return new CoordenadorDAO(em).buscarPorId(id);
        } finally {
            em.close();
        }
    }

    public Coordenador getCoordenador() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Coordenador> coordenadores = new CoordenadorDAO(em).retornarTodos();
            return coordenadores.isEmpty() ? null : coordenadores.get(0);
        } finally {
            em.close();
        }
    }

    public List<Coordenador> getTodosOsCoordenadores() {
        EntityManager em = emf.createEntityManager();
        try {
            return new CoordenadorDAO(em).retornarTodos();
        } finally {
            em.close();
        }
    }

    // Métodos para EditalDeMonitoria

    public void salvarEdital(EditalDeMonitoria edital) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new EditalDeMonitoriaDAO(em).salvar(edital);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void atualizarEdital(EditalDeMonitoria edital) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new EditalDeMonitoriaDAO(em).atualizar(edital);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void removerEdital(EditalDeMonitoria edital) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new EditalDeMonitoriaDAO(em).excluir(edital);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }


    public EditalDeMonitoria buscarEditalPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return new EditalDeMonitoriaDAO(em).buscarPorId(id);
        } finally {
            em.close();
        }
    }

    public List<EditalDeMonitoria> getTodosOsEditais() {
        EntityManager em = emf.createEntityManager();
        try {
            return new EditalDeMonitoriaDAO(em).retornarTodos();
        } finally {
            em.close();
        }
    }

    // Métodos para disciplina

    public void salvarDisciplina(Disciplina disciplina) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new DisciplinaDAO(em).salvar(disciplina);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void atualizarDisciplina(Disciplina disciplina) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new DisciplinaDAO(em).atualizar(disciplina);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void removerDisciplina(Disciplina disciplina) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new DisciplinaDAO(em).excluir(disciplina);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Disciplina buscarDisciplinaPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return new DisciplinaDAO(em).buscarPorId(id);
        } finally {
            em.close();
        }
    }

    public List<Disciplina> getTodasAsDisciplinas() {
        EntityManager em = emf.createEntityManager();
        try {
            return new DisciplinaDAO(em).retornarTodos();
        } finally {
            em.close();
        }
    }

    public void adicionarDisciplinaAoEdital(EditalDeMonitoria edital, Disciplina novaDisciplina) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Traz o edital para o estado gerenciado
            EditalDeMonitoria editalGerenciado = em.merge(edital);

            // Adiciona a nova disciplina que ainda não foi persistida
            editalGerenciado.adicionarDisciplina(novaDisciplina);

            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void removerDisciplinaDoEdital(EditalDeMonitoria edital, Disciplina disciplina) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            EditalDeMonitoria editalGerenciado = em.find(EditalDeMonitoria.class, edital.getId());
            Disciplina disciplinaGerenciada = em.find(Disciplina.class, disciplina.getId());

            if (editalGerenciado != null && disciplinaGerenciada != null) {
                editalGerenciado.getDisciplinas().remove(disciplinaGerenciada);
                em.remove(disciplinaGerenciada);
            }
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // Métodos de Inscricao
    public List<Inscricao> getInscricoesPorAluno(Aluno aluno) {
        EntityManager em = emf.createEntityManager();
        try {
            InscricaoDAO inscricaoDAO = new InscricaoDAO(em);
            return inscricaoDAO.buscarPorAluno(aluno);
        } finally {
            em.close();
        }
    }

    public void inscreverAlunoEmEdital(EditalDeMonitoria edital, Aluno aluno, Disciplina disciplina, double cre, double nota, Vaga tipoVaga, int ordem, PreferenciaInscricao pref) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            EditalDeMonitoria editalGerenciado = em.find(EditalDeMonitoria.class, edital.getId());
            if (editalGerenciado == null) {
                throw new IllegalArgumentException("Edital não encontrado.");
            }

            // Encontra a disciplina gerenciada e inicializa suas coleções
            Disciplina disciplinaGerenciada = null;
            DisciplinaDAO disciplinaDAO = new DisciplinaDAO(em);
            for (Disciplina d : editalGerenciado.getDisciplinas()) {
                if (d.getId().equals(disciplina.getId())) {
                    disciplinaGerenciada = d;
                    // Usa o métoodo do DAO para inicializar as coleções
                    disciplinaDAO.inicializarColecoesAlunos(disciplinaGerenciada);
                    break;
                }
            }

            if (disciplinaGerenciada == null) {
                throw new IllegalArgumentException("Disciplina não encontrada no edital.");
            }

            // Passa o objeto Disciplina em vez do nome
            editalGerenciado.inscreverAluno(aluno, disciplinaGerenciada, cre, nota, tipoVaga, ordem, pref);

            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }


    // Métodos de Autenticação e Usuário Genérico

    public void atualizarUsuario(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Usuario getUsuarioPorEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class
            );
            query.setParameter("email", email.toLowerCase());
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public Usuario autenticarUsuario(String email, String senha) {
        Usuario usuario = getUsuarioPorEmail(email);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            return usuario;
        }
        return null;
    }

    public boolean isLoginPermitido(String email, String senha) {
        Usuario usuario = getUsuarioPorEmail(email);
        if (usuario == null) {
            return false;
        }
        return usuario.autenticarUsuario(email, senha);
    }

    public void fechar() {
        if (emf != null && emf.isOpen()){
            emf.close();
        }
    }
}
