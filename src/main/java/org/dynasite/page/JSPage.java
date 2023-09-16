package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

/**
 * A basic js response {@link Page}, which can be created
 * by either a {@link JSPage#getFromFile(File) File},
 * {@link JSPage#getFromResource(String) Resource}, or
 * {@link JSPage#JSPage(String) js String}, and
 * served by a {@link org.dynasite.server.Server Server
 * implementation}.
 */
public class JSPage extends Page {

    // Factory Methods

    /**
     * Simple factory method to create a {@link JSPage}
     * from a js file on disk.
     *
     * @param file the js file.
     * @return a {@link JSPage} with the content of the file.
     * @throws IOException if the file cannot be found or read.
     */
    public static JSPage getFromFile(File file) throws IOException {
        return new JSPage(new String(Files.readAllBytes(file.toPath())));
    }

    /**
     * Simple factory method to create a {@link JSPage}
     * from a js file resource (with class loader) in a/the jar.
     *
     * @param path the path to the js file resource (e.g. /path/file.txt)
     * @return a {@link JSPage} with the content of the file.
     * @throws IOException if the resource cannot be found or read.
     */
    public static JSPage getFromResource(String path) throws IOException {
        return new JSPage(new String(Objects.requireNonNull(JSPage.class.getResourceAsStream(path)).readAllBytes()));
    }

    // Class

    /**
     * Mime type for js files.
     */
    public static final String MIME_JS = "text/javascript";

    protected String js;

    /**
     * Creates a new {@link JSPage} from a String
     * of js text.
     *
     * @param js the js text.
     */
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
