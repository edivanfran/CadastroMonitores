package br.com.monitoria;

import br.com.monitoria.model.Coordenador;
import br.com.monitoria.model.EditalDeMonitoria;

import javax.swing.*;
import java.util.List;

/**
 * Classe responsável por inicializar a interface gráfica.
 * Verifica se há coordenador cadastrado e direciona para a tela apropriada.
 */
public class InicializadorGUI {

    public static void iniciar() {
        // Define o look and feel do sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Se não conseguir, usa o padrão
        }

        GerenciadorDeDados gerenciadorDeDados = GerenciadorDeDados.getInstancia();
        List<Coordenador> coordenadores = gerenciadorDeDados.getTodosOsCoordenadores();

        // Verifica se a lista de coordenadores não está vazia
        boolean temCoordenador = (coordenadores != null && !coordenadores.isEmpty());

        // Verifica se há coordenador cadastrado
        if (!temCoordenador) {
            // Se não houver coordenador, abre tela de cadastro
            SwingUtilities.invokeLater(() -> {
                TelaCadastroCoordenador telaCadastro = new TelaCadastroCoordenador();
                telaCadastro.inicializar();
            });
        } else {
            // Se houver coordenador, abre tela de login
            SwingUtilities.invokeLater(() -> {
                TelaLogin telaLogin = new TelaLogin();
                telaLogin.inicializar();
            });
        }
    }
}
