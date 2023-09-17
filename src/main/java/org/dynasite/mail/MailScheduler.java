package org.dynasite.mail;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.mail.internet.InternetAddress;


@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class MailScheduler implements Job {

    private static final SchedulerFactory SCHEDULER_FACTORY
            = new StdSchedulerFactory();

    private static final Scheduler scheduler;

    static {
        try {
            scheduler = SCHEDULER_FACTORY.getScheduler();
        } catch (SchedulerException e) {
            //Likely to be fatal. Log & rethrow as a runtime exception.
            System.out.println("Possible fatal error" + e);
            throw new RuntimeException(e);
        }
    }


    //-------------------------------------------------------------

    private final String name;

    private final MailingList recipients;

    private final MailBuilder builder;

    private boolean enabled;

    public MailScheduler(String name, CronScheduleBuilder schedule, MailingList recipients, MailBuilder mailBuilder)
            throws SchedulerException {
        this.name = name;
        this.recipients = recipients;
        this.builder = mailBuilder;

        @SuppressWarnings("rawtypes")
        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(name).withSchedule(schedule);

        scheduler.scheduleJob(JobBuilder.newJob(this.getClass()).withIdentity(name).build(), triggerBuilder.build());
    }


    //-------------------------------------------------------------

    public void enable(){
        this.enabled = true;
    }

    public void disable(){
        this.enabled = false;
    }

    public boolean isEnabled(){
        return this.enabled;
    }

    public void forceFire(){

    }

    //-------------------------------------------------------------

    @Override
    @SuppressWarnings("RedundantThrows")
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }

    //-------------------------------------------------------------

    public interface MailBuilder {

        void prep();

        String getTitle(InternetAddress address);

        String getSubject(InternetAddress address);

        String getContent(InternetAddress address);

        String getDescription(InternetAddress address);
    }

}
