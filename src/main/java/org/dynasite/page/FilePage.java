package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public class FilePage extends Page {

    private final Object actualResource;

    private boolean cache = false;

    private String cachedPage = null;

    public FilePage(File file) {
        super(file.toURI());
        this.actualResource = file;
    }

    public FilePage(URL localResource) throws URISyntaxException {
        super(localResource.toURI());
        this.actualResource = localResource;
    }

    private Object readResource() {
        if(cache && (cachedPage != null)) {
            return cachedPage;
        } else if (actualResource instanceof File) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader((File) actualResource));
                StringBuilder content = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                if(cache) {
                    this.cachedPage = content.toString();
                }

                return content.toString();
            } catch (Exception e) {
                return e;
            }
        } else if (actualResource instanceof URL) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(((URL) actualResource).openStream());
                BufferedReader reader = new BufferedReader(inputStreamReader);

                StringBuilder content = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                if(cache) {
                    this.cachedPage = content.toString();
                }

                return content.toString();

            } catch (IOException e) {
                return e;
            }
        } else return null;
    }

    @Override
    public NanoHTTPD.Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        Object resourcePage = this.readResource();
        String mime = NanoHTTPD.getMimeTypeForFile((this.actualResource instanceof File ? ((File)actualResource).getPath() : ((URL)actualResource).toString()));

        if(resourcePage instanceof Exception) {
            return NanoHTTPD.newFixedLengthResponse(
                    NanoHTTPD.Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, resourcePage.toString()
            );
        } else {
            return NanoHTTPD.newFixedLengthResponse(
                    NanoHTTPD.Response.Status.OK, mime, String.valueOf(resourcePage)
            );
        }
    }

    public void setCaching(boolean caching) {
        this.cache = caching;
    }
}
