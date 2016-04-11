package com.epam.app.dao.impl;

import com.epam.app.dao.CommentRepository;
import com.epam.app.model.Comment;
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
 * Comment repository implementation.
 */
public class CommentRepositoryImpl implements CommentRepository {
    private static final Logger logger = Logger.getLogger(CommentRepositoryImpl.class.getName());

    private static final String ADD = "INSERT INTO Comments(news_id, comment_text, creation_date) VALUES(?, ?, ?)";
    private static final String FIND = "SELECT * FROM Comments WHERE comment_id = ?";
    private static final String UPDATE = "UPDATE Comments SET comment_text = ? WHERE comment_id = ?";
    private static final String DELETE = "DELETE FROM Comments WHERE comment_id = ?";

    @Autowired
    private DataSource dataSource;


    @Override
    public Comment add(Comment comment) {
        logger.info("Adding comment..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, comment.getNewsId());
            preparedStatement.setString(2, comment.getCommentText());
            preparedStatement.setTimestamp(3, comment.getCreationDate());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            Long commentId = resultSet.getLong(1);
            comment.setCommentId(commentId);
            logger.info("Successfully added comment");
        }
        catch (SQLException e) {
            logger.error("Error while adding comment: ", e);
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after adding comment", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after adding comment", e);
                }
            }

            return comment;
        }
    }


    @Override
    public Comment find(Long commentId) {
        logger.info("Retrieving comment..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Comment comment = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND);
            preparedStatement.setLong(1, commentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            comment = new Comment();
            comment.setCommentId(commentId);
            comment.setNewsId(resultSet.getLong(2));
            comment.setCommentText(resultSet.getString(3));
            comment.setCreationDate(resultSet.getTimestamp(4));
            logger.info("Successfully retrieved comment");
        }
        catch (SQLException e) {
            logger.error("Error while retrieving comment: ", e);
            comment = null;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after retrieving comment", e);
                    comment = null;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after retrieving comment", e);
                    comment = null;
                }
            }

            return comment;
        }
    }


    @Override
    public boolean update(Comment comment) {
        logger.info("Updating comment..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, comment.getCommentText());
            preparedStatement.setLong(2, comment.getCommentId());
            preparedStatement.executeUpdate();
            logger.info("Successfully updated comment");
        }
        catch (SQLException e) {
            logger.error("Error while updating comment: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after updating comment", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after updating comment", e);
                    result = false;
                }
            }

            return result;
        }
    }


    @Override
    public boolean delete(Comment comment) {
        logger.info("Deleting comment..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, comment.getCommentId());
            preparedStatement.executeUpdate();
            logger.info("Successfully deleted comment");
        }
        catch (SQLException e) {
            logger.error("Error while deleting comment: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after deleting comment", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after deleting comment", e);
                    result = false;
                }
            }

            return result;
        }
    }


    @Override
    public List<Comment> addAll(List<Comment> comments) {
        logger.info("Adding comments..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
            for (Comment comment : comments) {
                preparedStatement.setLong(1, comment.getNewsId());
                preparedStatement.setString(2, comment.getCommentText());
                preparedStatement.setTimestamp(3, comment.getCreationDate());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            int i = 0;
            while (resultSet.next()) {
                Comment comment = comments.get(i);
                comment.setCommentId(resultSet.getLong(1));
                i++;
            }
            logger.info("Successfully added comments");
        }
        catch (SQLException e) {
            logger.error("Error while adding comments: ", e);
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after adding comments: ", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after adding comments: ", e);
                }
            }

            return comments;
        }
    }


    @Override
    public boolean deleteAll(List<Comment> comments) {
        logger.info("Deleting comments..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            for (Comment comment : comments) {
                preparedStatement.setLong(1, comment.getCommentId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            logger.info("Successfully deleted comments");
        }
        catch (SQLException e) {
            logger.error("Error while deleting comments: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after deleting comments: ", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after deleting comments: ", e);
                    result = false;
                }
            }

            return result;
        }
    }
}
