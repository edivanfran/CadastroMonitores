package br.com.monitoria.dao;

import br.com.monitoria.EditalDeMonitoria;
import com.mysql.cj.x.protobuf.MysqlxCursor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.Optional;

public class EditalDeMonitoriaDAOImpl implements EditalDeMonitoriaDAO {

    private final EntityManagerFactory emf;

    public EditalDeMonitoriaDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void salvar(EditalDeMonitoria edital) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(edital);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<EditalDeMonitoria> buscarporId(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            EditalDeMonitoria e = em.find(EditalDeMonitoria.class, id);
            return Optional.ofNullable(e);
        } finally {
            em.close();
        }
    }

    @Override
    public void remover(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            EditalDeMonitoria e = em.find(EditalDeMonitoria.class, id);
            if (e != null) em.remove(e);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void aturalizar(EditalDeMonitoria edital) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.merge(edital);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

}
