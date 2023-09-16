package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

/**
 * JS Templates with token replacement, which can be loaded from file, resource, or String.
 * <br>
 *
 * A basic template for {@link JSPage JSPages}, which allows
 * manipulating the response JS before being served, with {@link
 * #setupResponse(String)} and {@link #formatResponse(String, NanoHTTPD.IHTTPSession)}.
 * With support for simple string token replacement, to create dynamic JS pages which
 * can be tailored to specific responses.
 */
public abstract class JSPageTemplate extends JSPage {

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
     * Creates a new JS Template with a JS String.
     *
     * @param js the JS String.
     */
    public JSPageTemplate(String js) {
        super(js);
        this.js = setupResponse(js);
    }

    @Override
    public NanoHTTPD.Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        return super.getJSResponse(formatResponse(this.js, session));
    }

    /**
     * Used to format the JS response text <b>each time the page is served</b>.
     * This is where you should replace String tokens, insert {@link Snippet snippets},
     * and do all other formatting which needs to be done when the page is served to a
     * new client.
     *
     * @param js pre-formatted JS response text.
     * @param session session object (with information about the client and connection)
     * @return post-formatted JS response text. Don't return {@code null}.
     */
    protected abstract String formatResponse(String js, NanoHTTPD.IHTTPSession session);

    /**
     * Used to format the JS response text <b>once when initialized</b>.
     * The formatted JS will then be passed to {@link #formatResponse(String, NanoHTTPD.IHTTPSession)}.
     * This is where you do all the ONE TIME formatting for the JS.
     *
     * @param js the original pre-formatted JS response text.
     * @return post-formatted JS response text. Don't return {@code null}.
     */
    protected String setupResponse(String js) {
        return js;
    }

    // Static Methods

    /**
     * Helper method to get JS text from a file on disk.
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
     * Helper method to get JS text from a resource file in the jar.
     *
     * @throws TemplateNotFoundException if the file cannot be found.
     */
    protected static String loadFromResource(String path) throws TemplateNotFoundException {
        try {
            return new String(Objects.requireNonNull(JSPageTemplate.class.getResourceAsStream(path)).readAllBytes());
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
