package org.dynasite;

import fi.iki.elonen.NanoHTTPD;
import org.dynasite.server.Server;

import java.io.IOException;
import java.util.Objects;

/**
 * Main class for hosting a site.
 */
public class Dynasite {

    public static String host = "http://localhost";

    public static int timeout = 1000; //Default 1 second timeout

    private final int port;

    private final Server server;

    private final NanoServer nanoServer;

    public Dynasite(int port, Server server) {
        this.port = port;
        this.server = Objects.requireNonNull(server);
        this.nanoServer = new NanoServer();
    }

    @SuppressWarnings("unused")
    public int getPort() {
        return port;
    }

    @SuppressWarnings("unused")
    public Server getServer() {
        return server;
    }

    public String getHostURL() {
        return host + ":" + this.port;
    }

    public void start() {
        try {
            nanoServer.start(timeout, false);
        } catch (IOException e) {
            server.handleServerError(e, null, null);
        }
    }

    private class NanoServer extends NanoHTTPD {

        public NanoServer() {
            super(Dynasite.this.port);
        }

        @Override
        public Response serve(IHTTPSession session) {
            return Dynasite.this.server.serve(session);
        }
    }
}
