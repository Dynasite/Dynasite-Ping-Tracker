package org.dynasite.tests;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.Dynasite;
import org.dynasite.page.JSONPage;
import org.dynasite.page.Page;
import org.dynasite.server.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestJSONPages {

    private static final Logger LOG = LogManager.getLogger();

    private Dynasite dynasite;

    @Test
    @SuppressWarnings({"StatementWithEmptyBody"})
    public void testWebServerSimpWatch() throws IOException {
        Dynasite.hostname = "http://ham1.duckdns.org";
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
                stopServer,
                new ResourceServer("test-sites/simpwatch"),
                new StaticServer(
                        new HashMap<>(){{
                            put("/simps/simp.json", JSONPage.getFromResource("/test-sites/json-pages/test-simp.json"));
                            put("/simps/test.json", new JSONPage("{\n" +
                                    "  \"description\": \"TEST SIMP. \",\n" +
                                    "  \"simping-spree\": true,\n" +
                                    "\n" +
                                    "  \"grading\": {\n" +
                                    "    \"grade\": \"A+++\",\n" +
                                    "    \"comment\": \"Test Simp.\"\n" +
                                    "  },\n" +
                                    "\n" +
                                    "  \"activity\": {\n" +
                                    "    \"current\": 0.0,\n" +
                                    "    \"previous\": 0.0\n" +
                                    "  },\n" +
                                    "\n" +
                                    "  \"history\": {\n" +
                                    "    \"height\": 300,\n" +
                                    "    \"graph\": {\n" +
                                    "      \"2021\": 200.0\n" +
                                    "    }\n" +
                                    "  }\n" +
                                    "}\n"));
                        }}
                )
        );

        dynasite = new Dynasite(new HostServer(80, server));
        dynasite.start();

        LOG.info("Site started at: " + dynasite.getHostURL());
        LOG.info("Goto: " + dynasite.getHostURL() + "/stop-test" + " to pass the test");

        while(dynasite.isAlive());
    }
}
