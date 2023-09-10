package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

public class JSPage extends Page {

    // Factory Methods

    public static JSPage getFromFile(File file) throws IOException {
        return new JSPage(new String(Files.readAllBytes(file.toPath())));
    }

    public static JSPage getFromResource(String path) throws IOException {
        return new JSPage(new String(Objects.requireNonNull(JSPage.class.getResourceAsStream(path)).readAllBytes()));
    }

    // Class

    public static final String MIME_JS = "text/javascript";

    protected String js;

    public JSPage(String js) {
        this.js = Objects.requireNonNull(js);
    }

    @Override
    public Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        return getJSResponse(js);
    }

    protected Response getJSResponse(String js) {
        return NanoHTTPD.newFixedLengthResponse(Response.Status.OK, MIME_JS, js);
    }
}
