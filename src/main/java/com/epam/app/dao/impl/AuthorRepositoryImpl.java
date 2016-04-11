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
    private static final Logger logger = Logger.getLogger(AuthorRepositoryImpl.class.getName());

    private static final String ADD = "INSERT INTO Authors(author_name, expired) VALUES(?, ?)";
    private static final String FIND = "SELECT * FROM Authors WHERE author_id = ?";
    private static final String UPDATE = "UPDATE Authors SET author_name = ?, expired = ? " +
            "WHERE author_id = ?";
    private static final String DELETE = "DELETE FROM Authors WHERE author_id = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public Author add(Author author) throws DaoException {
        logger.info("Adding author..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, author.getAuthorName());
            preparedStatement.setTimestamp(2, author.getExpired());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            Long authorId = resultSet.getLong(1);
            author.setAuthorId(authorId);
            logger.info("Successfully added author");
        }
        catch (SQLException e) {
            logger.error("Error while adding author: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while adding author: ",
                    preparedStatement, connection);
        }
        return author;
    }


    @Override
    public Author find(Long authorId) throws DaoException {
        logger.info("Retrieving author..");
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
            logger.info("Successfully retrieved author");
        }
        catch (SQLException e) {
            logger.error("Error while retrieving author: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while retrieving author: ",
                    preparedStatement, connection);
        }
        return author;
    }


    @Override
    public void update(Author author) throws DaoException {
        logger.info("Updating author..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, author.getAuthorName());
            preparedStatement.setTimestamp(2, author.getExpired());
            preparedStatement.setLong(3, author.getAuthorId());
            preparedStatement.executeUpdate();
            logger.info("Successfully updated author");
        }
        catch (SQLException e) {
            logger.error("Error while updating author: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while updating author: ",
                    preparedStatement, connection);
        }
    }


    @Override
    public void delete(Author author) throws DaoException {
        logger.info("Deleting author..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, author.getAuthorId());
            preparedStatement.executeUpdate();
            logger.info("Successfully deleted author");
        }
        catch (SQLException e) {
            logger.error("Error while deleting author: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while deleting author: ",
                    preparedStatement, connection);
        }
    }
}
