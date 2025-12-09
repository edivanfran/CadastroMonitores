package br.com.monitoria.interfaces;

/**
 * Interface para o padrão Observer.
 * Define um método de atualização que será chamado pelo sujeito (Subject)
 * quando seu estado mudar.
 */
public interface Observador {

    /**
     * Método chamado pelo sujeito para notificar o observador de uma mudança.
     */
    void atualizar();

}
