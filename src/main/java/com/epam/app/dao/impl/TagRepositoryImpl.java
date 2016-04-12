package com.epam.app.dao.impl;

import com.epam.app.dao.TagRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.model.Tag;
import com.epam.app.utils.DatabaseUtils;
import org.apache.log4j.Logger;
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
public class TagRepositoryImpl implements TagRepository {
    private static final String ADD = "INSERT INTO Tags(tag_name) VALUES(?)";
    private static final String FIND = "SELECT tag_id, tag_name FROM Tags WHERE tag_id = ?";
    private static final String UPDATE = "UPDATE Tags SET tag_name = ? WHERE tag_id = ?";
    private static final String DELETE = "DELETE FROM Tags WHERE tag_id = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    public Long add(Tag tag) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Long tagId = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
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


    public List<Long> addAll(List<Tag> tags) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Long> ids = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
            for (Tag tag : tags) {
                preparedStatement.setString(1, tag.getTagName());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            ids = new LinkedList<>();
            while (resultSet.next())
                ids.add(resultSet.getLong(1));
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return ids;
    }
}
