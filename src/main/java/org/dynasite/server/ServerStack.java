package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.page.Page;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ServerStack extends Server {

    private static final Logger LOG = LogManager.getLogger();

    private final List<Server> servers = new ArrayList<>();

    public ServerStack(Server... servers) {
        this.servers.addAll(Arrays.asList(servers));
    }

    @Override
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

    @Override
    public Page handleServerError(Exception error, String URI, NanoHTTPD.IHTTPSession session) {
        return super.handleServerError(error, URI, session);
    }
}
