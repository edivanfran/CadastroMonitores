package br.com.monitoria;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Representa um usuário que tenha sido cadastrado numa central de informações como aluno. Possui e-mail, senha, nome e gênero.
 * Possui também um número de matrícula, que serve como identificador exclusivo do aluno.
 */
@Entity
@DiscriminatorValue("A")
public class Aluno extends Usuario{
	@Column(unique = true, nullable = false)
	public String matricula;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
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

    protected Aluno() {
        // Construtor para JPA
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

    @Override
    public void abrirTelaCadastroEdital(JFrame telaPai) {
        // O Aluno NÃO PODE. Então ele mostra uma mensagem de erro.
        JOptionPane.showMessageDialog(telaPai, "Apenas coordenadores podem cadastrar editais.", "Acesso Negado", JOptionPane.WARNING_MESSAGE);
    }

}
