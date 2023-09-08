package org.dynasite.mail;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("unused")
public class MessageBuilder {

    private final Mailer issuer;

    private String sender;

    private String subject;

    private String content;

    private String text;

    private String description;

    //---------------------------------------------

    public MessageBuilder setSenderName(String senderName) {
        this.sender = senderName;
        return this;
    }

    public MessageBuilder setSubjectText(String subjectText) {
        this.subject = subjectText;
        return this;
    }

    public MessageBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public MessageBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public MessageBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public void send(Address to) throws MessagingException {
        issuer.send(this, to);
    }

    //---------------------------------------------

    MessageBuilder(Mailer issuer) {
        this.issuer = issuer;
        this.sender = null;
        this.subject = null;
        this.content = null;
        this.text = null;
        this.description = null;
    }

    void build(MimeMessage message) throws MessagingException {
        if(sender != null) try {
            message.setSender(new InternetAddress(message.getSender().toString(), sender));
        } catch (UnsupportedEncodingException e) {
            throw new MessagingException("Sender name is invalid and could not be encoded", e);
        }

        if(subject != null)
            message.setSubject(subject, StandardCharsets.UTF_8.name());

        if(content != null)
            message.setContent(content, "text/html");
        else if (text != null)
            message.setText(text);

        if(description != null)
            message.setDescription(description);
    }
}
