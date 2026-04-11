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
    
    /**
     * Inicializa a interface gráfica do sistema.
     * @param central A central de informações
     * @param persistencia O objeto de persistência
     * @param nomeArquivo O nome do arquivo de persistência
     */
    public static void iniciar(CentralDeInformacoes central, Persistencia persistencia, String nomeArquivo) {
        // Define o look and feel do sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Se não conseguir, usa o padrão
        }

        GerenciadorDeDados gerenciadorDeDados = GerenciadorDeDados.getInstancia();
        List<Coordenador> coordenadores = gerenciadorDeDados.getTodosOsCoordenadores();

        Boolean temCoordenador;

        if (coordenadores != null) {
            temCoordenador = Boolean.TRUE;
        } else {
            temCoordenador = Boolean.FALSE;
        }


        // Verifica se há coordenador cadastrado
        if (!temCoordenador) {
            // Se não houver coordenador, abre tela de cadastro
            SwingUtilities.invokeLater(() -> {
                TelaCadastroCoordenador telaCadastro = new TelaCadastroCoordenador(central, persistencia, nomeArquivo);
                telaCadastro.inicializar();
            });
        } else {
            // Se houver coordenador, abre tela de login
            SwingUtilities.invokeLater(() -> {
                TelaLogin telaLogin = new TelaLogin(central, persistencia, nomeArquivo);
                telaLogin.inicializar();
            });
        }
    }
}

