package com.epam.newsmanagement.app.dao.jdbc.impl;

import com.epam.newsmanagement.app.dao.jdbc.TagRepositoryJdbc;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;
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
 * Tag repository implementation.
 */
public class TagRepositoryJdbcImpl implements TagRepositoryJdbc {
    private static final String ADD = "INSERT INTO TAGS(TAG_NAME) VALUES(?)";
    private static final String FIND = "SELECT TAG_ID, TAG_NAME FROM TAGS WHERE TAG_ID = ?";
    private static final String UPDATE = "UPDATE TAGS SET TAG_NAME = ? WHERE TAG_ID = ?";
    private static final String DELETE = "DELETE FROM TAGS WHERE TAG_ID = ?";
    private static final String FIND_ALL_BY_NEWS = "SELECT TAG_ID, TAG_NAME FROM TAGS " +
            "WHERE TAG_ID IN (SELECT TAG_ID FROM NEWS_TAG WHERE NEWS_ID = ?)";
    private static final String FIND_ALL = "SELECT TAG_ID, TAG_NAME FROM TAGS";
    private static final String EXISTS = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM TAGS WHERE TAG_NAME = ?";
    private static final String ADD_NEWS_TAG_RELATION = "INSERT INTO NEWS_TAG(NEWS_ID, TAG_ID) VALUES(?, ?)";
    private static final String DELETE_ALL_BY_NEWS = "DELETE FROM NEWS_TAG WHERE NEWS_ID = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public Tag save(Tag tag) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            if (tag.getTagId() == null) {
                preparedStatement = connection.prepareStatement(ADD, new String[]{ "TAG_ID" });
                preparedStatement.setString(1, tag.getTagName());
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                tag.setTagId(resultSet.getLong(1));
            }
            else {
                preparedStatement = connection.prepareStatement(UPDATE);
                preparedStatement.setString(1, tag.getTagName());
                preparedStatement.setLong(2, tag.getTagId());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return tag;
    }


    @Override
    public Tag findOne(Long tagId) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Tag tag = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND);
            preparedStatement.setLong(1, tagId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                tag = new Tag();
                tag.setTagId(tagId);
                tag.setTagName(resultSet.getString(2));
            }
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return tag;
    }


    @Override
    public void delete(Tag tag) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, tag.getTagId());
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0)
                throw new SQLException("No rows deleted");
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
    }


    @Override
    public List<Tag> findAllByNews(News news) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Tag> tagsByNews = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND_ALL_BY_NEWS);
            preparedStatement.setLong(1, news.getNewsId());
            ResultSet resultSet = preparedStatement.executeQuery();

            tagsByNews = new LinkedList<>();
            while (resultSet.next()) {
                Tag tag = new Tag();
                tag.setTagId(resultSet.getLong(1));
                tag.setTagName(resultSet.getString(2));
                tagsByNews.add(tag);
            }
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return tagsByNews;
    }


    @Override
    public List<Tag> findAll() throws DaoException {
        Connection connection = null;
        Statement statement = null;
        List<Tag> allTags = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);

            allTags = new LinkedList<>();
            while (resultSet.next()) {
                Tag tag = new Tag();
                tag.setTagId(resultSet.getLong(1));
                tag.setTagName(resultSet.getString(2));
                allTags.add(tag);
            }
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(statement, connection);
        }
        return allTags;
    }


    @Override
    public boolean exists(String tagName) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean exists = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(EXISTS);
            preparedStatement.setString(1, tagName);
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
    public void addAll(News news, List<Tag> tags) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD_NEWS_TAG_RELATION);
            for (Tag tag : tags) {
                preparedStatement.setLong(1, news.getNewsId());
                preparedStatement.setLong(2, tag.getTagId());
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
            preparedStatement = connection.prepareStatement(DELETE_ALL_BY_NEWS);
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
