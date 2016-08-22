package com.epam.newsmanagement.app.utils;

import com.epam.newsmanagement.app.exception.DaoException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database utils.
 */
public class DatabaseUtils {
    /**
     * Closes connection and statement.
     * @param statement statement to be closed.
     * @param connection connection to be closed.
     * @throws DaoException thrown if couldn't
     * close statement or connection.
     */
    public void closeConnectionAndStatement(Statement statement,
                                            Connection connection) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new DaoException("Error while closing statement", e);
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DaoException("Error while closing connection", e);
            }
        }
    }
}
