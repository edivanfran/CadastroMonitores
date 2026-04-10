package br.com.monitoria.dao;

import br.com.monitoria.model.EditalDeMonitoria;
import jakarta.persistence.EntityManager;

import java.util.List;

public class EditalDeMonitoriaDAO implements DAO<EditalDeMonitoria, Long> {

    private final EntityManager em;

    public EditalDeMonitoriaDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public void salvar(EditalDeMonitoria edital) {
        em.persist(edital);
    }

    @Override
    public void atualizar(EditalDeMonitoria edital) {
        em.merge(edital);
    }

    @Override
    public void excluir(EditalDeMonitoria edital) {
        EditalDeMonitoria e = em.merge(edital);
        em.remove(e);
    }

    @Override
    public EditalDeMonitoria buscarPorId(Long id) {
        return em.find(EditalDeMonitoria.class, id);
    }

    @Override
    public List<EditalDeMonitoria> retornarTodos() {
        return em.createQuery("SELECT e FROM EditalDeMonitoria e", EditalDeMonitoria.class)
                .getResultList();
    }
}
