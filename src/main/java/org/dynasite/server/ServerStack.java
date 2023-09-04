package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.page.Page;

import java.util.*;

/**
 * A {@link Server} implementation which allows combining a list
 * of other {@link Server Servers} into one. The first Server in the
 * list to have a valid response {@link Page} will be responsible
 * for serving said Page, otherwise the next Server in the list
 * will be tried and so on and so forth. Any errors raised by a
 * Server impl are handled by the same Server impl.
 */
public class ServerStack extends Server {

    private static final Logger LOG = LogManager.getLogger();

    private final List<Server> servers = new ArrayList<>();

    /**
     * Creates a new Server Stack from a list of {@link Server Servers}.
     *
     * @param servers the list of Server implementations to combine into one.
     */
    public ServerStack(Server... servers) {
        this.servers.addAll(Arrays.asList(servers));
    }

    @Override
    @SuppressWarnings("RedundantThrows")
    public Page servePage(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session) throws Exception {
        Page page = getFirstPageOrNull(uri, session);

        if(page == null)
            page = Page.default404Page;

        return page;
    }

    public Page getFirstPageOrNull(String uri, NanoHTTPD.IHTTPSession session) {
        Page page = null;

        for(Server server : servers) {
            page = tryGetPageFromServer(server, uri, session);

            if(page != null)
                break;
        }

        return page;
    }

    public Page tryGetPageFromServer(Server server, String uri, NanoHTTPD.IHTTPSession session) {
        try {
            return server.servePage(uri, session.getHeaders(), session);
        } catch (Exception e) {
            LOG.error("Error thrown attempting to get page from server: " + server + " with uri: " + uri);
            server.handleServerError(e, uri, session);
            return null;
        }
    }

    /**
     * LIKELY UNUSED. Any errors raised by a Server impl are handled by the same Server impl.
     */
    @Override
    public Page handleServerError(Exception error, String URI, NanoHTTPD.IHTTPSession session) {
        return super.handleServerError(error, URI, session);
    }
}
