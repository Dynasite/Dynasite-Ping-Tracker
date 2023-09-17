package org.dynasite.page;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Represents basic text snippets contained within files,
 * either on disk or in the jar. Which can be inserted into
 * {@link Page} responses.
 */
public class Snippet {

    private final String snippetName;

    private String snippetText = null;

    /**
     * Creates a new named snippet.
     *
     * @param snippetName the snippet name.
     */
    public Snippet(String snippetName) {
        this.snippetName = Objects.requireNonNull(snippetName);
    }

    // Getters

    /**
     * @return the text of the snippet (read from file).
     */
    public String get() {
        if(this.snippetText == null) {
            throw new SnippetLoadException(this, "Snippet has not been loaded.");
        }

        return this.snippetText;
    }

    /**
     * @return the snippets name.
     */
    public String getName() {
        return snippetName;
    }

    private void setSnippetText(String text) {
        if(this.snippetText != null) {
            throw new SnippetLoadException(this, "Snippet is already loaded.");
        }

        this.snippetText = Objects.requireNonNull(text);
    }

    // Loaders

    /**
     * Loads the snippet text from a file. Cannot be called multiple times.
     *
     * @param file the file to load the text from.
     * @return the loaded snippet.
     * @throws SnippetLoadException if the file cannot be read or the snippet cannot be loaded.
     */
    public Snippet loadFromFile(File file) {
        try {
            setSnippetText(new String(Files.readAllBytes(file.toPath())));
        } catch (IOException e) {
            throw new SnippetLoadException(this, "Could not load snippet from file: " + file.getAbsolutePath(), e);
        }

        return this;
    }

    /**
     * Loads the snippet text from a file within the jar. Cannot be called multiple times.
     *
     * @param resource the resource location to load the file/text from.
     * @return the loaded snippet.
     * @throws SnippetLoadException if the file cannot be read or the snippet cannot be loaded.
     */
    public Snippet loadFromResource(String resource) {
        try {
            setSnippetText(new String(
                    Objects.requireNonNull(HTMLPage.class.getResourceAsStream(resource)).readAllBytes()
            ));
        } catch (IOException | NullPointerException e) {
            throw new SnippetLoadException(this, "Could not load snippet from resource: " + resource, e);
        }

        return this;
    }

    // Snippet Exception

    /**
     * Thrown when an error is raised working with, assigning, creating, or accessing {@link Snippet Snippets}.
     */
    public static class SnippetLoadException extends RuntimeException {

        protected SnippetLoadException(Snippet snippet, String message) {
            super(message + " For snippet: " + snippet.getName());
        }

        protected SnippetLoadException(Snippet snippet, String message, Exception cause) {
            super(message + ". For snippet: " + snippet.getName(), cause);
        }
    }
}
