package br.com.monitoria;

import br.com.monitoria.dao.*;
import br.com.monitoria.excecoes.LoginInvalidoException;
import br.com.monitoria.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

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

    // Métodos para EditalDeMonitoria

    public void salvarEdital(EditalDeMonitoria edital) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new EditalDeMonitoriaDAO(em).salvar(edital);
            em.getTransaction().commit();
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

    // Métodos de Autenticação e Usuário Genérico

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

    public Usuario autenticarUsuario(String email, String senha) throws LoginInvalidoException {
        Usuario usuario = getUsuarioPorEmail(email);
        if (usuario == null) {
            throw new LoginInvalidoException();
        }

        usuario.autenticarLogin(email, senha);
        return usuario;
    }

    public void fechar() {
        if (emf != null && emf.isOpen()){
            emf.close();
        }
    }
}
