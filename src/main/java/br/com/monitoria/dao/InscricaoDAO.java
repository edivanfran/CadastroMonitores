package br.com.monitoria.dao;

import br.com.monitoria.Inscricao;

import java.util.Optional;

public interface InscricaoDAO {
    void salvar(Inscricao inscricao);
    Optional<Inscricao> buscarporId(Long id);
    void atualizar(Inscricao inscricao);
    void remover(Long id);
}
