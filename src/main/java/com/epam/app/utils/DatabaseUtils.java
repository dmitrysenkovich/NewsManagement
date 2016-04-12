package com.epam.app.utils;

import com.epam.app.exception.DaoException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
                                            Connection connection) throws DaoException {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        }
    }
}
