package br.com.monitoria;

import br.com.monitoria.interfaces.Observador;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Singleton responsável por gerenciar o padrão Observer.
 * As telas (Observadores) se registram aqui para serem notificadas sobre
 * mudanças nos dados da aplicação, permitindo que a UI seja atualizada.
 */
public class GerenciadorDeEventos {

    private static GerenciadorDeEventos instancia;
    private final List<Observador> observadores = new ArrayList<>();

    private GerenciadorDeEventos() {
    }

    public static synchronized GerenciadorDeEventos getInstancia() {
        if (instancia == null) {
            instancia = new GerenciadorDeEventos();
        }
        return instancia;
    }

    /**
     * Adiciona um observador à lista para ser notificado sobre atualizações.
     * 
     * @param observador A tela ou componente que implementa a interface Observador.
     */
    public void adicionarObservador(Observador observador) {
        this.observadores.add(observador);
    }

    public void removerObservador(Observador observador) {
        this.observadores.remove(observador);
    }

    /**
     * Notifica todos os observadores registrados que uma mudança nos dados ocorreu.
     */
    public void notificarAtualizacao() {
        for (Observador observador : observadores) {
            observador.atualizar();
        }
    }
}