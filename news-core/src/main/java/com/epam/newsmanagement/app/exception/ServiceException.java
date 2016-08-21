package com.epam.newsmanagement.app.exception;

/**
 * Service exception.
 */
public class ServiceException extends Exception {
    private Throwable exception;

    public ServiceException() {}

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
        exception = cause;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        exception = cause;
    }

    public ServiceException(String message, Throwable cause,
                        boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        exception = cause;
    }

    public Throwable getException() {
        return exception;
    }
}
