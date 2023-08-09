package org.dynasite;

import java.util.Objects;

/**
 * Main class for hosting a site.
 */
public class Dynasite {

    private final int port;

    private final Server server;

    public Dynasite(int port, Server server) {
        this.port = port;
        this.server = Objects.requireNonNull(server);
    }

    public int getPort() {
        return port;
    }

    public Server getServer() {
        return server;
    }

}
