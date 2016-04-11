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
    private static final Logger logger = Logger.getLogger(NewsRepositoryImpl.class.getName());

    private static final String ADD = "INSERT INTO News(title, short_text, full_text, " +
            "creation_date, modification_date) VALUES(?, ?, ?, ?, ?)";
    private static final String FIND = "SELECT * FROM News WHERE news_id = ?";
    private static final String UPDATE = "UPDATE News SET title = ?, short_text = ?, " +
            "full_text = ?, modification_date = ? WHERE news_id = ?";
    private static final String DELETE = "DELETE FROM News WHERE news_id = ?";
    private static final String FIND_ALL_SORTED = "(SELECT news_id, title, short_text, " +
            "full_text, creation_date, modification_date, comments_count " +
            "FROM News JOIN (SELECT news_id, COUNT(*) AS comments_count " +
            "                FROM Comments " +
            "                GROUP BY news_id) AS News_Stat USING(news_id))" +
            "UNION " +
            "(SELECT news_id, title, short_text, " +
            "full_text, creation_date, modification_date, 0 AS comments_count " +
            "FROM News " +
            "WHERE news_id NOT IN (SELECT news_id " +
            "                      FROM Comments))" +
            "ORDER BY comments_count DESC";
    private static final String COUNT_ALL_NEWS = "SELECT COUNT(*) FROM News";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public News add(News news) throws DaoException {
        logger.info("Adding news..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getShortText());
            preparedStatement.setString(3, news.getFullText());
            preparedStatement.setTimestamp(4, news.getCreationDate());
            preparedStatement.setDate(5, news.getModificationDate());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            Long newsId = resultSet.getLong(1);
            news.setNewsId(newsId);
            logger.info("Successfully added news");
        }
        catch (SQLException e) {
            logger.error("Error while adding news: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while adding news: ",
                    preparedStatement, connection);
        }
        return news;
    }


    @Override
    public News find(Long newsId) throws DaoException {
        logger.info("Retrieving news..");
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
            logger.info("Successfully retrieved news");
        }
        catch (SQLException e) {
            logger.error("Error while retrieving news: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while retrieving news: ",
                    preparedStatement, connection);
        }
        return news;
    }


    @Override
    public void update(News news) throws DaoException {
        logger.info("Updating news..");
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
            logger.info("Successfully updated news");
        }
        catch (SQLException e) {
            logger.error("Error while updating news: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while updating news: ",
                    preparedStatement, connection);
        }
    }


    @Override
    public void delete(News news) throws DaoException {
        logger.info("Deleting news..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, news.getNewsId());
            preparedStatement.executeUpdate();
            logger.info("Successfully deleted news");
        }
        catch (SQLException e) {
            logger.error("Error while deleting news: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while deleting news: ",
                    preparedStatement, connection);
        }
    }


    @Override
    public List<News> search(final String SEARCH_CRITERIA_QUERY) throws DaoException {
        logger.info("Retrieving news according to search criteria..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<News> fitNews = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(SEARCH_CRITERIA_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();

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
            logger.info("Successfully retrieved news according to search criteria");
        }
        catch (SQLException e) {
            logger.error("Error while retrieving news: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while retrieving news: ",
                    preparedStatement, connection);
        }
        return fitNews;
    }


    @Override
    public List<News> findAllSorted() throws DaoException {
        logger.info("Retrieving all news..");
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
            logger.info("Successfully retrieved all news");
        }
        catch (SQLException e) {
            logger.error("Error while retrieving news: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while retrieving news: ",
                    statement, connection);
        }
        return allNews;
    }


    @Override
    public Long countAll() throws DaoException {
        logger.info("Retrieving news..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Long count = -1L;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(COUNT_ALL_NEWS);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getLong(1);
            logger.info("Successfully retrieved news count");
        }
        catch (SQLException e) {
            logger.error("Error while retrieving news count: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while retrieving news count: ",
                    preparedStatement, connection);
        }
        return count;
    }
}
