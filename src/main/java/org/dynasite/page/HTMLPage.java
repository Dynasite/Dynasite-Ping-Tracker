package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

/**
 * A basic HTML response {@link Page}, which can be created
 * by either a {@link HTMLPage#getFromFile(File) File},
 * {@link HTMLPage#getFromResource(String) Resource}, or
 * {@link HTMLPage#HTMLPage(String) HTML String}, and
 * served by a {@link org.dynasite.server.Server Server
 * implementation}.
 */
public class HTMLPage extends Page {

    // Factory Methods

    /**
     * Simple factory method to create an {@link HTMLPage}
     * from an HTML file on disk.
     *
     * @param file the html file.
     * @return an {@link HTMLPage} with the content of the file.
     * @throws IOException if the file cannot be found or read.
     */
    public static HTMLPage getFromFile(File file) throws IOException {
        return new HTMLPage(new String(Files.readAllBytes(file.toPath())));
    }

    /**
     * Simple factory method to create a {@link HTMLPage}
     * from an HTML file resource (with class loader) in a/the jar.
     *
     * @param path the path to the HTML file resource (e.g. /path/file.txt)
     * @return a {@link HTMLPage} with the content of the file.
     * @throws IOException if the resource cannot be found or read.
     */
    public static HTMLPage getFromResource(String path) throws IOException {
        return new HTMLPage(new String(Objects.requireNonNull(HTMLPage.class.getResourceAsStream(path)).readAllBytes()));
    }

    // Class

    protected String js;

    /**
     * Creates a new {@link HTMLPage} from a String
     * of HTML text.
     *
     * @param html the HTML text.
     */
    public HTMLPage(String html) {
        this.js = Objects.requireNonNull(html);
    }

    @Override
    public Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        return getHTMLResponse(js);
    }

    protected Response getHTMLResponse(String html) {
        return NanoHTTPD.newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, html);
    }
}
