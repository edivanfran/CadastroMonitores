package br.com.monitoria;

import java.util.ArrayList;
import br.com.monitoria.excecoes.*;

/**
 * <p>Representa o usuário atribuído na central de informações como o Coordenador do Curso.
 * Possui e-mail, senha e nome, mas não precisa de matrícula.</p>
 * <p>Só pode haver um Coordenador em um mesmo objeto {@link CentralDeInformacoes}.</p>
 */
public class Coordenador extends Usuario {

    public Coordenador(String email, String senha, String nome) {
        super(email, senha, nome);
    }


}
