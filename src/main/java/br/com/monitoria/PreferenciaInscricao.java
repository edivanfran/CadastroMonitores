package br.com.monitoria;

/**
 * Enum que representa a preferência do aluno ao se inscrever em uma monitoria.
 * Isso ajuda o sistema a decidir como alocar as vagas (remuneradas e voluntárias)
 * de acordo com a escolha do aluno.
 */
public enum PreferenciaInscricao {

    SOMENTE_REMUNERADA,
    REMUNERADA_OU_VOLUNTARIA,
    SOMENTE_VOLUNTARIA;

}
