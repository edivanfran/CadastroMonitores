package br.com.monitoria;

public class NotificadorFactory {

    private static Notificador instancia;

    public static synchronized Notificador obterInstancia() {
        if (instancia == null) {
            boolean usarMock = Boolean.parseBoolean(System.getProperty("mensageiro.mock", "false"));
            if (usarMock) {
                instancia = new NotificadorMock();
            } else {
                instancia = new NotificadorEmailSmtp();
            }
        }
        return instancia;
    }

    public static synchronized void setInstancia(Notificador novoNotificador) {
        instancia = novoNotificador;
    }
}
