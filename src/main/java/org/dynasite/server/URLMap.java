package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;

import java.util.Map;

/**
 * An implementable class which allows remapping different URIs,
 * allowing an input URI to be changed to an output URI given
 * different rules & requirements. A good example would be
 * changing a blank URI (a lone "/") to point to "/index.html".
 */
public abstract class URLMap {

    /**
     * A {@link URLMap} implementation which does nothing (i.e. remaps NO URLS).
     */
    public static final URLMap EMPTY_URL_MAP = new URLMap() {
        @Override
        public String remapURI(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
            return uri;
        }
    };

    /**
     * A simple {@link URLMap} which maps requests to a blank uri (/)
     * to "index.html".
     */
    public static final URLMap INDEX_PAGE_MAP = new URLMap() {
        @Override
        public String remapURI(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
            if(uri.equals("/"))
                return "/index.html";
            else return uri;
        }
    };

    /**
     * Allows changing an input {@code uri} to another
     * URI, or leaving it unchanged.
     *
     * @param uri the input uri
     * @param headers the session headers
     * @param session the session object.
     * @return the new remapped URI. {@code null}s are allowed, which
     * indicate the url should not be changed.
     */
    public abstract String remapURI(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session);

}
