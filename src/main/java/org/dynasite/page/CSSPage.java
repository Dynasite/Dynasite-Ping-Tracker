package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

public class CSSPage extends Page {

    // Factory Methods

    public static CSSPage getFromFile(File file) throws IOException {
        return new CSSPage(new String(Files.readAllBytes(file.toPath())));
    }

    public static CSSPage getFromResource(String path) throws IOException {
        return new CSSPage(new String(Objects.requireNonNull(CSSPage.class.getResourceAsStream(path)).readAllBytes()));
    }

    // Class

    public static final String MIME_CSS = "text/css";

    protected String css;

    public CSSPage(String css) {
        this.css = Objects.requireNonNull(css);
    }

    @Override
    public Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        return getCSSResponse(css);
    }

    protected Response getCSSResponse(String css) {
        return NanoHTTPD.newFixedLengthResponse(Response.Status.OK, MIME_CSS, css);
    }
}
