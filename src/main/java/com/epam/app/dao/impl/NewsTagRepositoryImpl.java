package com.epam.app.dao.impl;

import com.epam.app.dao.NewsTagRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.model.News;
import com.epam.app.model.NewsTag;
import com.epam.app.model.Tag;
import com.epam.app.utils.DatabaseUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * NewsTag repository implementation.
 */
public class NewsTagRepositoryImpl implements NewsTagRepository {
    private static final String ADD = "INSERT INTO NEWS_TAG(NEWS_ID, TAG_ID) VALUES(?, ?)";
    private static final String DELETE = "DELETE FROM NEWS_TAG WHERE NEWS_ID = ? AND TAG_ID = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public void add(NewsTag newsTag) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD);
            preparedStatement.setLong(1, newsTag.getNewsId());
            preparedStatement.setLong(2, newsTag.getTagId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
    }


    @Override
    public void delete(NewsTag newsTag) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, newsTag.getNewsId());
            preparedStatement.setLong(2, newsTag.getTagId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
    }


    @Override
    public void addAll(News news, List<Tag> tags) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD);
            for (Tag tag : tags) {
                preparedStatement.setLong(1, news.getNewsId());
                preparedStatement.setLong(2, tag.getTagId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
    }
}
