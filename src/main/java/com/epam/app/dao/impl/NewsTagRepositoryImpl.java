package com.epam.app.dao.impl;

import com.epam.app.dao.NewsTagRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.model.NewsTag;
import com.epam.app.utils.DatabaseUtils;
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

    private static final String ADD = "INSERT INTO News_Tag(news_id, tag_id) VALUES(?, ?)";
    private static final String DELETE = "DELETE FROM News_Tag WHERE news_id = ? AND tag_id = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public void add(NewsTag newsTag) throws DaoException {
        logger.info("Adding news to tag relation..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD);
            preparedStatement.setLong(1, newsTag.getNewsId());
            preparedStatement.setLong(2, newsTag.getTagId());
            preparedStatement.executeUpdate();
            logger.info("Successfully added news to tag relation");
        }
        catch (SQLException e) {
            logger.error("Error while adding news to tag relation: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while adding news to tag relation: ",
                    preparedStatement, connection);
        }
    }


    @Override
    public void delete(NewsTag newsTag) throws DaoException {
        logger.info("Deleting news to tag relation..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, newsTag.getNewsId());
            preparedStatement.setLong(2, newsTag.getTagId());
            preparedStatement.executeUpdate();
            logger.info("Successfully deleted news to tag relation");
        }
        catch (SQLException e) {
            logger.error("Error while deleting news to tag relation: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while deleting news to tag relation: ",
                    preparedStatement, connection);
        }
    }
}
