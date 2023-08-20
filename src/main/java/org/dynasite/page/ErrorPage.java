package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;

import java.util.Map;
import java.util.Objects;

public class ErrorPage extends Page {

    private final Exception error;

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
