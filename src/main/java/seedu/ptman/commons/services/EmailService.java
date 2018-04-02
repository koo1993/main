package seedu.ptman.commons.services;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


//@@author koo1993
/**
 * A email services that sends email to user
 */
public class EmailService {
    private static EmailService singleInstance = new EmailService();

    private final String senderEmailReset = "ptmanager.reset@gmail.com";
    private final String senderEmailTimetable = "ptmanager.timetable@gmail.com";
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
                        return new PasswordAuthentication(senderEmailReset, password);
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
        message.setFrom(new InternetAddress(senderEmailReset));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("[PTMan] Password Reset");
        message.setText("Hi " + name + ", \n\n" + "Your password has been reset: " + newPassword + "\n\n"
                + "Please reset your password immediately in PTMan.\n\nBest Regards,\nThe PTMan Team");
        Transport.send(message);
    }
    //@@author hzxcaryn
    /**
     * Send exported timetable image as an attachment to user
     * @param email
     * @param filename
     * @throws MessagingException
     */
    public void sendTimetableAttachment(String email, String filename) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmailTimetable));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("[PTMan] My Timetable");

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText("Dear Valued PTMan user,\n\nAttached is your exported timetable.\n"
                + "Thank you for using PTMan and have a nice day!\n\nBest Regards,\nThe PTMan Team");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);

        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);

        Transport.send(message);
    }

}
