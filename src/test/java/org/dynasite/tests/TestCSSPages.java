package org.dynasite.tests;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.Dynasite;
import org.dynasite.page.HTMLPage;
import org.dynasite.page.CSSPage;
import org.dynasite.page.Page;
import org.dynasite.server.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestCSSPages {

    private static final Logger LOG = LogManager.getLogger();

    private Dynasite dynasite;

    @Test
    public void testCSSPages() throws IOException {
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
                                //put("/hello1", HTMLPage.getFromFile(new File("C:/Users/lukem/Desktop/HelloWorld.html")));
                                put("/", HTMLPage.getFromResource("/test-sites/html-pages/HelloWorld.html"));
                                put("/CSS", CSSPage.getFromResource("/test-sites/css-pages/test-css.css"));
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
}
