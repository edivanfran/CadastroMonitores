public class Aluno extends Usuario{
	public String matricula;
	public Sexo genero;

    /*
     TODO|
      *  Colocar uma senha de recuperação para o aluno conseguir mudar o seu email e senha
      *  Fazer a verificação se a senha e o email tem espaço no meio.
     */

    public Aluno(String email, String senha, String nome, String matricula, Sexo genero) {
        super(email, senha, nome);
        this.matricula = matricula;
        this.genero = genero;
    }

	public String toString() {
		return this.getNome();
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
        //TODO| Exibe informações do edital e a opção de inscrição.
    }

    // Requisito 9: Inscrever-se em uma monitoria
    /* TODO| implementar método — necessita de classe Inscricao primeiro
    public Inscricao inscreverMonitoria(EditalDeMonitoria edital, Disciplina disciplina, double cre, double media) {
        //TODO| 1. Verificar data e limite de inscrições do edital
        //TODO| 2. Criar objeto Inscricao com este Aluno, CRE e Média
        //TODO| 3. Adicionar a inscrição à lista do Edital.
        return new Inscricao(this, disciplina, cre, media);
    }
     */

    // Requisito 12: Desistir de inscrição
    /* TODO| implementar método — necessita de classe Inscricao primeiro
    public void desistirInscricao(Inscricao inscricao) {
        //TODO| Marca a inscrição como desistida (inscricao.setDesistiu(true))
        //TODO| Se a inscrição contemplada, deve acionar o recálculo do ranque (no Edital/Coordenador) [cite: 49]
    }
     */
}
