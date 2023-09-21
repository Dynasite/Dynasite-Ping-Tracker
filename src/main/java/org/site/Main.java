package org.site;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.Dynasite;
import org.dynasite.page.HTMLPageTemplate;
import org.dynasite.server.HostServer;
import org.dynasite.server.StaticServer;
import org.dynasite.server.URLMap;

import java.util.HashMap;

/**
 * Main class template which hosts a very simple basic site.
 */
public class Main {

    /**
     * Utility logger.
     */
    private static final Logger LOG = LogManager.getLogger();

    private static final String NAME = "Hello World, From Dynasite!";

    private static final String PARAGRAPH
            = "Simple Dynasite template. Delete or modify the code to suit your needs.";

    /**
     * The server implementation which directs urls to pages. In this case, directs
     * blank urls to the main page.
     */
    private static final StaticServer server = new StaticServer(new HashMap<>() {
        {
            put("/", new MainPage());
        }
    });

    public static void main(String[] args) {
        LOG.info(NAME);

        Dynasite dynasite = new Dynasite(new HostServer(80, server, URLMap.EMPTY_URL_MAP));
        dynasite.start();

        LOG.info("Website started! At: " + dynasite.getHostURL());
    }

    /**
     * Simple template web page (/website/HelloWorld.html)
     */
    private static class MainPage extends HTMLPageTemplate {

        public MainPage() {
            super(loadFromResource("/website/HelloWorld.html"));
        }

        /**
         * Replaces the tokens in the HTML with the variables in this class.
         */
        @Override
        protected String formatResponse(String html, NanoHTTPD.IHTTPSession session) {
            return html.replace(token("WEBSITE-NAME"), NAME).replace(token("PARAGRAPH"), PARAGRAPH);
        }
    }
}
