package br.com.monitoria.interfaces;

/**
 * Define o contrato para objetos que podem ser observados.
 * Um objeto observável mantém uma lista de observadores e os notifica sobre mudanças de estado.
 */
public interface Observavel {
    void adicionarObservador(Observador observador);
    void removerObservador(Observador observador);
    void notificarObservadores();
}