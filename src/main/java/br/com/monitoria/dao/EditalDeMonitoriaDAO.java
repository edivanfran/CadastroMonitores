package br.com.monitoria.dao;

import br.com.monitoria.model.EditalDeMonitoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

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
        TypedQuery<EditalDeMonitoria> query = em.createQuery(
                "SELECT e FROM EditalDeMonitoria e LEFT JOIN FETCH e.disciplinas WHERE e.id = :id", EditalDeMonitoria.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<EditalDeMonitoria> retornarTodos() {
        // Usando LEFT JOIN FETCH para garantir que editais sem disciplinas também sejam retornados
        return em.createQuery("SELECT e FROM EditalDeMonitoria e LEFT JOIN FETCH e.disciplinas", EditalDeMonitoria.class)
                .getResultList();
    }
}
