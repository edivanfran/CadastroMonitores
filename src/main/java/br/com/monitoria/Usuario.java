package br.com.monitoria;

import main.java.br.com.monitoria.excecoes.LoginInvalidoException;

/**
 * Abstração que representa usuários cadastrados em uma central de informações.
 * <p>Possui nome, e-mail e senha.</p>
 * @see CentralDeInformacoes
 */
public abstract class Usuario {
    private String nome;
    public String email;
    public String senha;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Usuario(String email, String senha, String nome) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
    }

    /**
     * Testa a legitimidade das credenciais fornecidas.
     * @param email O endereço de e-mail do usuário que deseja autenticar-se
     * @param senha A senha
     * @throws LoginInvalidoException Se as credenciais forem ilegítimas; do contrário, não faz nada
     */
    public void autenticarLogin(String email, String senha) throws LoginInvalidoException {
        if (this.email.equals(email) && this.senha.equals(senha)) {
            throw new LoginInvalidoException();
        } //TODO| falta implementar
    }
}
