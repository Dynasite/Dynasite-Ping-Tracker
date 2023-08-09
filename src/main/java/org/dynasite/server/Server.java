package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.page.Page;

import java.util.Map;

/**
 * Represents a Page Server, which is responsible for serving
 * the correct {@link Page} when presented with a URI or URI,
 * and not a general server as such.
 */
public abstract class Server {

    private static final Logger LOG = LogManager.getLogger();

    public static final Server PAGE_NOT_FOUND_SERVER = new Server() {
        @Override
        public Page servePage(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
            LOG.info("Serving page at: " + uri);
            return Page.PAGE_NOT_FOUND;
        }

        @Override
        public void handleServerError(Exception error) {
            LOG.error("Page Not Found Server error: " + error);
        }
    };

    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> headers = session.getHeaders();

        return this.servePage(uri, headers, session).getPageResponse(headers, session);
    }

    public abstract Page servePage(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session);

    public abstract void handleServerError(Exception error);
}
