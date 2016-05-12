package com.epam.newsmanagement.app.dao.impl;

import com.epam.newsmanagement.app.dao.TagRepository;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Tag repository implementation.
 */
public class TagRepositoryImpl implements TagRepository {
    private static final String ADD = "INSERT INTO TAGS(TAG_NAME) VALUES(?)";
    private static final String FIND = "SELECT TAG_ID, TAG_NAME FROM TAGS WHERE TAG_ID = ?";
    private static final String UPDATE = "UPDATE TAGS SET TAG_NAME = ? WHERE TAG_ID = ?";
    private static final String DELETE = "DELETE FROM TAGS WHERE TAG_ID = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public Long add(Tag tag) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Long tagId = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, new String[]{ "TAG_ID" });
            preparedStatement.setString(1, tag.getTagName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            tagId = resultSet.getLong(1);
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return tagId;
    }


    @Override
    public Tag find(Long tagId) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Tag tag = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND);
            preparedStatement.setLong(1, tagId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            tag = new Tag();
            tag.setTagId(tagId);
            tag.setTagName(resultSet.getString(2));
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return tag;
    }


    @Override
    public void update(Tag tag) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, tag.getTagName());
            preparedStatement.setLong(2, tag.getTagId());
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
    public void delete(Tag tag) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, tag.getTagId());
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
