
public class Coordenador {
    private String nome;
    private String email;
    private String senha;

    public Coordenador(String email, String senha) {
        this.email = email;
        this.senha = senha;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean autenticar(String email, String senha) {
        return this.email.equals(email) && this.senha.equals(senha);
    }
}
