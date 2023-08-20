package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.page.ErrorPage;
import org.dynasite.page.Page;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a Page Server, which is responsible for serving
 * the correct {@link Page} when presented with a URI or URI,
 * and not a general server as such.
 */
public abstract class Server {

    private static final Logger LOG = LogManager.getLogger();

    @SuppressWarnings("unused")
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

        Page page;
        try {
            page = this.servePage(uri, headers, session);
        } catch (Exception e) {
            LOG.error("Internal server error encountered serving page.", e);
            return new ErrorPage(e).getPageResponse(headers, session);
        }

        return Objects.requireNonNullElse(page, Page.PAGE_NOT_FOUND).getPageResponse(headers, session);
    }

    public abstract Page servePage(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session) throws Exception;

    public abstract void handleServerError(Exception error);
}
