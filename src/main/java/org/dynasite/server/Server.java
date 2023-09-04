package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.page.ErrorPage;
import org.dynasite.page.Page;

import java.util.Map;

/**
 * Represents a Page Server, which is responsible for serving
 * the correct {@link Page} when presented with a URI or URI,
 * and not a general server as such.
 */
public abstract class Server {

    private static final Logger LOG = LogManager.getLogger();

    public abstract Page servePage(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session)
            throws Exception;

    public Page handleServerError(Exception error, String URI, NanoHTTPD.IHTTPSession session) {
        LOG.error("Internal server error encountered serving page, with uri: " + URI, error);
        return new ErrorPage(error);
    }
}
