package com.softserveinc.ita.kaiji.model.util.email;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

/**
 * @author Sydorenko Oleksandra
 * @version 1.0
 * @since 21.08.14
 */

@Component
public class MailSender {

    private static final Logger LOG = Logger.getLogger(MailSender.class);

    @Autowired
    Environment env;

    @Autowired
    JavaMailSenderImpl javaMailSender;

    /**
     * Sends message with specified subject and text to recipient
     *
     * @param to           email of recipient person
     * @param subject      email subject
     * @param userNickname user nickname
     * @param userPassword user password
     * @return true if message was successfully send, otherwise - false
     */
    public boolean send(String to, String subject, String userNickname, String userPassword) {

        if ("".equals(to)) {
            LOG.error("Empty recipient email address ");
            return false;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(env.getProperty("message.body") + "Nickname: " + userNickname + "\nPassword: " + userPassword);
        javaMailSender.send(message);
        LOG.trace("Sent message successfully....");
        return true;
    }

}
