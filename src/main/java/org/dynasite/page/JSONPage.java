package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

public class JSONPage extends Page {

    // Factory Methods

    public static JSONPage getFromFile(File file) throws IOException {
        return new JSONPage(new String(Files.readAllBytes(file.toPath())));
    }

    public static JSONPage getFromResource(String path) throws IOException {
        return new JSONPage(new String(Objects.requireNonNull(JSONPage.class.getResourceAsStream(path)).readAllBytes()));
    }

    // Class

    public static final String MIME_JSON = "application/json";

    protected String json;

    public JSONPage(String json) {
        this.json = Objects.requireNonNull(json);
    }

    @Override
    public Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        return getJSONResponse(json);
    }

    protected Response getJSONResponse(String json) {
        return NanoHTTPD.newFixedLengthResponse(Response.Status.OK, MIME_JSON, json);
    }
}
