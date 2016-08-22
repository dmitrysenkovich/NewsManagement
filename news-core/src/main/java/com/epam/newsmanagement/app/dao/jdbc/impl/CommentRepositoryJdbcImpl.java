package com.epam.newsmanagement.app.dao.jdbc.impl;

import com.epam.newsmanagement.app.dao.jdbc.CommentRepositoryJdbc;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Comment repository implementation.
 */
public class CommentRepositoryJdbcImpl implements CommentRepositoryJdbc {
    private static final String ADD = "INSERT INTO COMMENTS(NEWS_ID, COMMENT_TEXT, CREATION_DATE) VALUES(?, ?, ?)";
    private static final String FIND = "SELECT COMMENT_ID, NEWS_ID, COMMENT_TEXT, CREATION_DATE " +
            "FROM COMMENTS WHERE COMMENT_ID = ?";
    private static final String UPDATE = "UPDATE COMMENTS SET COMMENT_TEXT = ? WHERE COMMENT_ID = ?";
    private static final String DELETE = "DELETE FROM COMMENTS WHERE COMMENT_ID = ?";
    private static final String COUNT_ALL_BY_NEWS = "SELECT COUNT(*) FROM COMMENTS " +
            "                                        WHERE NEWS_ID = ?" +
            "                                        GROUP BY NEWS_ID";
    private static final String FIND_ALL_BY_NEWS = "SELECT COMMENT_ID, NEWS_ID, COMMENT_TEXT, CREATION_DATE " +
            "FROM COMMENTS WHERE NEWS_ID = ? ORDER BY CREATION_DATE";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public Comment save(Comment comment) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            if (comment.getCommentId() == null) {
                preparedStatement = connection.prepareStatement(ADD, new String[]{ "COMMENT_ID" });
                preparedStatement.setLong(1, comment.getNews().getNewsId());
                preparedStatement.setString(2, comment.getCommentText());
                preparedStatement.setTimestamp(3, comment.getCreationDate());
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                comment.setCommentId(resultSet.getLong(1));
            }
            else {
                preparedStatement = connection.prepareStatement(UPDATE);
                preparedStatement.setString(1, comment.getCommentText());
                preparedStatement.setLong(2, comment.getCommentId());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return comment;
    }


    @Override
    public Comment findOne(Long commentId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Comment comment = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND);
            preparedStatement.setLong(1, commentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                comment = new Comment();
                comment.setCommentId(commentId);
                News news = new News();
                news.setNewsId(resultSet.getLong(2));
                comment.setNews(news);
                comment.setCommentText(resultSet.getString(3));
                comment.setCreationDate(resultSet.getTimestamp(4));
            }
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return comment;
    }


    @Override
    public void delete(Comment comment) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, comment.getCommentId());
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
    public void deleteAll(List<Comment> comments) {
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
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
    }


    @Override
    public Long countAllByNews(News news) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Long count = -1L;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(COUNT_ALL_BY_NEWS);
            preparedStatement.setLong(1, news.getNewsId());
            ResultSet resultSet = preparedStatement.executeQuery();
            count = 0L;
            while (resultSet.next())
                count = resultSet.getLong(1);
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return count;
    }


    @Override
    public List<Comment> findAllByNews(News news) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Comment> commentsByNews = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND_ALL_BY_NEWS);
            preparedStatement.setLong(1, news.getNewsId());
            ResultSet resultSet = preparedStatement.executeQuery();

            commentsByNews = new LinkedList<>();
            while (resultSet.next()) {
                Comment comment = new Comment();
                comment.setCommentId(resultSet.getLong(1));
                comment.setNews(news);
                comment.setCommentText(resultSet.getString(3));
                comment.setCreationDate(resultSet.getTimestamp(4));
                commentsByNews.add(comment);
            }
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return commentsByNews;
    }
}
