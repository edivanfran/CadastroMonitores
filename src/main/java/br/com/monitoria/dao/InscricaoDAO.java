package br.com.monitoria.dao;

import br.com.monitoria.model.Aluno;
import br.com.monitoria.model.Inscricao;
import jakarta.persistence.EntityManager;

import java.util.List;
@Deprecated
public class InscricaoDAO implements DAO<Inscricao, Long> {

    private final EntityManager em;

    public InscricaoDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public void salvar(Inscricao inscricao) {
        em.persist(inscricao);
    }

    @Override
    public void atualizar(Inscricao inscricao) {
        em.merge(inscricao);
    }

    @Override
    public void excluir(Inscricao inscricao) {
        Inscricao i = em.merge(inscricao);
        em.remove(i);
    }

    @Override
    public Inscricao buscarPorId(Long id) {
        return em.find(Inscricao.class, id);
    }

    @Override
    public List<Inscricao> retornarTodos() {
        return em.createQuery("SELECT i FROM Inscricao i", Inscricao.class)
                .getResultList();
    }

    public List<Inscricao> buscarPorAluno(Aluno aluno) {
        String jpql = "SELECT i FROM Inscricao i JOIN FETCH i.edital e JOIN FETCH i.disciplina d WHERE i.aluno = :aluno";
        return em.createQuery(jpql, Inscricao.class).setParameter("aluno", aluno).getResultList();
    }
}
