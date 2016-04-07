package com.epam.app.dao.impl;

import com.epam.app.dao.CrudRepository;
import com.epam.app.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;

/**
 * User repository implementation.
 */
public class UserRepositoryImpl implements CrudRepository<User> {
    private static final Logger logger = Logger.getLogger(UserRepositoryImpl.class.getName());

    private static final String ADD = "INSERT INTO User(role_id, user_name, login, password) VALUES(?, ?, ?, ?);";
    private static final String FIND = "SELECT * FROM User WHERE user_id = ?;";
    private static final String UPDATE = "UPDATE User SET role_id = ?, user_name = ?, login = ?, password = ? " +
            "WHERE user_id = ?;";
    private static final String DELETE = "DELETE FROM User WHERE user_id = ?";

    @Autowired
    private DataSource dataSource;


    public User add(User user) {
        logger.info("Adding user..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        User result = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, user.getRoleId());
            preparedStatement.setString(2, user.getUserName());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int userId = resultSet.getInt(1);
            user.setUserId(userId);
            result = user;
            logger.info("Successfully added user");
        }
        catch (SQLException e) {
            logger.error("Error while adding user: ", e);
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after adding user", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after adding user", e);
                }
            }

            return result;
        }
    }


    public User find(int userId) {
        logger.info("Retrieving user..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        User user = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            user = new User();
            user.setUserId(userId);
            user.setUserName(resultSet.getString(2));
            user.setLogin(resultSet.getString(3));
            user.setPassword(resultSet.getString(4));
            logger.info("Successfully retrieved user");
        }
        catch (SQLException e) {
            logger.error("Error while retrieving user: ", e);
            user = null;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after retrieving user", e);
                    user = null;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after retrieving user", e);
                    user = null;
                }
            }

            return user;
        }
    }


    public boolean update(User user) {
        logger.info("Updating user..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setInt(1, user.getRoleId());
            preparedStatement.setString(2, user.getUserName());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setInt(5, user.getUserId());
            preparedStatement.executeUpdate();
            logger.info("Successfully updated user");
        }
        catch (SQLException e) {
            logger.error("Error while updating user: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after updating user", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after updating user", e);
                    result = false;
                }
            }

            return result;
        }
    }


    public boolean delete(User user) {
        logger.info("Deleting user..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setInt(1, user.getUserId());
            preparedStatement.executeUpdate();
            logger.info("Successfully deleted user");
        }
        catch (SQLException e) {
            logger.error("Error while deleting user: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after deleting user", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after deleting user", e);
                    result = false;
                }
            }

            return result;
        }
    }
}
