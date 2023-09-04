package org.dynasite;

import org.dynasite.server.HostServer;
import org.dynasite.server.Server;

import java.io.IOException;
import java.util.Objects;

/**
 * Main class for hosting a site.
 */
public class Dynasite {

    public static String hostname = "http://localhost";

    public static int timeout = 1000; //Default 1 second timeout

    private final int port;

    private final Server server;

    private final HostServer host;

    public Dynasite(HostServer host) {
        this.port = host.getPort();
        this.host = Objects.requireNonNull(host);
        this.server = Objects.requireNonNull(host.getServer());

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
        return hostname + ":" + this.port;
    }

    public void start() {
        try {
            host.start(timeout, false);
        } catch (IOException e) {
            server.handleServerError(e, null, null);
        }
    }

}
