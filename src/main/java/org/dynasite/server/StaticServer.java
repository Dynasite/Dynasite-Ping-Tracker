package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;
import org.dynasite.page.Page;

import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Server} that serves {@link Page Pages}
 * from a Map of URLS.
 */
public class StaticServer extends Server {

    private final Map<String, Page> pages;

    /**
     * Creates a new Server that will serve the Pages
     * mapped to specific URLs.
     *
     * @param pages Map of URLs and Pages.
     */
    public StaticServer(Map<String, Page> pages) {
        this.pages = Objects.requireNonNull(pages);
    }

    @Override
    public Page servePage(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        if(pages.containsKey(uri)) {
            return pages.get(uri);
        }

        return null;
    }
}
