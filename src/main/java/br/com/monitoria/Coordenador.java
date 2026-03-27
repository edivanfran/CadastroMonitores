package br.com.monitoria;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * <p>Representa o usuário atribuído na central de informações como o Coordenador do Curso.
 * Possui e-mail, senha e nome, mas não precisa de matrícula.</p>
 * <p>Só pode haver um Coordenador em um mesmo objeto {@link CentralDeInformacoes}.</p>
 */

@Entity
@DiscriminatorValue("C")
public class Coordenador extends Usuario {

    public Coordenador(String email, String senha, String nome) {
        super(email, senha, nome);
    }

    protected Coordenador() {
        // Construtor para JPA
    }

}
