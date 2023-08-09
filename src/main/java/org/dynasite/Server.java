package org.dynasite;

import fi.iki.elonen.NanoHTTPD;

import java.util.Map;

/**
 * Represents a Page Server, which is responsible for serving
 * the correct {@link Page} when presented with a URI or URI,
 * and not a general server as such.
 */
public abstract class Server {

    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> headers = session.getHeaders();

        return this.servePage(uri, headers, session).getPageResponse(headers, session);
    }

    public abstract Page servePage(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session);

}
