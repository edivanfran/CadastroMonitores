package br.com.monitoria;

public interface Notificador {
    void enviarEmail(String destinatario, String assunto, String mensagem);
}
