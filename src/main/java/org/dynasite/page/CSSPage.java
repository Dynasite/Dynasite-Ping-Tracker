package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

/**
 * A basic CSS response {@link Page}, which can be created
 * by either a {@link CSSPage#getFromFile(File) File},
 * {@link CSSPage#getFromResource(String) Resource}, or
 * {@link CSSPage#CSSPage(String) CSS String}, and
 * served by a {@link org.dynasite.server.Server Server
 * implementation}.
 */
public class CSSPage extends Page {

    // Factory Methods

    /**
     * Simple factory method to create a {@link CSSPage}
     * from a css file on disk.
     *
     * @param file the css file.
     * @return a {@link CSSPage} with the content of the file.
     * @throws IOException if the file cannot be found or read.
     */
    public static CSSPage getFromFile(File file) throws IOException {
        return new CSSPage(new String(Files.readAllBytes(file.toPath())));
    }

    /**
     * Simple factory method to create a {@link CSSPage}
     * from a css file resource (with class loader) in a/the jar.
     *
     * @param path the path to the css file resource (e.g. /path/file.txt)
     * @return a {@link CSSPage} with the content of the file.
     * @throws IOException if the resource cannot be found or read.
     */
    public static CSSPage getFromResource(String path) throws IOException {
        return new CSSPage(new String(Objects.requireNonNull(CSSPage.class.getResourceAsStream(path)).readAllBytes()));
    }

    // Class

    /**
     * Mime type for css files.
     */
    public static final String MIME_CSS = "text/css";

    protected String css;

    /**
     * Creates a new {@link CSSPage} from a String
     * of CSS text.
     *
     * @param css the css text.
     */
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
