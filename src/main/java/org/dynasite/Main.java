package org.dynasite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.page.ErrorPage;
import org.dynasite.server.*;

import java.util.Arrays;
import java.util.HashMap;

public class Main {

    private static final Logger LOG = LogManager.getLogger();

    public static void main(String[] args) {
        LOG.info("Starting Dynasite with arguments: " + Arrays.toString(args));

        Server server = new ServerStack(
                new ResourceServer("static/WebServerFiles"),
                new StaticServer(new HashMap<>(){{
                    put("/hello", new ErrorPage(new RuntimeException("Fuck You!")));
                }})
        );

        Dynasite dynasite = new Dynasite(new HostServer(80, server));
        dynasite.start();

        LOG.info("Site started at: " + dynasite.getHostURL());
    }

}
