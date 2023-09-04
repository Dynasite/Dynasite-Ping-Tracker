package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;

import java.net.URI;
import java.util.Map;

/**
 * <p>Basic implementable representation of a webpage or other web content
 * which is served to a client upon request, usually at specific urls.
 * Simply put, a Page returns the content (e.g. html page or js file)
 * given to client web browsers.</p>
 *
 * <p>See {@link org.dynasite.server.Server servers} for the mechanism on
 * serving specific pages.</p>
 *
 * @see fi.iki.elonen.NanoHTTPD.Response for returning Responses from pages.
 */
public abstract class Page {

    /**
     * Default {@link Page} displayed when a {@link org.dynasite.server.Server}
     * cannot find another Page to display. Reassign this variable to change
     * the default 404 page.
     */
    public static Page default404Page = new Page() {

        @Override
        public NanoHTTPD.Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
            return NanoHTTPD.newFixedLengthResponse(
                    NanoHTTPD.Response.Status.NOT_FOUND, NanoHTTPD.MIME_HTML, "<h1>Page Not Found!</h1>"
            );
        }

    };

    /**
     * The url of this page (if it has one).
     * Currently unused.
     */
    private final URI uri;

    /**
     * Creates a new Page with no URI.
     */
    protected Page() {
        this.uri = null;
    }

    /**
     * Creates a new Page with the given URI.
     *
     * @param uri the URI of the page.
     */
    @SuppressWarnings("unused")
    protected Page(URI uri) {
        this.uri = uri;
    }

    /**
     * Called to get the {@link NanoHTTPD.Response} of this page.
     * Usually, a {@link Page} simply wraps a Response, and this
     * method is where the Response is obtained from the Page.
     *
     * <p>
     *     This is where a Page type should handle its response
     *     logic and return its response.  So, for example, an
     *     HTMLPage should return its HTML Response here.
     * </p>
     *
     * @param headers the sessions headers.
     * @param session the session object containing details of the session.
     * @return the {@link fi.iki.elonen.NanoHTTPD.Response} for this Page.
     */
    public abstract NanoHTTPD.Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session);

    /**
     * @return The url of this page (if it has one).
     * Currently unused.
     */
    @SuppressWarnings("unused")
    public URI getUri() {
        return uri;
    }
}
