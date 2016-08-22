package com.epam.newsmanagement.app.exception;

import org.springframework.dao.DataAccessException;

/**
 * For indicating not implemented methods.
 */
public class NotImplementedException extends DataAccessException {
    /**
     * Creates an exception with the message only.
     * @param message message.
     */
    public NotImplementedException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the message and the exceptions' cause.
     * @param message message.
     * @param cause the exceptions' cause.
     */
    public NotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }
}
