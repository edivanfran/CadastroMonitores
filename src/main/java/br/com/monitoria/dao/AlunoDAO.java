package br.com.monitoria.dao;

import br.com.monitoria.model.Aluno;
import jakarta.persistence.EntityManager;

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
        Aluno al = em.find(Aluno.class, aluno.getId());
        if (al != null) em.remove(al);
    }

    public Aluno buscarPorId(Long id) {
        return em.find(Aluno.class, id);
    }

    public java.util.List<Aluno> retornarTodos() {
        return em.createQuery("SELECT a FROM Aluno a", Aluno.class).getResultList();
    }

}
