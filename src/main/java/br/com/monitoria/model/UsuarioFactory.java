package br.com.monitoria.model;

import br.com.monitoria.Sexo;

public class UsuarioFactory {

    public static Aluno criarAluno(String email, String senha, String nome, String matricula, Sexo genero) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail do aluno é obrigatório.");
        }
        if (matricula == null || matricula.isBlank()) {
            throw new IllegalArgumentException("Matrícula do aluno é obrigatória.");
        }
        return new Aluno(email, senha, nome, matricula, genero);
    }

    public static Coordenador criarCoordenador(String email, String senha, String nome) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail do coordenador é obrigatório.");
        }
        return new Coordenador(email, senha, nome);
    }
}
