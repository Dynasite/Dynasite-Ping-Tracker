package org.dynasite.server;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynasite.page.Page;

import java.io.File;
import java.util.Map;
import java.util.Objects;

public class ExternalFileServer extends Server {

    private static final Logger LOG = LogManager.getLogger();

    private final File folder;

    public ExternalFileServer(File folder) {
        if(!Objects.requireNonNull(folder).isDirectory()) {
            throw new IllegalArgumentException("Expected a folder");
        }

        this.folder = folder;
    }

    @Override
    public Page servePage(String uri, Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        String requestedFilePath = folder.getAbsolutePath() + uri;
        LOG.info("Attempting to serve file @ " + requestedFilePath);

        File requestedFile = new File(requestedFilePath);
        if(requestedFile.exists() && requestedFile.isFile()) {
            LOG.info("Found file: " + requestedFile.getAbsolutePath());

        } else {
            LOG.warn("Couldn't find file: " + requestedFile.getAbsolutePath());
            return null;
        }

        return null;
    }

    @Override
    public void handleServerError(Exception error) {
        LOG.fatal("Error starting External File Server.", error);
    }
}
