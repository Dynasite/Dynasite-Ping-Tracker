package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.page.Page;
import org.dynasite.page.ResourcePage;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

public class ResourceServer extends Server {

    private static final Logger LOG = LogManager.getLogger();

    private final String resourceLocation;

    public ResourceServer(String resourceLocation) {
        this.resourceLocation = Objects.requireNonNull(resourceLocation);
    }

    @Override
    public Page servePage(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        String resourcePath = this.resourceLocation + uri;
        LOG.trace("Attempting to serve resource @ " + resourcePath);

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        InputStream is = classloader.getResourceAsStream(resourcePath);

        if(isAvailable(is)) {
            LOG.trace("Serving resource: " + resourcePath);
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
            return false;
        }
    }
}
