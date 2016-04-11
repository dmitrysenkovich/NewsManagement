package com.epam.app.utils;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Database utils. For logging only.
 */
public class DatabaseUtils {
    public void closeConnectionAndStatement(Logger logger, String errorMessage,
                                            PreparedStatement preparedStatement,
                                            Connection connection) {

    }
}
