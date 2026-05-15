package br.com.monitoria.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import javax.swing.*;
// Supondo que você tenha uma tela com este nome
// import br.com.monitoria.view.TelaCadastroEdital; 

/**
 * <p>Representa o usuário atribuído na central de informações como o Coordenador do Curso.
 * Possui e-mail, senha e nome, mas não precisa de matrícula.</p>
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
