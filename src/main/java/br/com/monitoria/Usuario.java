package br.com.monitoria;

import br.com.monitoria.excecoes.LoginInvalidoException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

/**
 * Abstração que representa usuários cadastrados em uma central de informações.
 * <p>Possui nome, e-mail e senha.</p>
 * @see CentralDeInformacoes
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

    /**
     * Testa a legitimidade das credenciais fornecidas.
     * @param email O endereço de e-mail do usuário que deseja autenticar-se
     * @param senha A senha
     * @throws LoginInvalidoException Se as credenciais forem ilegítimas; do contrário, não faz nada
     */
    public void autenticarLogin(String email, String senha) throws LoginInvalidoException {
        if (!this.email.equals(email) || !this.senha.equals(senha)) {
            throw new LoginInvalidoException();
        }
    }
}
