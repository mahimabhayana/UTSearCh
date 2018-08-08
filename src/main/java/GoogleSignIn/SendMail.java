package GoogleSignIn;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendMail {

    public static void send(String toEmail, String name, String token) {
        final String username = "UTSearCh.BOT@gmail.com";
        final String password = "8d8d4d1d";
        final String verifyUrl = "http://localhost:8081/verify?token=";

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
            message.setSubject("UTSearCh Confirmation Email");
            message.setText("Dear " + name + ","
                    + "\n\n Please go to the following link to verify your email\n\n" + verifyUrl + token);

            Transport.send(message);

            System.out.println("Sent Email");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
