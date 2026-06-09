package br.com.monitoria.servico;

import br.com.monitoria.GerenciadorDeDados;
import br.com.monitoria.Sexo;
import br.com.monitoria.model.Aluno;
import br.com.monitoria.model.Inscricao;
import br.com.monitoria.model.UsuarioFactory;

import java.util.List;
import java.util.stream.Collectors;

public class AlunoService {

    private GerenciadorDeDados gerenciadorDeDados;

    public AlunoService() {
        this.gerenciadorDeDados = GerenciadorDeDados.getInstancia();
    }

    public void cadastrarAluno(String nome, String matricula, String email, Sexo sexo, String senha, String confirmarSenha) throws Exception {
        // Validação de campos
        if (nome.isEmpty() || matricula.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            throw new Exception("Por favor, preencha todos os campos.");
        }

        if (!senha.equals(confirmarSenha)) {
            throw new Exception("As senhas não coincidem.");
        }

        if (!email.matches("(?i)^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")) {
            throw new Exception("E-mail inválido. Por favor, insira um e-mail válido.");
        }

        // Validação de duplicidade
        if (gerenciadorDeDados.getUsuarioPorEmail(email) != null) {
            throw new Exception("Já existe um usuário cadastrado com este e-mail.");
        }
        if (gerenciadorDeDados.buscarAlunoPorMatricula(matricula) != null) {
            throw new Exception("Já existe um usuário cadastrado com esta matrícula.");
        }

        // Criação e persistência usando a Factory
        Aluno novoAluno = UsuarioFactory.criarAluno(email, senha, nome, matricula, sexo);
        gerenciadorDeDados.salvarAluno(novoAluno);
    }

    public void atualizarPerfil(Aluno aluno, String nome, String senha, Sexo genero) throws Exception {
        // Validar nome
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("O nome não pode estar em branco.");
        }
        if (!nome.matches("^[A-ZÀ-Ÿ][a-zà-ÿ]+(?: (?:[dD]e|[dD]a|[dD]os|[dD]as|[eE])? ?[A-ZÀ-Ÿ]?[a-zà-ÿ]+)+$")) {
            throw new Exception("Nome inválido. Por favor, insira um nome completo válido.");
        }

        // Validar senha
        if (senha == null || senha.isEmpty()) {
            throw new Exception("A senha não pode estar em branco.");
        }

        // Atualizar dados do aluno
        aluno.setNome(nome);
        aluno.setSenha(senha);
        aluno.setGenero(genero);

        // Salva na persistência
        gerenciadorDeDados.atualizarAluno(aluno);
    }

    public List<Inscricao> getHistoricoInscricoes(Aluno aluno) {
        List<Inscricao> todasInscricoes = gerenciadorDeDados.getInscricoesPorAluno(aluno);
        
        if (todasInscricoes == null) {
            return List.of(); // Retorna lista vazia se não houver inscrições
        }

        // Filtra para retornar apenas as inscrições ativas (não desistiu)
        return todasInscricoes.stream()
                .filter(inscricao -> !inscricao.isDesistiu())
                .collect(Collectors.toList());
    }
}
