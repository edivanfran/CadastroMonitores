package br.com.monitoria;

/**
 * Representa um usuário que tenha sido cadastrado numa central de informações como aluno. Possui e-mail, senha, nome e gênero.
 * Possui também um número de matrícula, que serve como identificador exclusivo do aluno.
 */
public class Aluno extends Usuario{
	public String matricula;
	public Sexo genero;
    public String getMatricula() {
        return matricula;
    }
    public Sexo getGenero() {
        return genero;
    }
    public void setGenero(Sexo genero) {
        this.genero = genero;
    }


    public Aluno(String email, String senha, String nome, String matricula, Sexo genero) {
        super(email, senha, nome);
        this.matricula = matricula;
        this.genero = genero;
    }

    /**
     * Representação em {@code String} do objeto.
     * @return O nome do aluno
     */
	public String toString() {
		return this.getNome();
	}

}
