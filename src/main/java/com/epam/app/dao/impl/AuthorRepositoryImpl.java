package com.epam.app.dao.impl;

import com.epam.app.dao.AuthorRepository;
import com.epam.app.model.Author;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Author repository implementation.
 */
public class AuthorRepositoryImpl implements AuthorRepository {
    private static final Logger logger = Logger.getLogger(AuthorRepositoryImpl.class.getName());

    private static final String ADD = "INSERT INTO Authors(author_name, expired) VALUES(?, ?)";
    private static final String FIND = "SELECT * FROM Authors WHERE author_id = ?";
    private static final String UPDATE = "UPDATE Authors SET author_name = ?, expired = ? " +
            "WHERE author_id = ?";
    private static final String DELETE = "DELETE FROM Authors WHERE author_id = ?";

    @Autowired
    private DataSource dataSource;


    public Author add(Author author) {
        logger.info("Adding author..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, author.getAuthorName());
            preparedStatement.setTimestamp(2, author.getExpired());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            Long authorId = resultSet.getLong(1);
            author.setAuthorId(authorId);
            logger.info("Successfully added author");
        }
        catch (SQLException e) {
            logger.error("Error while adding author: ", e);
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after adding author", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after adding author", e);
                }
            }

            return author;
        }
    }


    public Author find(Long authorId) {
        logger.info("Retrieving author..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Author author = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND);
            preparedStatement.setLong(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            author = new Author();
            author.setAuthorId(authorId);
            author.setAuthorName(resultSet.getString(2));
            author.setExpired(resultSet.getTimestamp(3));
            logger.info("Successfully retrieved author");
        }
        catch (SQLException e) {
            logger.error("Error while retrieving author: ", e);
            author = null;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after retrieving author", e);
                    author = null;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after retrieving author", e);
                    author = null;
                }
            }

            return author;
        }
    }


    public boolean update(Author author) {
        logger.info("Updating author..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, author.getAuthorName());
            preparedStatement.setTimestamp(2, author.getExpired());
            preparedStatement.setLong(3, author.getAuthorId());
            preparedStatement.executeUpdate();
            logger.info("Successfully updated author");
        }
        catch (SQLException e) {
            logger.error("Error while updating author: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after updating author", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after updating author", e);
                    result = false;
                }
            }

            return result;
        }
    }


    public boolean delete(Author author) {
        logger.info("Deleting author..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, author.getAuthorId());
            preparedStatement.executeUpdate();
            logger.info("Successfully deleted author");
        }
        catch (SQLException e) {
            logger.error("Error while deleting author: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after deleting author", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after deleting author", e);
                    result = false;
                }
            }

            return result;
        }
    }
}
