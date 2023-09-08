package org.dynasite.mail;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.PasswordAuthentication;
import java.util.Calendar;
import java.util.Objects;
import java.util.Properties;

@SuppressWarnings("unused")
public final class Mailer {

    private static final Properties properties = System.getProperties();

    static {
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
    }

    //---------------------------------------------------

    private final Session activeSession;

    private final InternetAddress emailAddress;

    public Mailer(PasswordAuthentication loginDetails){
        //Set our (the MailSender) email address.
        try {
            emailAddress = new InternetAddress(loginDetails.getUserName());
        } catch (AddressException e) {
            throw new IllegalArgumentException("Login email/username is invalid", e);
        }

        //Create mail session api for the given login details
        activeSession = Session.getInstance(properties, new PasswordAuthenticationBridge(){
            @Override
            protected PasswordAuthentication getAuthentication(){
                return loginDetails;
            }
        });

        activeSession.setDebug(true);

        System.out.println("$Mail Session Started: " + emailAddress.getAddress());
    }

    //---------------------------------------------------

    public void setDebugPrinting(boolean debug){
        activeSession.setDebug(debug);
    }

    public MimeMessage newMessage() throws MessagingException {
        MimeMessage newMessage = new MimeMessage(activeSession);

        newMessage.setSender(emailAddress);
        newMessage.setFrom(emailAddress);

        return newMessage;
    }

    @SuppressWarnings("RedundantThrows")
    public MessageBuilder newMessageBuilder() throws MessagingException {
        return new MessageBuilder(this);
    }

    public void send(MessageBuilder configuredBuilder, Address to) throws MessagingException {
        MimeMessage newMessage = newMessage();
        configuredBuilder.build(newMessage);
        send(newMessage, to);
    }

    public void send(MimeMessage mimeMessage, Address to) throws MessagingException {
        Calendar cal = Calendar.getInstance();
        prepMessage(mimeMessage, Objects.requireNonNull(to));
        Transport.send(mimeMessage, new Address[]{to});

        System.out.println(
                "$Mail sent [" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE)
                        + ":" + cal.get(Calendar.SECOND) + "] "
                        + emailAddress.getAddress() + " -> " + to
                        + " (" + mimeMessage.getHeader("Internal-Tracker-Number", null) + ")"
        );
    }

    //---------------------------------------------------

    private static void prepMessage(MimeMessage message, Address to)
            throws MessagingException {
        message.setHeader("Internal-Tracking-Number",
                "@" + message.toString().split("@")[1]
        );

        message.setRecipients(Message.RecipientType.TO, (String) null);
        message.setRecipients(Message.RecipientType.CC, (String) null);
        message.setRecipients(Message.RecipientType.BCC, (String) null);
        message.setRecipient(Message.RecipientType.TO, to);

        message.setFrom(message.getSender());
    }

    private static abstract class PasswordAuthenticationBridge extends javax.mail.Authenticator {

        @Override
        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
            PasswordAuthentication loginDetails = getAuthentication();
            return new javax.mail.PasswordAuthentication(
                    loginDetails.getUserName(), String.valueOf(loginDetails.getPassword())
            );
        }

        protected abstract PasswordAuthentication getAuthentication();
    }
}
