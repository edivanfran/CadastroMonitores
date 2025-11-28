public abstract class Usuario {
    private long id;
    public String email;
    public String senha;

    public Usuario(String email, String senha) {
        this.id = System.currentTimeMillis();
        this.email = email;
        this.senha = senha;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean fazerLogin(String email, String senha) {
        return this.email.equals(email) && this.senha.equals(senha);
    }

    public void editarPerfil(String novoEmail, String novaSenha) {
        this.email = novoEmail;
        this.senha = novaSenha;
    }

}
