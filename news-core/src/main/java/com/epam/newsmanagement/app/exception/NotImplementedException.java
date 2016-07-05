package com.epam.newsmanagement.app.exception;

import org.springframework.dao.DataAccessException;

/**
 * For indicating not implemented methods.
 */
public class NotImplementedException extends DataAccessException {
    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }
}
