import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mensageiro {

    private static final boolean MOCK = Boolean.parseBoolean(System.getProperty("mensageiro.mock", "false"));

    private static final String SMTP_HOST = System.getenv("SMTP_HOST");      
    private static final String SMTP_PORT = System.getenv("SMTP_PORT");      
    private static final String SENDER_EMAIL = System.getenv("SENDER_EMAIL");
    private static final String SENDER_PASSWORD = System.getenv("SENDER_PASSWORD");

    public static void enviarEmail(String destinatario, String mensagem) {
        String assunto = "Confirmação de inscrição";

        if (MOCK) {
            System.out.println("=== Mensageiro (MOCK) ===");
            System.out.println("Para: " + destinatario);
            System.out.println("Assunto: " + assunto);
            System.out.println("Mensagem:\n" + mensagem);
            System.out.println("=========================");
            return;
        }

        if (SENDER_EMAIL == null || SENDER_PASSWORD == null) {
            System.err.println("Mensageiro: variáveis de ambiente SENDER_EMAIL ou SENDER_PASSWORD não configuradas.");
            return;
        }

        String host = (SMTP_HOST != null) ? SMTP_HOST : "smtp.gmail.com";
        String port = (SMTP_PORT != null) ? SMTP_PORT : "587";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
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
            e.printStackTrace();
        }
    }
}
