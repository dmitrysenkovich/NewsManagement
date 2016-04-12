package com.epam.app.dao.impl;

import com.epam.app.dao.CommentRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.model.Comment;
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
 * Comment repository implementation.
 */
public class CommentRepositoryImpl implements CommentRepository {
    private static final String ADD = "INSERT INTO Comments(news_id, comment_text, creation_date) VALUES(?, ?, ?)";
    private static final String FIND = "SELECT comment_id, news_id, comment_text, creation_date " +
            "FROM Comments WHERE comment_id = ?";
    private static final String UPDATE = "UPDATE Comments SET comment_text = ? WHERE comment_id = ?";
    private static final String DELETE = "DELETE FROM Comments WHERE comment_id = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public Comment add(Comment comment) throws DaoException {
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
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return comment;
    }


    @Override
    public Comment find(Long commentId) throws DaoException {
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
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return comment;
    }


    @Override
    public void update(Comment comment) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, comment.getCommentText());
            preparedStatement.setLong(2, comment.getCommentId());
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
    public void delete(Comment comment) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, comment.getCommentId());
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
    public List<Comment> addAll(List<Comment> comments) throws DaoException {
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
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return comments;
    }


    @Override
    public void deleteAll(List<Comment> comments) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            for (Comment comment : comments) {
                preparedStatement.setLong(1, comment.getCommentId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
    }
}
