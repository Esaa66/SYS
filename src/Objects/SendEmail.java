package Objects;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

    public static void send(String recipient, String subject, String message) {

        final String username = "ahmadissa66666@gmail.com";
        final String password = "1234567e..";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message emailMessage = new MimeMessage(session);
            emailMessage.setFrom(new InternetAddress("youremail@example.com"));
            emailMessage.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient)
            );
            emailMessage.setSubject(subject);
            emailMessage.setText(message);

            Transport.send(emailMessage);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

