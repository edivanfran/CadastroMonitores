package br.com.monitoria;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import javax.swing.JFrame;
// Supondo que você tenha uma tela com este nome
// import br.com.monitoria.view.TelaCadastroEdital; 

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

    @Override
    public void abrirTelaCadastroEdital(JFrame telaPai) {
        // O Coordenador PODE. Então ele abre a tela.
        // new TelaCadastroEdital(telaPai).setVisible(true);
        System.out.println("LOG: Coordenador abrindo tela de cadastro de edital."); // Placeholder
    }

}
