package org.dynasite.page;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class Snippet {

    private final String snippetName;

    private String snippetText = null;

    public Snippet(String snippetName) {
        this.snippetName = Objects.requireNonNull(snippetName);
    }

    // Getters

    public String get() {
        if(this.snippetText == null) {
            throw new SnippetLoadException(this, "Snippet has not been loaded.");
        }

        return this.snippetText;
    }

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

    public Snippet loadFromFile(File file) {
        try {
            setSnippetText(new String(Files.readAllBytes(file.toPath())));
        } catch (IOException e) {
            throw new SnippetLoadException(this, "Could not load snippet from file: " + file.getAbsolutePath(), e);
        }

        return this;
    }

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

    public static class SnippetLoadException extends RuntimeException {

        protected SnippetLoadException(Snippet snippet, String message) {
            super(message + " For snippet: " + snippet.getName());
        }

        protected SnippetLoadException(Snippet snippet, String message, Exception cause) {
            super(message + ". For snippet: " + snippet.getName(), cause);
        }
    }
}
