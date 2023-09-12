package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

public abstract class CSSPageTemplate extends CSSPage {

    public static String tokenOpen = "{#";

    public static String tokenClose = "}";

    public CSSPageTemplate(String css) {
        super(css);
        this.css = setupResponse(css);
    }

    @Override
    public NanoHTTPD.Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        return super.getCSSResponse(formatResponse(this.css, session));
    }

    protected abstract String formatResponse(String css, NanoHTTPD.IHTTPSession session);

    protected String setupResponse(String css) {
        return css;
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
            return new String(Objects.requireNonNull(CSSPageTemplate.class.getResourceAsStream(path)).readAllBytes());
        } catch (IOException | NullPointerException e) {
            throw new TemplateNotFoundException("Resource: " + path + " cannot be found", e);
        }
    }

    public static String token(String tokenName) {
        return tokenOpen + tokenName.toUpperCase() + tokenClose;
    }
}
