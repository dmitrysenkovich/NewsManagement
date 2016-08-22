package com.epam.newsmanagement.app.exception;

import org.springframework.dao.DataAccessException;

/**
 * Dao exception.
 */
public class DaoException extends DataAccessException {
    /**
     * Creates an exception with the message only.
     * @param message message.
     */
    public DaoException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the message and the exceptions' cause.
     * @param message message.
     * @param cause the exceptions' cause.
     */
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
