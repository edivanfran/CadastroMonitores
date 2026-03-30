package br.com.monitoria.dao;

import br.com.monitoria.Disciplina;

import java.util.Optional;

public interface DisciplinaDAO {
    void salvar(Disciplina disciplina);
    Optional<Disciplina> buscarporId(Long id);
    void atualizar(Disciplina disciplina);
    void remover(Long id);
}
