package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

/**
 * CSS Templates with token replacement, which can be loaded from file, resource, or String.
 * <br>
 *
 * A basic template for {@link CSSPage CSSPages}, which allows
 * manipulating the response css before being served, with {@link
 * #setupResponse(String)} and {@link #formatResponse(String, NanoHTTPD.IHTTPSession)}.
 * With support for simple string token replacement, to create dynamic css pages which
 * can be tailored to specific responses.
 */
public abstract class CSSPageTemplate extends CSSPage {

    /**
     * Denotes the opening tag for a token. Can be changed.
     * Used with {@link #token(String)}
     */
    public static String tokenOpen = "{#";

    /**
     * Denotes the closing tag for a token. Can be changed.
     * Used with {@link #token(String)}
     */
    public static String tokenClose = "}";

    /**
     * Creates a new CSS Template with a CSS String.
     *
     * @param css the CSS String.
     */
    public CSSPageTemplate(String css) {
        super(css);
        this.css = setupResponse(css);
    }

    @Override
    public NanoHTTPD.Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        return super.getCSSResponse(formatResponse(this.css, session));
    }

    /**
     * Used to format the CSS response text <b>each time the page is served</b>.
     * This is where you should replace String tokens, insert {@link Snippet snippets},
     * and do all other formatting which needs to be done when the page is served to a
     * new client.
     *
     * @param css pre-formatted css response text.
     * @param session session object (with information about the client and connection)
     * @return post-formatted css response text. Don't return {@code null}.
     */
    protected abstract String formatResponse(String css, NanoHTTPD.IHTTPSession session);

    /**
     * Used to format the CSS response text <b>once when initialized</b>.
     * The formatted CSS will then be passed to {@link #formatResponse(String, NanoHTTPD.IHTTPSession)}.
     * This is where you do all the ONE TIME formatting for the CSS.
     *
     * @param css the original pre-formatted css response text.
     * @return post-formatted css response text. Don't return {@code null}.
     */
    protected String setupResponse(String css) {
        return css;
    }

    // Static Methods

    /**
     * Helper method to get CSS text from a file on disk.
     *
     * @throws TemplateNotFoundException if the file cannot be found.
     */
    protected static String loadFromFile(File file) throws TemplateNotFoundException {
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new TemplateNotFoundException("File: " + file.getAbsolutePath() + " cannot be found", e);
        }
    }

    /**
     * Helper method to get CSS text from a resource file in the jar.
     *
     * @throws TemplateNotFoundException if the file cannot be found.
     */
    protected static String loadFromResource(String path) throws TemplateNotFoundException {
        try {
            return new String(Objects.requireNonNull(CSSPageTemplate.class.getResourceAsStream(path)).readAllBytes());
        } catch (IOException | NullPointerException e) {
            throw new TemplateNotFoundException("Resource: " + path + " cannot be found", e);
        }
    }

    /**
     * Helper method to tokenize names. E.g. input "token-name" outputs "{#TOKEN-NAME}"
     */
    public static String token(String tokenName) {
        return tokenOpen + tokenName.toUpperCase() + tokenClose;
    }
}
