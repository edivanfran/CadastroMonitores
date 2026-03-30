package br.com.monitoria.dao;

import br.com.monitoria.EditalDeMonitoria;

import java.util.Optional;

public interface EditalDeMonitoriaDAO {
    void salvar(EditalDeMonitoria edital);
    Optional<EditalDeMonitoria> buscarporId(Long id);
    void remover(Long id);
    void aturalizar(EditalDeMonitoria edital);
}
