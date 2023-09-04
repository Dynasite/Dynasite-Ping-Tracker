package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.page.ErrorPage;
import org.dynasite.page.Page;

import java.util.Map;

/**
 * <p>Represents a Page Server, which is responsible for serving
 * the correct {@link Page} when presented with a URI or URI,
 * and not a general server as such.</p>
 *
 * <p>Servers are responsible for accepting a String URI as input,
 * and serving a response {@link Page}, or {@code null} if they
 * have no page to serve. A single server is passed to a {@link
 * HostServer} which hosts the server. Servers can be stacked
 * together using a {@link ServerStack}. Additionally, Servers
 * are also responsible for handling errors, which return an
 * {@link ErrorPage} by default.</p>
 */
public abstract class Server {

    private static final Logger LOG = LogManager.getLogger();

    /**
     * Default, overridable method for returning response {@link Page pages}
     * for a given {@code uri}. This is where a Server implementation should
     * handle the logic for displaying the correct Page for a given URI. The
     * Page itself will then handle the response.
     *
     * @param uri the browsers requested uri.
     * @param headers the headers in the uri request.
     * @param session the session object and session information.
     * @return a response {@link Page}, or {@code null} if the server has no page for the given URI.
     * The {@link HostServer} will handle 404 pages.
     * @throws Exception if any errors are raised which prevent the server from serving its page.
     */
    public abstract Page servePage(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session)
            throws Exception;

    /**
     * Called when a/the server encounters an error serving a page.
     *
     * <p>This is where a server implementation should catch & handle
     * errors, as well as return an error page. The server SHOULD NOT raise
     * any errors when handling errors. </p>
     *
     * @param error the {@link Exception} which caused the error.
     * @param URI the uri the browser requested when the error was raised.
     * @param session the session object in use when the error was raised.
     * @return an {@link ErrorPage} or some other kind of {@link Page}.
     */
    public Page handleServerError(Exception error, String URI, NanoHTTPD.IHTTPSession session) {
        LOG.error("Internal server error encountered serving page, with uri: " + URI, error);
        return new ErrorPage(error);
    }

}
