package com.epam.newsmanagement.app.exception;

import org.springframework.dao.DataAccessException;

/**
 * Dao exception.
 */
public class DaoException extends DataAccessException {
    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
