package br.com.monitoria;

import java.util.Properties;
import jakarta.mail.Session;
import jakarta.mail.Message;
import jakarta.mail.Transport;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.PasswordAuthentication;

public class NotificadorEmailSmtp implements Notificador {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SENDER_EMAIL = "testeprojetoemail31@gmail.com";
    private static final String SENDER_PASSWORD = "wqse lmze pahb eksw";

    @Override
    public void enviarEmail(String destinatario, String assunto, String mensagem) {
        if (SENDER_EMAIL == null || SENDER_PASSWORD == null) {
            System.err.println("NotificadorEmailSmtp: variáveis de ambiente SENDER_EMAIL ou SENDER_PASSWORD não configuradas.");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");       // ativa STARTTLS
        props.put("mail.smtp.starttls.required", "true");     // exige upgrade para TLS
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");      // força TLSv1.2
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(SENDER_EMAIL));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario, false));
            msg.setSubject(assunto);
            msg.setText(mensagem);
            Transport.send(msg);
            System.out.println("E-mail enviado para " + destinatario);
        } catch (MessagingException e) {
            System.err.println("Falha ao enviar e-mail: " + e.getMessage());
        }
    }
}
