package br.com.monitoria.dao;

import java.util.List;

public interface DAO<C, I> {
    void salvar(C c);

    void atualizar(C c);

    void excluir(C c);

    C buscarPorId(I i);

    List<C> retornarTodos();
}
