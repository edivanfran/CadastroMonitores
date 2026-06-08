package br.com.monitoria.servico;

import br.com.monitoria.GerenciadorDeDados;
import br.com.monitoria.SessaoUsuario;
import br.com.monitoria.excecoes.CamposObrigatoriosException;
import br.com.monitoria.excecoes.CoordenadorNaoCadastradoException;
import br.com.monitoria.excecoes.CredenciaisInvalidasException;
import br.com.monitoria.model.Aluno;
import br.com.monitoria.model.Coordenador;
import br.com.monitoria.model.Usuario;

public class LoginService {

    private GerenciadorDeDados gerenciadorDeDados;
    private SessaoUsuario sessaoUsuario;

    public LoginService() {
        this.gerenciadorDeDados = GerenciadorDeDados.getInstancia();
        this.sessaoUsuario = SessaoUsuario.getInstancia();
    }

    public void autenticar(String email, String senha) throws CamposObrigatoriosException, CoordenadorNaoCadastradoException, CredenciaisInvalidasException {
        
        if (gerenciadorDeDados.getTodosOsCoordenadores().isEmpty()) {
            throw new CoordenadorNaoCadastradoException("Nenhum coordenador encontrado. É necessário cadastrar um administrador primeiro.");
        }

        if (email == null || email.trim().isEmpty() || senha == null || senha.isEmpty()) {
            throw new CamposObrigatoriosException("Por favor, preencha todos os campos.");
        }

        // Tenta autenticar como Coordenador
        Coordenador coordenador = gerenciadorDeDados.getCoordenador();
        if (coordenador.getEmail().equalsIgnoreCase(email) && coordenador.getSenha().equals(senha)) {
            sessaoUsuario.setUsuarioLogado(coordenador);
            return; // Sucesso
        }

        // Tenta autenticar como Aluno
        Usuario usuario = gerenciadorDeDados.getUsuarioPorEmail(email);
        if (usuario instanceof Aluno && usuario.getSenha().equals(senha)) {
            sessaoUsuario.setUsuarioLogado(usuario);
            return; // Sucesso
        }

        throw new CredenciaisInvalidasException("E-mail ou senha inválidos. Tente novamente.");
    }
}
