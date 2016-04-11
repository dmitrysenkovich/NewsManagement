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
import java.util.List;

/**
 * Tag repository implementation.
 */
public class TagRepositoryImpl implements TagRepository {
    private static final Logger logger = Logger.getLogger(TagRepositoryImpl.class.getName());

    private static final String ADD = "INSERT INTO Tags(tag_name) VALUES(?)";
    private static final String FIND = "SELECT * FROM Tags WHERE tag_id = ?";
    private static final String UPDATE = "UPDATE Tags SET tag_name = ? WHERE tag_id = ?";
    private static final String DELETE = "DELETE FROM Tags WHERE tag_id = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    public Tag add(Tag tag) throws DaoException {
        logger.info("Adding tag..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, tag.getTagName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            Long tagId = resultSet.getLong(1);
            tag.setTagId(tagId);
            logger.info("Successfully added tag");
        }
        catch (SQLException e) {
            logger.error("Error while adding tag: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while adding tag: ",
                    preparedStatement, connection);
        }
        return tag;
    }


    public Tag find(Long tagId) throws DaoException {
        logger.info("Retrieving tag..");
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
            logger.info("Successfully retrieved tag");
        }
        catch (SQLException e) {
            logger.error("Error while retrieving tag: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while retrieving tag: ",
                    preparedStatement, connection);
        }
        return tag;
    }


    public void update(Tag tag) throws DaoException {
        logger.info("Updating tag..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, tag.getTagName());
            preparedStatement.setLong(2, tag.getTagId());
            preparedStatement.executeUpdate();
            logger.info("Successfully updated tag");
        }
        catch (SQLException e) {
            logger.error("Error while updating tag: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while updating tag: ",
                    preparedStatement, connection);
        }
    }


    public void delete(Tag tag) throws DaoException {
        logger.info("Deleting tag..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, tag.getTagId());
            preparedStatement.executeUpdate();
            logger.info("Successfully deleted tag");
        }
        catch (SQLException e) {
            logger.error("Error while deleting tag: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while deleting tag: ",
                    preparedStatement, connection);
        }
    }


    public List<Tag> addAll(List<Tag> tags) throws DaoException {
        logger.info("Adding tags..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
            for (Tag tag : tags) {
                preparedStatement.setString(1, tag.getTagName());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            int i = 0;
            while (resultSet.next()) {
                Tag tag = tags.get(i);
                tag.setTagId(resultSet.getLong(1));
                i++;
            }
            logger.info("Successfully added tags");
        }
        catch (SQLException e) {
            logger.error("Error while adding tags: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while adding tags: ",
                    preparedStatement, connection);
        }
        return tags;
    }
}
