package br.com.monitoria;

public class NotificadorMock implements Notificador {

    @Override
    public void enviarEmail(String destinatario, String assunto, String mensagem) {
        System.out.println("=== Notificador (MOCK SIMULADO) ===");
        System.out.println("Para: " + destinatario);
        System.out.println("Assunto: " + assunto);
        System.out.println("Mensagem:\n" + mensagem);
        System.out.println("===================================");
    }
}
