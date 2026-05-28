package br.com.monitoria.dao;

import br.com.monitoria.model.EditalDeMonitoria;

import java.util.List;

public class EditalNoSqlDAO implements DAO<EditalDeMonitoria, Long>{

    @Override
    public void salvar(EditalDeMonitoria editalDeMonitoria) {

    }

    @Override
    public void atualizar(EditalDeMonitoria editalDeMonitoria) {

    }

    @Override
    public void excluir(EditalDeMonitoria editalDeMonitoria) {

    }

    @Override
    public EditalDeMonitoria buscarPorId(Long aLong) {
        return null;
    }

    @Override
    public List<EditalDeMonitoria> retornarTodos() {
        return List.of();
    }
}
