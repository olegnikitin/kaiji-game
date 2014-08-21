package com.softserveinc.ita.kaiji.model.util.email;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
    Environment env;

    /**
     * Sends message with specified subject and text to recipient
     *
     * @param recipientMail  email of recipient person
     * @param messageSubject   email subject
     * @param userNickName   user nickname
     * @param userPassword   user password
     * @return  true if message was successfully send, otherwise - false
     */
    public boolean send(String recipientMail, String messageSubject, String userNickName, String userPassword) {

        if ("".equals(recipientMail)) {
            LOG.error("Empty recipient email address ");
            return false;
        }

        String from = env.getProperty("email.address");
        final String username = env.getProperty("email.address");
        final String password = env.getProperty("email.password");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", env.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", env.getProperty("mail.smtp.port"));

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
            message.setText(env.getProperty("message.body")+ "Nickname: "+userNickName+"\nPassword: "+userPassword);
            Transport.send(message, message.getAllRecipients());
            LOG.trace("Sent message successfully....");

        } catch (MessagingException e) {
            LOG.error("Can't sent message to recipient " + recipientMail + " " + e.getMessage());
            return false;
        }
        return true;
    }

}
