package com.epam.app.dao.impl;

import com.epam.app.dao.NewsAuthorRepository;
import com.epam.app.model.NewsAuthor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;

/**
 * NewsAuthor repository implementation.
 */
public class NewsAuthorRepositoryImpl implements NewsAuthorRepository {
    private static final Logger logger = Logger.getLogger(NewsAuthorRepositoryImpl.class.getName());

    private static final String ADD = "INSERT INTO News_Author(news_id, author_id) VALUES(?, ?);";
    private static final String DELETE = "DELETE FROM News_Author WHERE news_id = ? AND author_id = ?;";

    @Autowired
    private DataSource dataSource;

    public boolean add(NewsAuthor newsAuthor) {
        logger.info("Adding news to author relation..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD);
            preparedStatement.setLong(1, newsAuthor.getNewsId());
            preparedStatement.setLong(2, newsAuthor.getAuthorId());
            preparedStatement.executeUpdate();
            logger.info("Successfully added news to author relation");
        }
        catch (SQLException e) {
            logger.error("Error while adding news to author relation: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after adding news to author relation", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after adding news to author relation", e);
                    result = false;
                }
            }

            return result;
        }
    }


    public boolean delete(NewsAuthor newsAuthor) {
        logger.info("Deleting news to author relation..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, newsAuthor.getNewsId());
            preparedStatement.setLong(2, newsAuthor.getAuthorId());
            preparedStatement.executeUpdate();
            logger.info("Successfully deleted news to author relation");
        }
        catch (SQLException e) {
            logger.error("Error while deleting news to author relation: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after deleting news to author relation", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after deleting news to author relation", e);
                    result = false;
                }
            }

            return result;
        }
    }
}
