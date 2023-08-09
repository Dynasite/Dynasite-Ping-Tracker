package org.dynasite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class Main {

    private static final Logger LOG = LogManager.getLogger();

    public static void main(String[] args) {
        LOG.info("Starting Dynasite with arguments: " + Arrays.toString(args));
        Dynasite dynasite = new Dynasite(80, Server.PAGE_NOT_FOUND_SERVER);
        dynasite.start();
        LOG.info("Site started at: " + dynasite.getHostURL());
    }

}
