package com.epam.newsmanagement.app.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.insert.Insert;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Database initializing class. For initializing
 * database only. To be removed when not using oracle.
 */
public class DatabaseInitializingUtils {
    private static final Logger logger = Logger.getLogger(DatabaseInitializingUtils.class.getName());
    private static final String ROWS_COUNT_PER_TABLE = "SELECT * FROM " +
            "(SELECT COUNT(*) FROM NEWS NEWS_COUNT), (SELECT COUNT(*) FROM TAGS TAGS_COUNT), " +
            "(SELECT COUNT(*) FROM AUTHORS AUTHORS_COUNT), (SELECT COUNT(*) FROM ROLES ROLES_COUNT)";
    private static final String DATA_FILE_NAME = "data.sql";


    /**
     * Checks if database doesn't have rows.
     * If so returns null.
     * @return connection if database is empty
     * or null if not.
     */
    private static Connection checkIfDatabaseIsEmpty() {
        Connection connection = null;
        Statement statement = null;
        try {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (ClassNotFoundException e) {
                logger.error("Error loading database driver");
                return connection;
            }
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost", "dmitry", "pass");
            statement = connection.createStatement();
            statement.executeQuery(ROWS_COUNT_PER_TABLE);
            ResultSet resultSet = statement.getResultSet();
            boolean databaseIsEmpty = true;
            resultSet.next();
            Long newsCount = resultSet.getLong(1);
            Long tagsCount = resultSet.getLong(2);
            Long authorsCount = resultSet.getLong(3);
            Long rolesCount = resultSet.getLong(4);
            if (newsCount > 0 || tagsCount > 0 || authorsCount > 0 || rolesCount > 0)
                databaseIsEmpty = false;
            if (!databaseIsEmpty) {
                logger.info("Database is not empty. Skipping initialization");
                connection = null;
            }
        } catch (SQLException e) {
            logger.error("Error while checking if database is not empty: ", e);
            connection = null;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error("Error while checking if database is not empty: ", e);
                    connection = null;
                }
            }
        }
        return connection;
    }


    /**
     * Inserts default generated data.
     * @param connection database connection.
     */
    private static void insertData(Connection connection) {
        URL url;
        try {
            url = new URL(new ScriptFileUtils().getSearchScriptDirectoryPath() + DATA_FILE_NAME);
        } catch (MalformedURLException e) {
            logger.error("Error while loading data: ", e);
            return;
        }
        BufferedReader bufferedReader;
        try {
            InputStream inputStream = url.openStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (IOException e) {
            logger.error("Error while loading data: ", e);
            return;
        }

        String buffer;
        List<String> queries = new LinkedList<>();
        try {
            while((buffer = bufferedReader.readLine()) != null)
                queries.add(buffer);
            bufferedReader.close();
        } catch (IOException e) {
            logger.error("Error while loading data: ", e);
            return;
        }

        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        for(String query : queries)
        {
            if(!query.equals(""))
            {
                boolean newsInsert = query.contains("INSERT INTO NEWS VALUES");
                boolean commentsInsert = query.contains("INSERT INTO COMMENTS VALUES");
                boolean insertedSuccessfully;
                if (newsInsert || commentsInsert) {
                    int textIndex;
                    if (newsInsert)
                        textIndex = 3;
                    else
                        textIndex = 2;
                    insertedSuccessfully = insertNewsOrComment(connection, query, parserManager, textIndex);
                }
                else {
                    insertedSuccessfully = simpleInsert(connection, query);
                }
                if (!insertedSuccessfully)
                    return;
            }
        }
        logger.info("Successfully inserted data");
    }


    /**
     * Inserts news or comment row.
     * @param connection database connection.
     * @param originalQuery current insert query.
     * @param parserManager parses query.
     * @param textIndex index of text column in table.
     * @return true if successful.
     */
    private static boolean insertNewsOrComment(Connection connection, String originalQuery,
                                               CCJSqlParserManager parserManager, int textIndex) {
        String query;
        String text;
        PreparedStatement preparedStatement = null;
        try {
            Insert insert = (Insert) parserManager.parse(new StringReader(originalQuery));
            text = ((StringValue) ((ExpressionList) insert.getItemsList())
                    .getExpressions().get(textIndex)).getValue();
            query = originalQuery.replace("'" + text + "'", "?");
        } catch (JSQLParserException e) {
            logger.error("Error parsing query: ", e);
            return false;
        }
        try {
            preparedStatement = connection.prepareStatement(query);
            NClob nClob = connection.createNClob();
            nClob.setString(1, text);
            preparedStatement.setNClob(1, nClob);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error inserting data: ", e);
            return false;
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error inserting data: ", e);
                }
            }
        }
        return true;
    }


    /**
     * Inserts query not doing any
     * intermediate steps.
     * @param connection database connection.
     * @param query insert query.
     * @return true if successful.
     */
    private static boolean simpleInsert(Connection connection, String query) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.error("Error inserting data: ", e);
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error("Error inserting data: ", e);
                }
            }
        }
        return true;
    }


    /**
     * Closes connection.
     * @param connection database connection.
     */
    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Error closing connection: ", e);
            }
        }
    }


    /**
     * Initializes database with default
     * generated data if needed.
     */
    private static void initializeDatabase() {
        Connection connection = checkIfDatabaseIsEmpty();
        if (connection == null)
            return;

        insertData(connection);
        closeConnection(connection);
    }
}
