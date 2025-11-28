import excecoes.LoginInvalidoException;

public abstract class Usuario {
    private long id;
    private String nome;
    public String email;
    public String senha;

    public Usuario(String email, String senha, String nome) {
        this.id = System.currentTimeMillis();
        this.email = email;
        this.senha = senha;
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void fazerLogin(String email, String senha) throws LoginInvalidoException {
        if (this.email.equals(email) && this.senha.equals(senha)) {
            throw new LoginInvalidoException();
        }
    }

    public void editarPerfil(String novoEmail, String novaSenha) {
        this.email = novoEmail;
        this.senha = novaSenha;
    }

}
