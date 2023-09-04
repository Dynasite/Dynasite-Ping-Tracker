package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;
import org.dynasite.page.ErrorPage;
import org.dynasite.page.Page;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HostServer extends NanoHTTPD {

    private final Server server;

    private final URLMap urlMap;

    private int port;

    public HostServer(int port, Server server) {
        this(port, server, new SimpleURLMap(new HashMap<>(){{
            this.put("/", "/index.html");
        }}));

        this.port = port;
    }

    public HostServer(int port, Server server, URLMap urlMap) {
        super(port);
        this.server = Objects.requireNonNull(server);
        this.urlMap = Objects.requireNonNull(urlMap);
        this.port = port;
    }

    public NanoHTTPD.Response _serve(NanoHTTPD.IHTTPSession session) {
        String uri = urlMap.remapURI(session.getUri(), session.getHeaders(), session);
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
