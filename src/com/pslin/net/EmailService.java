package com.pslin.net;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * @author plin
 */
public class EmailService {

    private static final String HOST = "smtp.gmail.com";
    private static final String USER = "FROM_EMAIL";
    private static final String PASSWORD = "PASSWORD";
    private static final String NAME = "FROM_NAME";
    private static final int PORT = 465;

    public void sendSimpleEmail(String recipient, String subject, String body) {
        Email email = createEmail();
        try {
            email.setFrom(USER, NAME);
            email.addTo(recipient);
            email.setSubject(subject);
            email.setMsg(body);
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    private Email createEmail() {
        Email email = new SimpleEmail();
        email.setHostName(HOST);
        email.setAuthenticator(new DefaultAuthenticator(USER, PASSWORD));
        email.setSmtpPort(PORT);
        email.setSSLOnConnect(true);
        return email;
    }
}
