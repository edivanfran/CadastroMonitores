import java.util.Properties;
import jakarta.mail.Session;
import jakarta.mail.Message;
import jakarta.mail.Transport;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.PasswordAuthentication;

//TODO| documentar essa classe (não sei como funciona)
public class Mensageiro {

    private static final boolean MOCK = Boolean.parseBoolean(System.getProperty("mensageiro.mock", "false"));

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SENDER_EMAIL = "testeprojetoemail31@gmail.com";
    private static final String SENDER_PASSWORD = "wqse lmze pahb eksw";

    public static void enviarEmail(String destinatario, String assunto, String mensagem) {

        if (MOCK) {
            System.out.println("=== Mensageiro (MOCK) ===");
            System.out.println("Para: " + destinatario);
            System.out.println("Assunto: " + assunto);
            System.out.println("Mensagem:\n" + mensagem);
            System.out.println("=========================");
            return;
        }

        if (SENDER_EMAIL == null || SENDER_PASSWORD == null) { //TODO| aparentemente essa condição é sempre falsa
            System.err.println("Mensageiro: variáveis de ambiente SENDER_EMAIL ou SENDER_PASSWORD não configuradas.");
            return;
        }

        String host = (SMTP_HOST != null) ? SMTP_HOST : "smtp.gmail.com"; //TODO| aparentemente essas duas são sempre verdadeiras
        String port = (SMTP_PORT != null) ? SMTP_PORT : "587";

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");       // ativa STARTTLS
        props.put("mail.smtp.starttls.required", "true");     // exige upgrade para TLS
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");      // força TLSv1.2
        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

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
            e.printStackTrace(); //TODO| não é uma boa prática
        }
    }
}
