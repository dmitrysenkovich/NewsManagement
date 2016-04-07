package com.epam.app.dao.impl;

import com.epam.app.dao.NewsTagRepository;
import com.epam.app.model.NewsTag;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * NewsTag repository implementation.
 */
public class NewsTagRepositoryImpl implements NewsTagRepository {
    private static final Logger logger = Logger.getLogger(NewsTagRepositoryImpl.class.getName());

    private static final String ADD = "INSERT INTO NewsTag(news_id, tag_id) VALUES(?, ?);";
    private static final String DELETE = "DELETE FROM NewsTag WHERE news_id = ? AND tag_id = ?;";

    @Autowired
    private DataSource dataSource;

    public boolean add(NewsTag newsTag) {
        logger.info("Adding news to tag relation..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD);
            preparedStatement.setInt(1, newsTag.getNewsId());
            preparedStatement.setInt(2, newsTag.getTagId());
            preparedStatement.executeUpdate();
            logger.info("Successfully added news to tag relation");
        }
        catch (SQLException e) {
            logger.error("Error while adding news to tag relation: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after adding news to tag relation", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after adding news to tag relation", e);
                    result = false;
                }
            }

            return result;
        }
    }


    public boolean delete(NewsTag newsTag) {
        logger.info("Deleting news to tag relation..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setInt(1, newsTag.getNewsId());
            preparedStatement.setInt(1, newsTag.getTagId());
            preparedStatement.executeUpdate();
            logger.info("Successfully deleted news to tag relation");
        }
        catch (SQLException e) {
            logger.error("Error while deleting news to tag relation: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after deleting news to tag relation", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after deleting news to tag relation", e);
                    result = false;
                }
            }

            return result;
        }
    }
}
