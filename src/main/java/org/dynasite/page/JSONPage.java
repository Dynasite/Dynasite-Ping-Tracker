package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

/**
 * A basic json response {@link Page}, which can be created
 * by either a {@link JSONPage#getFromFile(File) File},
 * {@link JSONPage#getFromResource(String) Resource}, or
 * {@link JSONPage#JSONPage(String) json String}, and
 * served by a {@link org.dynasite.server.Server Server
 * implementation}.
 */
public class JSONPage extends Page {

    // Factory Methods

    /**
     * Simple factory method to create a {@link JSONPage}
     * from a json file on disk.
     *
     * @param file the json file.
     * @return a {@link JSONPage} with the content of the file.
     * @throws IOException if the file cannot be found or read.
     */
    public static JSONPage getFromFile(File file) throws IOException {
        return new JSONPage(new String(Files.readAllBytes(file.toPath())));
    }

    /**
     * Simple factory method to create a {@link JSONPage}
     * from a json file resource (with class loader) in a/the jar.
     *
     * @param path the path to the json file resource (e.g. /path/file.txt)
     * @return a {@link JSONPage} with the content of the file.
     * @throws IOException if the resource cannot be found or read.
     */
    public static JSONPage getFromResource(String path) throws IOException {
        return new JSONPage(new String(Objects.requireNonNull(JSONPage.class.getResourceAsStream(path)).readAllBytes()));
    }

    // Class

    /**
     * Mime type for json files.
     */
    public static final String MIME_JSON = "application/json";

    protected String json;

    /**
     * Creates a new {@link JSONPage} from a String
     * of json text.
     *
     * @param json the json text.
     */
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
