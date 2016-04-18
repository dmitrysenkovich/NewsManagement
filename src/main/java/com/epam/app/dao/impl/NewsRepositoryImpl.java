package com.epam.app.dao.impl;

import com.epam.app.dao.NewsRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.model.News;
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
 * News repository implementation.
 */
public class NewsRepositoryImpl implements NewsRepository {
    private static final String ADD = "INSERT INTO NEWS(TITLE, SHORT_TEXT, FULL_TEXT, " +
            "CREATION_DATE, MODIFICATION_DATE) VALUES(?, ?, ?, ?, ?)";
    private static final String FIND = "SELECT NEWS_ID, TITLE, SHORT_TEXT, FULL_TEXT, " +
            "CREATION_DATE, MODIFICATION_DATE FROM NEWS WHERE NEWS_ID = ?";
    private static final String UPDATE = "UPDATE NEWS SET TITLE = ?, SHORT_TEXT = ?, " +
            "FULL_TEXT = ?, MODIFICATION_DATE = ? WHERE NEWS_ID = ?";
    private static final String DELETE = "DELETE FROM NEWS WHERE NEWS_ID = ?";
    private static final String FIND_ALL_SORTED = "(SELECT NEWS_ID, TITLE, SHORT_TEXT, " +
            "FULL_TEXT, CREATION_DATE, MODIFICATION_DATE, COMMENTS_COUNT " +
            "FROM NEWS JOIN (SELECT NEWS_ID, COUNT(*) COMMENTS_COUNT " +
            "                FROM COMMENTS " +
            "                GROUP BY NEWS_ID) NEWS_STAT USING(NEWS_ID))" +
            "UNION " +
            "(SELECT NEWS_ID, TITLE, SHORT_TEXT, " +
            "FULL_TEXT, CREATION_DATE, MODIFICATION_DATE, 0 COMMENTS_COUNT " +
            "FROM NEWS " +
            "WHERE NEWS_ID NOT IN (SELECT NEWS_ID " +
            "                      FROM COMMENTS))" +
            "ORDER BY COMMENTS_COUNT DESC, MODIFICATION_DATE";
    private static final String COUNT_ALL_NEWS = "SELECT COUNT(*) FROM NEWS";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public Long add(News news) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Long newsId = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, new String[]{ "NEWS_ID" });
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getShortText());
            preparedStatement.setString(3, news.getFullText());
            preparedStatement.setTimestamp(4, news.getCreationDate());
            preparedStatement.setDate(5, news.getModificationDate());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            newsId = resultSet.getLong(1);
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return newsId;
    }


    @Override
    public News find(Long newsId) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        News news = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND);
            preparedStatement.setLong(1, newsId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            news = new News();
            news.setNewsId(newsId);
            news.setTitle(resultSet.getString(2));
            news.setShortText(resultSet.getString(3));
            news.setFullText(resultSet.getString(4));
            news.setCreationDate(resultSet.getTimestamp(5));
            news.setModificationDate(resultSet.getDate(6));
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return news;
    }


    @Override
    public void update(News news) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getShortText());
            preparedStatement.setString(3, news.getFullText());
            preparedStatement.setDate(4, news.getModificationDate());
            preparedStatement.setLong(5, news.getNewsId());
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
    public void delete(News news) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, news.getNewsId());
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
    public List<News> search(final String SEARCH_CRITERIA_QUERY) throws DaoException {
        Connection connection = null;
        Statement statement = null;
        List<News> fitNews = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SEARCH_CRITERIA_QUERY);

            fitNews = new LinkedList<>();
            while (resultSet.next()) {
                News news = new News();
                news.setNewsId(resultSet.getLong(1));
                news.setTitle(resultSet.getString(2));
                news.setShortText(resultSet.getString(3));
                news.setFullText(resultSet.getString(4));
                news.setCreationDate(resultSet.getTimestamp(5));
                news.setModificationDate(resultSet.getDate(6));
                fitNews.add(news);
            }
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(statement, connection);
        }
        return fitNews;
    }


    @Override
    public List<News> findAllSorted() throws DaoException {
        Connection connection = null;
        Statement statement = null;
        List<News> allNews = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SORTED);

            allNews = new LinkedList<>();
            while (resultSet.next()) {
                News news = new News();
                news.setNewsId(resultSet.getLong(1));
                news.setTitle(resultSet.getString(2));
                news.setShortText(resultSet.getString(3));
                news.setFullText(resultSet.getString(4));
                news.setCreationDate(resultSet.getTimestamp(5));
                news.setModificationDate(resultSet.getDate(6));
                allNews.add(news);
            }
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(statement, connection);
        }
        return allNews;
    }


    @Override
    public Long countAll() throws DaoException {
        Connection connection = null;
        Statement statement = null;
        Long count = -1L;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(COUNT_ALL_NEWS);
            resultSet.next();
            count = resultSet.getLong(1);
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(statement, connection);
        }
        return count;
    }
}
