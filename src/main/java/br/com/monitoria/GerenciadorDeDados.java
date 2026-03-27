package br.com.monitoria;

import br.com.monitoria.excecoes.LoginInvalidoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
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

    public <T> void salvar (T entidade) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entidade);
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

    public List<Aluno> getTodosOsAlunos() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Aluno> query = em.createQuery("SELECT a FROM Alunos a", Aluno.class);
            return  query.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuario getUsuarioPorEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.email = ?", Usuario.class
            );
            query.setParameter("email", email.toLowerCase());
            return query.getSingleResult();
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
