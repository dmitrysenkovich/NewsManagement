package com.epam.app.dao.impl;

import com.epam.app.dao.CrudRepository;
import com.epam.app.dao.NewsRepository;
import com.epam.app.model.News;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * News repository implementation.
 */
public class NewsRepositoryImpl implements CrudRepository<News>, NewsRepository {
    private static final Logger logger = Logger.getLogger(NewsRepositoryImpl.class.getName());

    private static final String ADD = "INSERT INTO News(title, short_text, full_text, " +
            "creation_date, modification_date) VALUES(?, ?, ?, ?, ?);";
    private static final String FIND = "SELECT * FROM News WHERE news_id = ?;";
    private static final String UPDATE = "UPDATE News SET title = ?, short_text = ?, " +
            "full_text = ?, modification_date = ? WHERE news_id = ?;";
    private static final String DELETE = "DELETE FROM News WHERE news_id = ?;";
    private static final String FIND_ALL_SORTED = "SELECT news_id, title, short_text, " +
            "full_text, creation_date, modification_date" +
            "FROM News JOIN (SELECT news_id, COUNT(*) AS comments_count" +
            "               FROM Comments" +
            "               GROUP BY news_id) AS News_Stat USING(news_id)" +
            "ORDER BY comments_count DESC;";
    private static final String COUNT_ALL_NEWS = "SELECT COUNT(*) FROM News;";

    @Autowired
    private DataSource dataSource;


    public News add(News news) {
        logger.info("Adding news..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        News result = null;
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
            int newsId = resultSet.getInt(1);
            news.setNewsId(newsId);
            result = news;
            logger.info("Successfully added news");
        }
        catch (SQLException e) {
            logger.error("Error while adding news: ", e);
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after adding news", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after adding news", e);
                }
            }

            return result;
        }
    }


    public News find(int newsId) {
        logger.info("Retrieving news..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        News news = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND);
            preparedStatement.setInt(1, newsId);
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
            news = null;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after retrieving news", e);
                    news = null;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after retrieving news", e);
                    news = null;
                }
            }

            return news;
        }
    }


    public boolean update(News news) {
        logger.info("Updating news..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getShortText());
            preparedStatement.setString(3, news.getFullText());
            preparedStatement.setDate(4, news.getModificationDate());
            preparedStatement.setInt(5, news.getNewsId());
            preparedStatement.executeUpdate();
            logger.info("Successfully updated news");
        }
        catch (SQLException e) {
            logger.error("Error while updating news: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after updating news", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after updating news", e);
                    result = false;
                }
            }

            return result;
        }
    }


    public boolean delete(News news) {
        logger.info("Deleting news..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setInt(1, news.getNewsId());
            preparedStatement.executeUpdate();
            logger.info("Successfully deleted news");
        }
        catch (SQLException e) {
            logger.error("Error while deleting news: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after deleting news", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after deleting news", e);
                    result = false;
                }
            }

            return result;
        }
    }


    public List<News> findAllSorted() {
        logger.info("Retrieving all news..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<News> allNews = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND_ALL_SORTED);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            allNews = new LinkedList<News>();
            while (resultSet.next()) {
                News news = new News();
                news.setNewsId(resultSet.getInt(1));
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
            allNews = null;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after all retrieving news", e);
                    allNews = null;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after all retrieving news", e);
                    allNews = null;
                }
            }

            return allNews;
        }
    }


    public int countAll() {
        logger.info("Retrieving news..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int count = -1;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(COUNT_ALL_NEWS);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
            logger.info("Successfully retrieved news count");
        }
        catch (SQLException e) {
            logger.error("Error while retrieving news count: ", e);
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after retrieving news count", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after retrieving news count", e);
                }
            }

            return count;
        }
    }
}
