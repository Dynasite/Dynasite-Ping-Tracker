package org.pingtracker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.Dynasite;
import org.dynasite.server.*;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.*;

public class Main {

    /**
     * Utility logger.
     */
    private static final Logger LOG = LogManager.getLogger();

    /**
     * A list of IP PING_RECORDS to ping, along with a text description of them.
     */
    private static final List<PingRecord> PING_RECORDS = new ArrayList<>(18);

    static {
        PING_RECORDS.add(new PingRecord("GOOGLE", "8.8.8.8"));
        PING_RECORDS.add(new PingRecord("CLOUD FLAIR", "1.1.1.1"));

        PING_RECORDS.add(new PingRecord("DURBAN", "41.23.216.150"));
        PING_RECORDS.add(new PingRecord("CAPE TOWN", "102.216.72.15"));
        PING_RECORDS.add(new PingRecord("JOHANNESBURG", "41.76.103.34"));

        PING_RECORDS.add(new PingRecord("BERLIN", "89.246.171.50"));
        PING_RECORDS.add(new PingRecord("PARIS", "87.231.109.145"));
        PING_RECORDS.add(new PingRecord("LONDON", "192.221.154.0"));
        PING_RECORDS.add(new PingRecord("MUNICH", "82.135.31.211"));

        PING_RECORDS.add(new PingRecord("BRAZIL", "132.255.73.248"));

        PING_RECORDS.add(new PingRecord("CHICAGO", "50.229.170.233"));
        PING_RECORDS.add(new PingRecord("TORONTO", "205.171.3.26"));

        PING_RECORDS.add(new PingRecord("LOCAL IP (1)", "154.66.152.86"));
        PING_RECORDS.add(new PingRecord("EUROPE IP (1)", "149.5.38.1"));
        PING_RECORDS.add(new PingRecord("EUROPE IP (2)", "185.58.18.1"));
        PING_RECORDS.add(new PingRecord("NORTH AMERICA (1)", "45.33.2.79"));
    }

    private static final HomePage homePage = new HomePage(PING_RECORDS);

    private static final Server server = new ServerStack(
            new StaticServer(new HashMap<>(){{
                put("/", homePage);
            }}), new ResourceServer("/assets")
    );

    public static void main(String[] args) {
        LOG.info("Starting Ping Tracker...");

        Dynasite dynasite = new Dynasite(new HostServer(80, server));
        dynasite.start();

        LOG.info("Hosting on: " + dynasite.getHostURL());
    }


}
