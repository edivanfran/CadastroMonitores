package br.com.monitoria.dao;

import br.com.monitoria.model.Disciplina;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class DisciplinaDAO implements DAO<Disciplina, Long> {

    private final EntityManagerFactory emf;

    public DisciplinaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void salvar(Disciplina disciplina) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(disciplina);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void atualizar(Disciplina disciplina) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(disciplina);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void excluir(Disciplina disciplina) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Disciplina d = em.merge(disciplina);
            em.remove(d);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Disciplina buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Disciplina.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Disciplina> retornarTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT d FROM Disciplina d", Disciplina.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
