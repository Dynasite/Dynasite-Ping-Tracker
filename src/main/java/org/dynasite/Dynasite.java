package org.dynasite;

import org.dynasite.server.HostServer;
import org.dynasite.server.Server;

import java.io.IOException;
import java.util.Objects;

/**
 * Main class for hosting a site. See tests for simple examples
 * on the types of sites which can be created.
 */
public class Dynasite {

    /**
     * Default hostname the site is hosted on. Can be changed.
     */
    public static String hostname = "http://localhost";

    /**
     * Default response timeout (1 second). Can be changed.
     */
    public static int timeout = 1000; //Default 1 second timeout

    private final int port;

    private final Server server;

    private final HostServer host;

    /**
     * Create a new Dynasite to host the given {@link HostServer}.
     *
     * @param host server to host.
     */
    public Dynasite(HostServer host) {
        this.port = host.getPort();
        this.host = Objects.requireNonNull(host);
        this.server = Objects.requireNonNull(host.getServer());

    }

    /**
     * @return the port the site is being hosted on.
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the server implementation being hosted.
     */
    public Server getServer() {
        return server;
    }

    /**
     * @return the url where the site is being served.
     */
    public String getHostURL() {
        return hostname + ":" + this.port;
    }

    /**
     * Starts and hosts the server as in a separate non-daemon thread.
     */
    public void start() {
        try {
            host.start(timeout, false);
        } catch (IOException e) {
            server.handleServerError(e, null, null);
        }
    }

    public void stop() {
        host.stop();
    }

    public boolean isAlive() {
        return host.isAlive();
    }

}
