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
        // Buscar o edital e suas disciplinas
        TypedQuery<EditalDeMonitoria> query = em.createQuery(
                "SELECT DISTINCT e FROM EditalDeMonitoria e LEFT JOIN FETCH e.disciplinas WHERE e.id = :id", EditalDeMonitoria.class);
        query.setParameter("id", id);
        EditalDeMonitoria edital = query.getSingleResult();

        // Buscar as inscrições para o edital encontrado
        if (edital != null) {
            TypedQuery<EditalDeMonitoria> inscricoesQuery = em.createQuery(
                    "SELECT DISTINCT e FROM EditalDeMonitoria e LEFT JOIN FETCH e.inscricoes WHERE e.id = :id", EditalDeMonitoria.class);
            inscricoesQuery.setParameter("id", id);
            edital = inscricoesQuery.getSingleResult();
        }

        return edital;
    }

    @Override
    public List<EditalDeMonitoria> retornarTodos() {
        // Buscar todos os editais e suas disciplinas
        List<EditalDeMonitoria> editais = em.createQuery(
                "SELECT DISTINCT e FROM EditalDeMonitoria e LEFT JOIN FETCH e.disciplinas", EditalDeMonitoria.class)
                .getResultList();

        // Buscar as inscrições para os editais encontrados
        if (editais != null && !editais.isEmpty()) {
            // A lista retornada terá os mesmos editais
            editais = em.createQuery(
                "SELECT DISTINCT e FROM EditalDeMonitoria e LEFT JOIN FETCH e.inscricoes WHERE e IN :editais", EditalDeMonitoria.class)
                .setParameter("editais", editais)
                .getResultList();
        }

        return editais;
    }
}
