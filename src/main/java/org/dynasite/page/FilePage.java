package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

public class FilePage extends Page {

    private final Object actualResource;

    public boolean cache = false;

    public FilePage(File file) {
        super(file.toURI());
        this.actualResource = file;
    }

    public FilePage(URL localResource) throws URISyntaxException {
        super(localResource.toURI());
        this.actualResource = localResource;
    }

    @Override
    public NanoHTTPD.Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        return null;
    }
}
