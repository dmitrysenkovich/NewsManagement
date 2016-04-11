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
    public void closeConnectionAndStatement(Logger logger, String errorMessage,
                                            Statement statement,
                                            Connection connection) throws DaoException {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error(errorMessage, e);
                throw new DaoException(e);
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(errorMessage, e);
                throw new DaoException(e);
            }
        }
    }
}
