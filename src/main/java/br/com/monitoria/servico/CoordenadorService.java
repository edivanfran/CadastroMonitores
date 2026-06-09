package br.com.monitoria.servico;

import br.com.monitoria.GerenciadorDeDados;
import br.com.monitoria.excecoes.CampoEmailInvalidadoException;
import br.com.monitoria.excecoes.CampoSenhaInvalidadoException;
import br.com.monitoria.model.Coordenador;
import br.com.monitoria.model.UsuarioFactory;

public class CoordenadorService {
    private GerenciadorDeDados gerenciadorDeDados;

    public CoordenadorService(){
        this.gerenciadorDeDados = GerenciadorDeDados.getInstancia();
    }

    public Coordenador cadastrarCoordenador(String nome, String email, String senha, String confirmarSenha) throws Exception {
        // Validações
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            throw new Exception("Por favor, preencha todos os campos.");
        }

        if (!senha.equals(confirmarSenha)) {
            throw new CampoSenhaInvalidadoException("As senhas não coincidem. Tente novamente.");
        }

        // Validação de email básica
        if (!email.matches("(?i)^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")) {
            throw new CampoEmailInvalidadoException("E-mail inválido. Por favor, insira um e-mail válido.");
        }

        // Adiciona verificação de e-mail existente
        if (gerenciadorDeDados.getUsuarioPorEmail(email) != null) {
            throw new CampoEmailInvalidadoException("Este e-mail já está em uso. Por favor, escolha outro.");
        }

        // Cadastra o coordenador usando a Factory
        Coordenador coordenador = UsuarioFactory.criarCoordenador(email, senha, nome);
        gerenciadorDeDados.salvarCoordenador(coordenador);
        return coordenador;
    }
}
