package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;

import java.util.Map;

public abstract class URLMap {

    public abstract String remapURI(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session);

}
