package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.page.HTMLPageTemplate;
import org.dynasite.page.Page;
import org.dynasite.page.ResourcePage;
import org.jetbrains.annotations.Nullable;
import org.pingtracker.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

/**
 * A {@link FileServer}, but for files contained within the jar.
 */
public class ResourceServer extends Server {

    private static final Logger LOG = LogManager.getLogger();

    private final String resourceLocation;

    /**
     * Constructs a new server to serve the resources at the given
     * location.
     *
     * @param resourceLocation the location where the resources are
     *                        stored (e.g. /path/folder)
     */
    public ResourceServer(String resourceLocation) {
        this.resourceLocation = Objects.requireNonNull(resourceLocation);
    }

    @Override
    public Page servePage(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        String resourcePath = this.resourceLocation + uri;
        LOG.debug("Attempting to serve resource @ " + resourcePath);

        InputStream is = this.getClass().getResourceAsStream(resourcePath);

        if(isAvailable(is)) {
            LOG.debug("Serving resource: " + resourcePath);
            return new ResourcePage(is, resourcePath);
        } else {
            LOG.warn("Cannot find resource: " + resourcePath);
            return null;
        }

    }

    private static boolean isAvailable(@Nullable InputStream is) {
        if(is == null)
            return false;

        try {
            int a = is.available();
            return a > 0;
        } catch (IOException e) {
            LOG.debug("IOException checking resource for resource server:", e);
            return false;
        }
    }
}
