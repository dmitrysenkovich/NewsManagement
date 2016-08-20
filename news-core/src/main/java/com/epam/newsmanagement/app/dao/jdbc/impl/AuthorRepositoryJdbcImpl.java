package com.epam.newsmanagement.app.dao.jdbc.impl;

import com.epam.newsmanagement.app.dao.jdbc.AuthorRepositoryJdbc;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Author repository implementation.
 */
public class AuthorRepositoryJdbcImpl implements AuthorRepositoryJdbc {
    private static final String ADD = "INSERT INTO AUTHORS(AUTHOR_NAME, EXPIRED) VALUES(?, ?)";
    private static final String FIND = "SELECT AUTHOR_ID, AUTHOR_NAME, EXPIRED FROM AUTHORS WHERE AUTHOR_ID = ?";
    private static final String UPDATE = "UPDATE AUTHORS SET AUTHOR_NAME = ?, EXPIRED = ? " +
            "WHERE AUTHOR_ID = ?";
    private static final String FIND_ALL_BY_NEWS = "SELECT AUTHOR_ID, AUTHOR_NAME, EXPIRED FROM AUTHORS " +
            "WHERE AUTHOR_ID IN (SELECT AUTHOR_ID FROM NEWS_AUTHOR WHERE NEWS_ID = ?)";
    private static final String FIND_NOT_EXPIRED = "SELECT AUTHOR_ID, AUTHOR_NAME, EXPIRED FROM AUTHORS " +
            "WHERE EXPIRED IS NULL";
    private static final String FIND_ALL = "SELECT AUTHOR_ID, AUTHOR_NAME, EXPIRED FROM AUTHORS";
    private static final String EXISTS = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM AUTHORS WHERE AUTHOR_NAME = ?";
    private static final String ADD_NEWS_AUTHOR_RELATION = "INSERT INTO NEWS_AUTHOR(NEWS_ID, AUTHOR_ID) VALUES(?, ?)";
    private static final String DELETE_ALL_RELATIONS_BY_NEWS = "DELETE FROM NEWS_TAG WHERE NEWS_ID = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public Author save(Author author) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            if (author.getAuthorId() == null) {
                preparedStatement = connection.prepareStatement(ADD, new String[]{"AUTHOR_ID"});
                preparedStatement.setString(1, author.getAuthorName());
                preparedStatement.setTimestamp(2, author.getExpired());
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                author.setAuthorId(resultSet.getLong(1));
            }
            else {
                preparedStatement = connection.prepareStatement(UPDATE);
                preparedStatement.setString(1, author.getAuthorName());
                preparedStatement.setTimestamp(2, author.getExpired());
                preparedStatement.setLong(3, author.getAuthorId());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return author;
    }


    @Override
    public Author findOne(Long authorId) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Author author = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND);
            preparedStatement.setLong(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                author = new Author();
                author.setAuthorId(authorId);
                author.setAuthorName(resultSet.getString(2));
                author.setExpired(resultSet.getTimestamp(3));
            }
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return author;
    }


    @Override
    public List<Author> findAllByNews(News news) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Author> authorsByNews = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND_ALL_BY_NEWS);
            preparedStatement.setLong(1, news.getNewsId());
            ResultSet resultSet = preparedStatement.executeQuery();

            authorsByNews = new LinkedList<>();
            while (resultSet.next()) {
                Author author = new Author();
                author.setAuthorId(resultSet.getLong(1));
                author.setAuthorName(resultSet.getString(2));
                author.setExpired(resultSet.getTimestamp(3));
                authorsByNews.add(author);
            }
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return authorsByNews;
    }


    @Override
    public List<Author> findNotExpired() throws DaoException {
        Connection connection = null;
        Statement statement = null;
        List<Author> notExpiredAuthors = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_NOT_EXPIRED);

            notExpiredAuthors = new LinkedList<>();
            while (resultSet.next()) {
                Author author = new Author();
                author.setAuthorId(resultSet.getLong(1));
                author.setAuthorName(resultSet.getString(2));
                author.setExpired(resultSet.getTimestamp(3));
                notExpiredAuthors.add(author);
            }
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(statement, connection);
        }
        return notExpiredAuthors;
    }


    @Override
    public List<Author> findAll() throws DaoException {
        Connection connection = null;
        Statement statement = null;
        List<Author> allAuthors = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);

            allAuthors = new LinkedList<>();
            while (resultSet.next()) {
                Author author = new Author();
                author.setAuthorId(resultSet.getLong(1));
                author.setAuthorName(resultSet.getString(2));
                author.setExpired(resultSet.getTimestamp(3));
                allAuthors.add(author);
            }
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(statement, connection);
        }
        return allAuthors;
    }


    @Override
    public boolean exists(String authorName) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean exists = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(EXISTS);
            preparedStatement.setString(1, authorName);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            exists = resultSet.getBoolean(1);
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return exists;
    }


    @Override
    public void addAll(News news, List<Author> authors) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD_NEWS_AUTHOR_RELATION);
            for (Author author : authors) {
                preparedStatement.setLong(1, news.getNewsId());
                preparedStatement.setLong(2, author.getAuthorId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
    }


    @Override
    public void deleteAllRelationsByNews(News news) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE_ALL_RELATIONS_BY_NEWS);
            preparedStatement.setLong(1, news.getNewsId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
    }
}
