package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;

import java.util.Map;
import java.util.Objects;

/**
 * Basic HTML page to format and display
 * Java {@link Exception}s wrapped by this
 * class.
 */
public class ErrorPage extends Page {

    private final Exception error;

    /**
     * Creates a new HTML error page from the
     * provided {@link Exception}, with details
     * about the error.
     *
     * @param error exception which caused the error
     *              and holds the error details.
     */
    public ErrorPage(Exception error) {
        this.error = Objects.requireNonNull(error);
    }

    @Override
    public NanoHTTPD.Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        return NanoHTTPD.newFixedLengthResponse(
                NanoHTTPD.Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_HTML,
                "<h1>Internal Server Error!</h1> <p>" + error.getClass() + "<br>" + error.getMessage()
        );
    }

}
