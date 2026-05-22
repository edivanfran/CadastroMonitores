package br.com.monitoria.model;

import jakarta.persistence.*;

import javax.swing.*;
import java.util.Objects;

/**
 * Abstração que representa usuários cadastrados em uma central de informações.
 * <p>Possui nome, e-mail e senha.</p>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo")
public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String senha;

    protected Usuario() {
        // Construtor para JPA
    }

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

    public Long getId() {
        return id;
    }

    public Usuario(String email, String senha, String nome) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Testa a legitimidade das credenciais fornecidas.
     * @param email O endereço de e-mail do usuário que deseja autenticar-se
     * @param senha A senha
     */
    public boolean autenticarUsuario(String email, String senha) {
        if (!this.email.equals(email) || !this.senha.equals(senha)) {
            return false;
        }
        return true;
    }
}
