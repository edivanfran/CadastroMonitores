package br.com.monitoria.dao;

import br.com.monitoria.model.Aluno;
import jakarta.persistence.EntityManager;

public class AlunoDAO implements Dao<Aluno, Long> {
    private final EntityManager em;

    public AlunoDAO(EntityManager em) {
        this.em = em;
    }

    public void salvar(Aluno aluno) {
        em.getTransaction().begin();
        em.persist(aluno);
        em.getTransaction().commit();
    }

    public void atualizar(Aluno aluno) {
        em.getTransaction().begin();
        em.merge(aluno);
        em.getTransaction().commit();
    }

    public void excluir(Aluno aluno) {
        try {
            em.getTransaction().begin();
            Aluno al = em.find(Aluno.class, aluno.getId());
            if (al != null) em.remove(al);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public Aluno buscarPorId(Long id) {
        return em.find(Aluno.class, id);
    }

    public java.util.List<Aluno> retornarTodos() {
        return em.createQuery("SELECT a FROM Aluno a", Aluno.class).getResultList();
    }

}
