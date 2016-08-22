package com.epam.newsmanagement.app.exception;

/**
 * Service exception.
 */
public class ServiceException extends Exception {
    private Throwable exception;

    /**
     * Just to create an empty exception.
     */
    public ServiceException() {}

    /**
     * Creates an exception with the message only.
     * @param message message.
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the exceptions' cause.
     * @param cause the exceptions' cause.
     */
    public ServiceException(Throwable cause) {
        super(cause);
        exception = cause;
    }

    /**
     * Creates an exception with the message and the exceptions' cause.
     * @param message message.
     * @param cause the exceptions' cause.
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        exception = cause;
    }

    /**
     * Creates an exception with the message and the exceptions' cause.
     * @param message message.
     * @param cause the exceptions' cause.
     * @param enableSuppression whether or not suppression is enabled or disabled.
     * @param writableStackTrace whether or not the stack trace should be writable.
     */
    public ServiceException(String message, Throwable cause,
                        boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        exception = cause;
    }

    public Throwable getException() {
        return exception;
    }
}
