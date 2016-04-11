package com.epam.app.dao.impl;

import com.epam.app.dao.UserRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.model.User;
import com.epam.app.utils.DatabaseUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * User repository implementation.
 */
public class UserRepositoryImpl implements UserRepository {
    private static final Logger logger = Logger.getLogger(UserRepositoryImpl.class.getName());

    private static final String ADD = "INSERT INTO Users(role_id, user_name, login, password) VALUES(?, ?, ?, ?)";
    private static final String FIND = "SELECT * FROM Users WHERE user_id = ?";
    private static final String UPDATE = "UPDATE Users SET role_id = ?, user_name = ?, login = ?, password = ? " +
            "WHERE user_id = ?";
    private static final String DELETE = "DELETE FROM Users WHERE user_id = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    public User add(User user) throws DaoException {
        logger.info("Adding user..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, user.getRoleId());
            preparedStatement.setString(2, user.getUserName());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            Long userId = resultSet.getLong(1);
            user.setUserId(userId);
            logger.info("Successfully added user");
        }
        catch (SQLException e) {
            logger.error("Error while adding user: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while adding user: ",
                    preparedStatement, connection);
        }
        return user;
    }


    public User find(Long userId) throws DaoException {
        logger.info("Retrieving user..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        User user = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND);
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            user = new User();
            user.setUserId(userId);
            user.setRoleId(resultSet.getLong(2));
            user.setUserName(resultSet.getString(3));
            user.setLogin(resultSet.getString(4));
            user.setPassword(resultSet.getString(5));
            logger.info("Successfully retrieved user");
        }
        catch (SQLException e) {
            logger.error("Error while retrieving user: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while retrieving user: ",
                    preparedStatement, connection);
        }
        return user;
    }


    public void update(User user) throws DaoException {
        logger.info("Updating user..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setLong(1, user.getRoleId());
            preparedStatement.setString(2, user.getUserName());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setLong(5, user.getUserId());
            preparedStatement.executeUpdate();
            logger.info("Successfully updated user");
        }
        catch (SQLException e) {
            logger.error("Error while updating user: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while updating user: ",
                    preparedStatement, connection);
        }
    }


    public void delete(User user) throws DaoException {
        logger.info("Deleting user..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, user.getUserId());
            preparedStatement.executeUpdate();
            logger.info("Successfully deleted user");
        }
        catch (SQLException e) {
            logger.error("Error while deleting user: ", e);
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(logger, "Error while deleting user: ",
                    preparedStatement, connection);
        }
    }
}
