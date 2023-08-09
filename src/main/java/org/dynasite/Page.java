package org.dynasite;

import fi.iki.elonen.NanoHTTPD;

import java.net.URI;

/**
 * Basic implementable representation of a webpage or other web content
 * which is served to a client upon request, usually at specific urls.
 * Simply put, a Page returns the content (e.g. html page or js file)
 * given to client web browsers.
 */
public abstract class Page {

    private final URI uri;

    protected Page(URI uri) {
        this.uri = uri;
    }

    public NanoHTTPD.Response getPageResponse() {
        return NanoHTTPD.newFixedLengthResponse(
                NanoHTTPD.Response.Status.OK, NanoHTTPD.MIME_HTML, "<h1>Default Dynasite Page</h1>"
        );
    }

    public URI getUri() {
        return uri;
    }
}
