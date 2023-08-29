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

    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> headers = session.getHeaders();

        Page page;
        try {
            page = this.servePage(uri, headers, session);
        } catch (Exception exception) {
            Page errorPage = this.handleServerError(exception, uri, session);
            errorPage = Objects.requireNonNullElse(errorPage, new ErrorPage(exception));
            return errorPage.getPageResponse(headers, session);
        }

        return Objects.requireNonNullElse(page, Page.default404Page).getPageResponse(headers, session);
    }

    public abstract Page servePage(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session) throws Exception;

    public Page handleServerError(Exception error, String URI, NanoHTTPD.IHTTPSession session) {
        LOG.error("Internal server error encountered serving page, with uri: " + URI, error);
        return new ErrorPage(error);
    }
}
