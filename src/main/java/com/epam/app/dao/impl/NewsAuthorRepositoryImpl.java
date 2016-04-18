package com.epam.app.dao.impl;

import com.epam.app.dao.NewsAuthorRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.model.NewsAuthor;
import com.epam.app.utils.DatabaseUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * NewsAuthor repository implementation.
 */
public class NewsAuthorRepositoryImpl implements NewsAuthorRepository {
    private static final String ADD = "INSERT INTO NEWS_AUTHOR(NEWS_ID, AUTHOR_ID) VALUES(?, ?)";
    private static final String DELETE = "DELETE FROM NEWS_AUTHOR WHERE NEWS_ID = ? AND AUTHOR_ID = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public void add(NewsAuthor newsAuthor) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD);
            preparedStatement.setLong(1, newsAuthor.getNewsId());
            preparedStatement.setLong(2, newsAuthor.getAuthorId());
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
    public void delete(NewsAuthor newsAuthor) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, newsAuthor.getNewsId());
            preparedStatement.setLong(2, newsAuthor.getAuthorId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
    }
}
