package br.com.monitoria.dao;

import br.com.monitoria.Disciplina;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;


import java.util.Optional;

public class DisciplinaDAOImpl implements DisciplinaDAO{

    private final EntityManagerFactory emf;

    public DisciplinaDAOImpl(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public void salvar(Disciplina disciplina) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(disciplina);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Disciplina> buscarporId(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            Disciplina d = em.find(Disciplina.class, id);
            return Optional.ofNullable(d);
        } finally {
            em.close();
        }
    }

    @Override
    public void atualizar(Disciplina disciplina) {
        EntityManager em = emf.createEntityManager();

        try{
            em.getTransaction().begin();
            em.merge(disciplina);
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
            Disciplina d = em.find(Disciplina.class, id);
            if (d != null) em.remove(d);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
