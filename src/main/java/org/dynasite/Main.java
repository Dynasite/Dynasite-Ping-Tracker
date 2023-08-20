package org.dynasite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.server.FileServer;

import java.io.File;
import java.util.Arrays;

public class Main {

    private static final Logger LOG = LogManager.getLogger();

    public static void main(String[] args) {
        LOG.info("Starting Dynasite with arguments: " + Arrays.toString(args));
        Dynasite dynasite = new Dynasite(80, new FileServer(new File("C:/Users/lukem/Desktop/WebServerFiles")));
        dynasite.start();
        LOG.info("Site started at: " + dynasite.getHostURL());
    }

}
