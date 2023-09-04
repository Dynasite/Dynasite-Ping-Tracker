package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;
import org.dynasite.Dynasite;
import org.dynasite.page.ErrorPage;
import org.dynasite.page.Page;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The main host server, which takes a port, a top level
 * {@link Server} implementation, and an optional {@link
 * URLMap}. The {@link HostServer} instance is then given
 * to a {@link org.dynasite.Dynasite} instance and
 * {@link Dynasite#start() started}.
 */
public class HostServer extends NanoHTTPD {

    private final Server server;

    private final URLMap urlMap;

    private int port;

    /**
     * Creates a new host server on the given port and hosts
     * the given top level server. A default {@link URLMap}
     * is implied, which just maps a blank URI to an index.html
     * page.
     *
     * @param port the port to host on.
     * @param server the top level {@link Server} implementation.
     */
    public HostServer(int port, Server server) {
        this(port, server, new SimpleURLMap(new HashMap<>(){{
            this.put("/", "/index.html");
        }}));

        this.port = port;
    }

    /**
     * Creates a new host server on the given port and hosts
     * the given top level server. A {@link URLMap} which
     * maps one URL to another can also be provided.
     *
     * @param port the port to host on.
     * @param server the top level {@link Server} implementation.
     * @param urlMap map of urls to other urls.
     */
    public HostServer(int port, Server server, URLMap urlMap) {
        super(port);
        this.server = Objects.requireNonNull(server);
        this.urlMap = Objects.requireNonNull(urlMap);
        this.port = port;
    }

    private NanoHTTPD.Response _serve(NanoHTTPD.IHTTPSession session) {
        String uri = urlMap.remapURI(session.getUri(), session.getHeaders(), session);

        if(uri == null)
            uri = session.getUri();

        Map<String, String> headers = session.getHeaders();

        Page page;
        try {
            page = this.getServer().servePage(uri, headers, session);
        } catch (Exception exception) {
            Page errorPage = this.getServer().handleServerError(exception, uri, session);
            errorPage = Objects.requireNonNullElse(errorPage, new ErrorPage(exception));
            return errorPage.getPageResponse(headers, session);
        }

        return Objects.requireNonNullElse(page, Page.default404Page).getPageResponse(headers, session);
    }

    @Override
    public Response serve(IHTTPSession session) {
        return this._serve(session);
    }

    public Server getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

}
