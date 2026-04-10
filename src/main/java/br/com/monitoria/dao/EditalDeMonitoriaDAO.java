package br.com.monitoria.dao;

import br.com.monitoria.model.EditalDeMonitoria;
import jakarta.persistence.EntityManager;

import java.util.List;

public class EditalDeMonitoriaDAO implements Dao<EditalDeMonitoria, Long> {

    private final EntityManager em;

    public EditalDeMonitoriaDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public void salvar(EditalDeMonitoria edital) {
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
        try {
            return em.find(EditalDeMonitoria.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<EditalDeMonitoria> retornarTodos() {
        try {
            return em.createQuery("SELECT e FROM EditalDeMonitoria e", EditalDeMonitoria.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}