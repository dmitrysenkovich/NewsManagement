package com.epam.app.dao.impl;

import com.epam.app.dao.TagRepository;
import com.epam.app.model.Tag;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Tag repository implementation.
 */
public class TagRepositoryImpl implements TagRepository {
    private static final Logger logger = Logger.getLogger(TagRepositoryImpl.class.getName());

    private static final String ADD = "INSERT INTO Tag(tag_name) VALUES(?);";
    private static final String FIND = "SELECT * FROM Tag WHERE tag_id = ?;";
    private static final String UPDATE = "UPDATE Tag SET tag_name = ? WHERE tag_id = ?;";
    private static final String DELETE = "DELETE FROM Tag WHERE tag_id = ?";

    @Autowired
    private DataSource dataSource;


    public Tag add(Tag tag) {
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
            int tagId = resultSet.getInt(1);
            tag.setTagId(tagId);
            logger.info("Successfully added tag");
        }
        catch (SQLException e) {
            logger.error("Error while adding tag: ", e);
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after adding tag", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after adding tag", e);
                }
            }

            return tag;
        }
    }


    public Tag find(int tagId) {
        logger.info("Retrieving tag..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Tag tag = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND);
            preparedStatement.setInt(1, tagId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            tag = new Tag();
            tag.setTagId(tagId);
            tag.setTagName(resultSet.getString(2));
            logger.info("Successfully retrieved tag");
        }
        catch (SQLException e) {
            logger.error("Error while retrieving tag: ", e);
            tag = null;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after retrieving tag", e);
                    tag = null;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after retrieving tag", e);
                    tag = null;
                }
            }

            return tag;
        }
    }


    public boolean update(Tag tag) {
        logger.info("Updating tag..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, tag.getTagName());
            preparedStatement.setInt(2, tag.getTagId());
            preparedStatement.executeUpdate();
            logger.info("Successfully updated tag");
        }
        catch (SQLException e) {
            logger.error("Error while updating tag: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after updating tag", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after updating tag", e);
                    result = false;
                }
            }

            return result;
        }
    }


    public boolean delete(Tag tag) {
        logger.info("Deleting tag..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setInt(1, tag.getTagId());
            preparedStatement.executeUpdate();
            logger.info("Successfully deleted tag");
        }
        catch (SQLException e) {
            logger.error("Error while deleting tag: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after deleting tag", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after deleting tag", e);
                    result = false;
                }
            }

            return result;
        }
    }


    public List<Tag> addAll(List<Tag> tags) {
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
                tag.setTagId(resultSet.getInt(1));
                i++;
            }
            logger.info("Successfully added tags");
        }
        catch (SQLException e) {
            logger.error("Error while adding tags: ", e);
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after adding tags: ", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after adding tags: ", e);
                }
            }

            return tags;
        }
    }
}
