package br.com.monitoria.dao;

import br.com.monitoria.model.Inscricao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class InscricaoDAO implements Dao<Inscricao, Long> {

    private final EntityManagerFactory emf;

    public InscricaoDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void salvar(Inscricao inscricao) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(inscricao);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void atualizar(Inscricao inscricao) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(inscricao);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void excluir(Inscricao inscricao) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Inscricao i = em.merge(inscricao);
            em.remove(i);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Inscricao buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Inscricao.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Inscricao> retornarTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT i FROM Inscricao i", Inscricao.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}