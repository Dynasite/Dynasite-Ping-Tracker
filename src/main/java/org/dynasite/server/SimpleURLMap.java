package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;

import java.util.Map;
import java.util.Objects;

public class SimpleURLMap extends URLMap {

    private final Map<String, String> map;

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
