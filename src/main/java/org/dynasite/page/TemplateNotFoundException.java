package org.dynasite.page;

public class TemplateNotFoundException extends RuntimeException {
    public TemplateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
