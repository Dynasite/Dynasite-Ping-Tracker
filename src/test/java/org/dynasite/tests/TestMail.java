package org.dynasite.tests;

import org.dynasite.mail.Mailer;
import org.dynasite.mail.MessageBuilder;
import org.junit.jupiter.api.Test;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.Properties;

public class TestMail {

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(new FileReader(new File("C:\\Dev\\Projects\\Dynasite\\secrets.properties")));
        } catch (IOException e) {
            System.err.println("Failed to read secrets.properties");
        }
    }

    private static final String ORIGIN_EMAIL = properties.getProperty("email");

    private static final char[] ORIGIN_PASSWORD = properties.getProperty("pass").toCharArray();

    private static final String DESTINATION_EMAIL = properties.getProperty("destination_email");

    @Test
    public void testSendMail() throws MessagingException {
        Mailer mailer = new Mailer(new PasswordAuthentication(
                ORIGIN_EMAIL, ORIGIN_PASSWORD
        ));

        MessageBuilder covidUpdateMessageTemplate = mailer.newMessageBuilder()
                .setSenderName("MAIL TEST")
                .setSubjectText("TEST MESSAGE")
                .setText("Test Message.")
                .setDescription("A Test Message.");

        covidUpdateMessageTemplate.send(new InternetAddress(DESTINATION_EMAIL));
    }

}
