package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

public abstract class HTMLPageTemplate extends HTMLPage {

    public HTMLPageTemplate(String html) {
        super(html);
        this.html = setupResponse(html);
    }

    @Override
    public NanoHTTPD.Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        return super.getHTMLResponse(formatResponse(this.html, session));
    }

    protected abstract String formatResponse(String html, NanoHTTPD.IHTTPSession session);

    protected String setupResponse(String html) {
        return html;
    }

    // Static Methods

    protected static String loadFromFile(File file) throws TemplateNotFoundException {
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new TemplateNotFoundException("File: " + file.getAbsolutePath() + " cannot be found", e);
        }
    }

    protected static String loadFromResource(String path) throws TemplateNotFoundException {
        try {
            return new String(Objects.requireNonNull(HTMLPage.class.getResourceAsStream(path)).readAllBytes());
        } catch (IOException | NullPointerException e) {
            throw new TemplateNotFoundException("Resource: " + path + " cannot be found", e);
        }
    }

    public static class TemplateNotFoundException extends RuntimeException {

        protected TemplateNotFoundException(String message, Exception cause) {
            super(message, cause);
        }
    }
}
