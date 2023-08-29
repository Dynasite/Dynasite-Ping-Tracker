package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a webpage for a local file, or more simply:
 * allows turning any local {@link File} into a {@link Page} which
 * can be served by either a {@link org.dynasite.server.FileServer}
 * or any other {@link org.dynasite.server.Server}.
 */
public class FilePage extends Page {

    private static final Logger LOG = LogManager.getLogger();

    private final File file;

    /**
     * Creates a new webpage from a given {@link File}
     * for serving the file over the web.
     *
     * @param file the given {@link File} to serve as a webpage.
     */
    public FilePage(File file) {
        this.file = Objects.requireNonNull(file);
    }

    @Override
    public NanoHTTPD.Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        if(!file.exists()) {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, NanoHTTPD.MIME_HTML,
                    "<h1>File Not Found!</h1>" + "<p>" + file.getAbsolutePath()
            );
        }

        try {
            FileInputStream inputStream = new FileInputStream(file);

            String mimeType = NanoHTTPD.getMimeTypeForFile(file.toURI().toString());

            LOG.info("Serving page " + file.getAbsolutePath() + " with mime: " + mimeType);

            return NanoHTTPD.newFixedLengthResponse(
                    NanoHTTPD.Response.Status.OK, mimeType,
                    inputStream, file.length()
            );

        } catch (IOException e) {
            return new ErrorPage(e).getPageResponse(headers, session);
        }
    }
}
