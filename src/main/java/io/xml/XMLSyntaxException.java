package io.xml;

/**
 * @version 08.12.2021
 * @since 08.12.2021
 */
public class XMLSyntaxException extends RuntimeException {

    public XMLSyntaxException() {
    }

    public XMLSyntaxException(String message) {
        super(message);
    }

    public XMLSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public XMLSyntaxException(Throwable cause) {
        super(cause);
    }

    public XMLSyntaxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}