package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

public class ResourcePage extends Page {

    private static final Logger LOG = LogManager.getLogger();

    private final InputStream inputStream;

    private final String resourcePath;

    public ResourcePage(InputStream inputStream, String resourcePath) {
        this.inputStream = Objects.requireNonNull(inputStream);
        this.resourcePath = Objects.requireNonNull(resourcePath);
    }

    @Override
    public NanoHTTPD.Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        try {
            String mimeType = NanoHTTPD.getMimeTypeForFile(resourcePath);

            LOG.info("Serving page " + resourcePath + " with mime: " + mimeType);

            return NanoHTTPD.newFixedLengthResponse(
                    NanoHTTPD.Response.Status.OK, mimeType,
                    inputStream, inputStream.available()
            );

        } catch (IOException e) {
            return new ErrorPage(e).getPageResponse(headers, session);
        }
    }
}
