package com.epam.app.dao.impl;

import com.epam.app.dao.AuthorRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.model.Author;
import com.epam.app.utils.DatabaseUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Author repository implementation.
 */
public class AuthorRepositoryImpl implements AuthorRepository {
    private static final String ADD = "INSERT INTO Authors(author_name, expired) VALUES(?, ?)";
    private static final String FIND = "SELECT author_id, author_name, expired FROM Authors WHERE author_id = ?";
    private static final String UPDATE = "UPDATE Authors SET author_name = ?, expired = ? " +
            "WHERE author_id = ?";
    private static final String MAKE_AUTHOR_EXPIRED = "UPDATE Authors SET expired = ? WHERE author_id = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public Long add(Author author) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Long authorId = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, author.getAuthorName());
            preparedStatement.setTimestamp(2, author.getExpired());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            authorId = resultSet.getLong(1);
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return authorId;
    }


    @Override
    public Author find(Long authorId) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Author author = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND);
            preparedStatement.setLong(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            author = new Author();
            author.setAuthorId(authorId);
            author.setAuthorName(resultSet.getString(2));
            author.setExpired(resultSet.getTimestamp(3));
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return author;
    }


    @Override
    public void update(Author author) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, author.getAuthorName());
            preparedStatement.setTimestamp(2, author.getExpired());
            preparedStatement.setLong(3, author.getAuthorId());
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
    public void makeAuthorExpired(Author author) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(MAKE_AUTHOR_EXPIRED);
            preparedStatement.setTimestamp(1, author.getExpired());
            preparedStatement.setLong(2, author.getAuthorId());
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
