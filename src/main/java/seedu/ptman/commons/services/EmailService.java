package seedu.ptman.commons.services;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * A email services that sends email to user
 */
public class EmailService {
    private static EmailService singleInstance = new EmailService();

    private final String senderEmail = "ptmanager.reset@gmail.com";
    private final String password = "DEFAULT!1";


    private Session session;


    private EmailService() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, password);
                    }
                });
    }

    public static synchronized EmailService getInstance() {
        return singleInstance;
    }

    /**
     * Send a standard reset password message over to user
     * @param name
     * @param email
     * @param newPassword
     * @throws MessagingException
     */
    public void sendResetPasswordMessage(String name, String email, String newPassword) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Password Reset");
        message.setText("Hi " + name + ", \n" + "Your password has been reset: " + newPassword + "\n\n"
                + "Please reset your password immediately in PTMan");
        Transport.send(message);
    }

}
