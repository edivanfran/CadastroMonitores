package br.com.monitoria.dao;

import br.com.monitoria.model.Aluno;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AlunoDAO implements DAO<Aluno, Long> {
    private final EntityManager em;

    public AlunoDAO(EntityManager em) {
        this.em = em;
    }

    public void salvar(Aluno aluno) {
        em.persist(aluno);
    }

    public void atualizar(Aluno aluno) {
        em.merge(aluno);
    }

    public void excluir(Aluno aluno) {
        // Usa merge para trazer a entidade para o estado gerenciado antes de remover
        if (aluno != null) {
            Aluno a = em.merge(aluno);
            em.remove(a);
        }
    }

    public Aluno buscarPorId(Long id) {
        return em.find(Aluno.class, id);
    }

    public List<Aluno> retornarTodos() {
        return em.createQuery("SELECT a FROM Aluno a", Aluno.class).getResultList();
    }

    public Aluno buscarPorMatricula(String matricula) {
        try {
            TypedQuery<Aluno> query = em.createQuery("SELECT a FROM Aluno a WHERE a.matricula = :matricula", Aluno.class);
            query.setParameter("matricula", matricula);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
