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
