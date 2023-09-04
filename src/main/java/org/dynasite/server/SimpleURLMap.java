package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;

import java.util.Map;
import java.util.Objects;

/**
 * A simple implementation of a {@link URLMap}, which
 * remaps URLS given a {@link java.util.HashMap} or
 * other kind of {@link Map}.
 */
public class SimpleURLMap extends URLMap {

    private final Map<String, String> map;

    /**
     * Creates a new Simple URL Map, with a {@link Map} as input.
     * The Map allows converting URL keys to URL values.
     *
     * @param map the map of which URLS should be converted.
     */
    public SimpleURLMap(Map<String, String> map) {
        this.map = Objects.requireNonNull(map);
    }

    @Override
    public String remapURI(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        if(map.containsKey(uri)) {
            return map.get(uri);
        }

        return uri;
    }
}
