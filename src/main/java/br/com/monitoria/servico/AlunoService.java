package br.com.monitoria.servico;

import br.com.monitoria.GerenciadorDeDados;
import br.com.monitoria.Sexo;
import br.com.monitoria.model.Aluno;

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

        // Criação e persistência
        Aluno novoAluno = new Aluno(email, senha, nome, matricula, sexo);
        gerenciadorDeDados.salvarAluno(novoAluno);
    }
}
