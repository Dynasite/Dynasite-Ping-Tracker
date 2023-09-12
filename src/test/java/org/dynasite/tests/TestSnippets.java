package org.dynasite.tests;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.Dynasite;
import org.dynasite.page.HTMLPage;
import org.dynasite.page.HTMLPageTemplate;
import org.dynasite.page.Page;
import org.dynasite.page.Snippet;
import org.dynasite.server.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestSnippets {

    private static final Logger LOG = LogManager.getLogger();

    private Dynasite dynasite;

    @Test
    public void testSnippetNotFound() {
        try {
            new Snippet("test-crash").loadFromResource("nonexistent-resource.txt");
        } catch (Snippet.SnippetLoadException e) {
            LOG.error("Crash test PASS!", e);
            return;
        }
        LOG.info("Crash test FAIL!");
    }

    @Test
    public void testSnippets() throws IOException {

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
                                put("/", new SnippetTemplatePage());
                            }
                        }
                ),
                stopServer
        );

        dynasite = new Dynasite(new HostServer(80, server, URLMap.EMPTY_URL_MAP));
        dynasite.start();

        LOG.info("Site started at: " + dynasite.getHostURL());
        LOG.info("Goto: " + dynasite.getHostURL() + "/stop-test" + " to pass the test");

        while(dynasite.isAlive());
    }

    private static class SnippetTemplatePage extends HTMLPageTemplate {

        private static final Snippet HEAD = new Snippet("head")
                .loadFromResource("/test-sites/snippets/TestSnippetHeaders.html");

        private static final Snippet BODY = new Snippet("body")
                .loadFromResource("/test-sites/snippets/TestSnippetBodys.html");

        public SnippetTemplatePage() {
            super(loadFromResource("/test-sites/html-pages/TestSnippetsTemplate.html"));
        }

        @Override
        protected String formatResponse(String html, NanoHTTPD.IHTTPSession session) {
            html = html.replace(token(HEAD.getName()), HEAD.get())
                    .replace(token(BODY.getName()), BODY.get());

            html = html.replace(token("title"), "Snippets Test")
                    .replace(token("icon"), "https://avatars.githubusercontent.com/u/23332887?v=4")
                    .replace(token("image"), "https://www.w3schools.com/html/img_chania.jpg");

            return html;
        }
    }
}
