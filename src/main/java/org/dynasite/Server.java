package org.dynasite;

import fi.iki.elonen.NanoHTTPD;

import java.util.Map;

/**
 * Represents a Page Server, which is responsible for serving
 * the correct {@link Page} when presented with a URI or URI,
 * and not a general server as such.
 */
public abstract class Server {

    public static final Server PAGE_NOT_FOUND_SERVER = new Server() {
        @Override
        public Page servePage(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
            return Page.PAGE_NOT_FOUND;
        }

        @Override
        public void handleServerError(Exception error) {
            System.err.println("Server error: " + error);
        }
    };

    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> headers = session.getHeaders();

        return this.servePage(uri, headers, session).getPageResponse(headers, session);
    }

    public abstract Page servePage(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session);

    public abstract void handleServerError(Exception error);
}
