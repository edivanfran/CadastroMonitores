package br.com.monitoria.dao;

import br.com.monitoria.Inscricao;

import br.com.monitoria.Inscricao;
import jakarta.persistence.*;
import java.util.Optional;

public class InscricaoDAOImpl implements InscricaoDAO {
    private final EntityManagerFactory emf;

    public InscricaoDAOImpl(EntityManagerFactory emf){
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
    public Optional<Inscricao> buscarporId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Inscricao.class, id));
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
        } catch (Exception e){
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void remover(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Inscricao i = em.find(Inscricao.class, id);
            if (i != null) em.remove(i);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}

