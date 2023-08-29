package org.dynasite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.server.FileServer;
import org.dynasite.server.Server;
import org.dynasite.server.ServerStack;

import java.io.File;
import java.util.Arrays;

public class Main {

    private static final Logger LOG = LogManager.getLogger();

    public static void main(String[] args) {
        LOG.info("Starting Dynasite with arguments: " + Arrays.toString(args));

        Server server = new ServerStack(
                new FileServer(new File("C:/Users/lukem/Desktop/WebServerFiles")),
                new FileServer(new File("C:/Users/lukem/Desktop/WebServerFiles2"))
        );

        Dynasite dynasite = new Dynasite(80, server);
        dynasite.start();

        LOG.info("Site started at: " + dynasite.getHostURL());
    }

}
