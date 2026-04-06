package br.com.monitoria.dao;

import br.com.monitoria.model.EditalDeMonitoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;

public class EditalDeMonitoriaDAO implements Dao<EditalDeMonitoria, Long> {

    private final EntityManagerFactory emf;

    public EditalDeMonitoriaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void salvar(EditalDeMonitoria edital) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(edital);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void atualizar(EditalDeMonitoria edital) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(edital);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void excluir(EditalDeMonitoria edital) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            EditalDeMonitoria e = em.merge(edital);
            em.remove(e);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public EditalDeMonitoria buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(EditalDeMonitoria.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<EditalDeMonitoria> retornarTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT e FROM EditalDeMonitoria e", EditalDeMonitoria.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}