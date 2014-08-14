package com.softserveinc.ita.kaiji.model.util.email;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import java.util.Properties;

@Component
public class MailSender {

    private static final Logger LOG = Logger.getLogger(MailSender.class);

    @Autowired
    private PropertyFileReader propertyFileReader;

    private String filePath;

    /**
     * Sends message with specified subject and text to recipient
     *
     * @param recipientMail  email of recipient person
     * @param messageSubject email subject
     * @param messageText   email text
     */
    public void send(String recipientMail, String messageSubject, String messageText) {

        if ("".equals(recipientMail)) {
            LOG.error("Empty recipient email address ");
            return;
        }

        Properties mailProperties = propertyFileReader.readMailProperty(filePath);
        if(mailProperties == null){
            LOG.error("Error while reading email properties");
            return;
        }

        String from = mailProperties.getProperty("email.address");
        final String username = mailProperties.getProperty("email.address");
        final String password = mailProperties.getProperty("email.password");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", mailProperties.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", mailProperties.getProperty("mail.smtp.port"));

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientMail));
            message.setSubject(messageSubject);
            message.setText(messageText);
            Transport.send(message, message.getAllRecipients());
            LOG.trace("Sent message successfully....");

        } catch (MessagingException e) {
            LOG.error("Can't sent message to recipient " + recipientMail + " " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
