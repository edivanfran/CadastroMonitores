
public class Aluno {
	public String nome;
	public String matricula;
	public String senha;
	public String email;
	public Sexo genero;
	
	public Aluno(String n, String m, String s, String e, Sexo g) {
		nome = n;
		matricula = m; //Vê como a matricula vai ser gerada para inserir aqui, na Central não tem como ter duas matriculas iguais.
		senha = s;
		email = e;
		genero = g; // Lembrar de usar um Sexo.valueOf("String".toUpperCase()) na hora de inserir.
	}

    // isso é um test de commit

	public String toString() {
		return nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMatricula() {
		return matricula;
	}
		
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Sexo getGenero() {
		return genero;
	}

	public void setGenero(Sexo genero) {
		this.genero = genero;
	}
	
}
