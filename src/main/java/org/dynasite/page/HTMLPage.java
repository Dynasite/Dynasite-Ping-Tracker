package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

public class HTMLPage extends Page {

    // Factory Methods

    public static HTMLPage getFromFile(File file) throws IOException {
        return new HTMLPage(new String(Files.readAllBytes(file.toPath())));
    }

    public static HTMLPage getFromResource(String path) throws IOException {
        return new HTMLPage(new String(Objects.requireNonNull(HTMLPage.class.getResourceAsStream(path)).readAllBytes()));
    }

    // Class

    protected String html;

    public HTMLPage(String html) {
        this.html = Objects.requireNonNull(html);
    }

    @Override
    public Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        return getHTMLResponse(html);
    }

    protected Response getHTMLResponse(String html) {
        return NanoHTTPD.newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, html);
    }
}
