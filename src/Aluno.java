
public class Aluno extends Usuario{
	public String nome;
	public String matricula;
	public Sexo genero;

    // Colocar uma senha de recuperação para o aluno conseguir mudar o seu email e senha
    // Fazer a verificação se a senha e o email tem espaço no meio.

    public Aluno(String email, String senha, String nome, String matricula, Sexo genero) {
        super(email, senha);
        this.nome = nome;
        this.matricula = matricula;
        this.genero = genero;
    }

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

	public Sexo getGenero() {
		return genero;
	}

	public void setGenero(Sexo genero) {
		this.genero = genero;
	}

    // Requisito 8: Detalhar Edital sem Resultado (para ver a opção de se inscrever)
    public void detalharEditalEmAberto(EditalDeMonitoria edital) {
        // Exibe informações do edital e a opção de inscrição.
    }

    // Requisito 9: Inscrever-se em uma monitoria
    public Inscricao inscreverMonitoria(EditalDeMonitoria edital, Disciplina disciplina, double cre, double media) {
        // 1. Verificar data e limite de inscrições do edital [cite: 19, 37]
        // 2. Criar objeto Inscricao com este Aluno, CRE e Média [cite: 38]
        // 3. Adicionar a inscrição à lista do Edital.
        return new Inscricao(this, disciplina, cre, media);
    }

    // Requisito 12: Desistir de inscrição
    public void desistirInscricao(Inscricao inscricao) {
        // Marca a inscrição como desistida (inscricao.setDesistiu(true)) [cite: 47]
        // Se a inscrição contemplada, deve acionar o recálculo do ranque (no Edital/Coordenador) [cite: 49]
    }

    public boolean fazerLogin(String email, String senha) {
        return super.fazerLogin(email, senha);
    }

    public void editarPerfil(String novoEmail, String novaSenha) {
        super.editarPerfil(novoEmail, novaSenha);
    }
	
}
