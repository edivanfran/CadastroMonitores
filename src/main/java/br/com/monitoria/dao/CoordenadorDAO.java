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
        this.em.persist(coordenador);
    }

    @Override
    public void atualizar(Coordenador coordenador) {
        this.em.merge(coordenador);
    }

    @Override
    public void excluir(Coordenador coordenador) {
        this.em.remove(coordenador);
    }

    @Override
    public Coordenador buscarPorId(Long aLong) {
        return this.em.find(Coordenador.class, aLong);
    }

    @Override
    public List<Coordenador> retornarTodos() {
        return this.em.createQuery("SELECT c FROM Coordenador c", Coordenador.class).getResultList();
    }
}
