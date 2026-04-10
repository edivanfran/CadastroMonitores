package br.com.monitoria.dao;

import br.com.monitoria.model.Disciplina;
import jakarta.persistence.EntityManager;

import java.util.List;

public class DisciplinaDAO implements DAO<Disciplina, Long> {

    private final EntityManager em;

    public DisciplinaDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public void salvar(Disciplina disciplina) {
        em.persist(disciplina);
    }

    @Override
    public void atualizar(Disciplina disciplina) {
        em.merge(disciplina);
    }

    @Override
    public void excluir(Disciplina disciplina) {
        Disciplina d = em.merge(disciplina);
        em.remove(d);
    }

    @Override
    public Disciplina buscarPorId(Long id) {
        return em.find(Disciplina.class, id);
    }

    @Override
    public List<Disciplina> retornarTodos() {
        return em.createQuery("SELECT d FROM Disciplina d", Disciplina.class)
                .getResultList();
    }
}
