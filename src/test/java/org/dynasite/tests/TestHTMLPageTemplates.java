package org.dynasite.tests;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.Dynasite;
import org.dynasite.page.HTMLPageTemplate;
import org.dynasite.page.Page;
import org.dynasite.server.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TestHTMLPageTemplates {

    private static final Logger LOG = LogManager.getLogger();

    private Dynasite dynasite;

    @Test
    public void testHTMLPageTemplate() {
        Server stopServer = new Server() {
            @Override
            public Page servePage(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
                if(uri.equals("/stop-test")) {
                    dynasite.stop();
                    return new Page() {
                        @Override
                        public NanoHTTPD.Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
                            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, NanoHTTPD.MIME_HTML, "<h1>Stopping!</h1>");
                        }
                    };
                }

                return null;
            }
        };

        Server server = new ServerStack(
                new StaticServer(
                        new HashMap<>() {
                            {
                                put("/", new Template());
                            }
                        }
                ),
                stopServer
        );

        dynasite = new Dynasite(new HostServer(80, server, new SimpleURLMap(new HashMap<>())));
        dynasite.start();

        LOG.info("Site started at: " + dynasite.getHostURL());
        LOG.info("Goto: " + dynasite.getHostURL() + "/stop-test" + " to pass the test");

        while(dynasite.isAlive());
    }

    private static class Template extends HTMLPageTemplate {

        private static int serves = 1;

        public Template() {
            super(loadFromResource("/test-sites/html-pages/TestTemplate.html"));
        }

        @Override
        protected String formatResponse(String html, NanoHTTPD.IHTTPSession session) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            return html.replace("{#IP}", session.getRemoteIpAddress())
                    .replace("{#SERVES}", ("" + serves++))
                    .replace("{#TIME}", dtf.format(now));
        }

        @Override
        protected String setupResponse(String html) {
            return html.replace("{#TITLE}", "HTML Page Template Test");
        }
    }
}
