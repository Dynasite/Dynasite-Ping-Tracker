package org.dynasite.page;

/**
 * Thrown when an error occurs creating a Template from a template file
 * which cannot be found.
 *
 * @see HTMLPageTemplate
 * @see JSPageTemplate
 * @see JSONPageTemplate
 * @see CSSPageTemplate
 */
public class TemplateNotFoundException extends RuntimeException {

    public TemplateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
