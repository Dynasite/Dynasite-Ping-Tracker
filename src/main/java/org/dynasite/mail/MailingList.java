package org.dynasite.mail;

import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused")
public class MailingList {

    private final List<InternetAddress> recipients = new ArrayList<>();

    public MailingList(){ }

    public void addRecipient(InternetAddress address){
        recipients.add(address);
    }

    public boolean removeRecipient(InternetAddress address){
        return recipients.remove(address);
    }

    public Iterator<InternetAddress> getRecipientsIterator(){
        return recipients.iterator();
    }

    public Iterable<InternetAddress> getRecipientsIterable(){
        return recipients;
    }

}
