package br.com.monitoria.dao;

import br.com.monitoria.model.Coordenador;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CoordenadorDAO implements DAO<Coordenador, Long> {
    private EntityManager em;

    public CoordenadorDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public void salvar(Coordenador coordenador) {
        em.persist(coordenador);
    }

    @Override
    public void atualizar(Coordenador coordenador) {
        em.merge(coordenador);
    }

    @Override
    public void excluir(Coordenador coordenador) {
        if (coordenador != null) {
            Coordenador c = em.merge(coordenador);
            em.remove(c);
        }
    }

    @Override
    public Coordenador buscarPorId(Long aLong) {
        return em.find(Coordenador.class, aLong);
    }

    @Override
    public List<Coordenador> retornarTodos() {
        return em.createQuery("SELECT c FROM Coordenador c", Coordenador.class).getResultList();
    }
}
