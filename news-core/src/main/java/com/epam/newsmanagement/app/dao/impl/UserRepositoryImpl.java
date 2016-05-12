package com.epam.newsmanagement.app.dao.impl;

import com.epam.newsmanagement.app.dao.UserRepository;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.User;
import com.epam.newsmanagement.app.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User repository implementation.
 */
public class UserRepositoryImpl implements UserRepository {
    private static final String ADD = "INSERT INTO USERS(ROLE_ID, USER_NAME, LOGIN, PASSWORD) VALUES(?, ?, ?, ?)";
    private static final String FIND = "SELECT USER_ID, ROLE_ID, USER_NAME, LOGIN, " +
            "PASSWORD FROM USERS WHERE USER_ID = ?";
    private static final String UPDATE = "UPDATE USERS SET ROLE_ID = ?, USER_NAME = ?, LOGIN = ?, PASSWORD = ? " +
            "WHERE USER_ID = ?";
    private static final String DELETE = "DELETE FROM USERS WHERE USER_ID = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public Long add(User user) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Long userId = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, new String[]{ "USER_ID" });
            preparedStatement.setLong(1, user.getRoleId());
            preparedStatement.setString(2, user.getUserName());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            userId = resultSet.getLong(1);
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return userId;
    }


    @Override
    public User find(Long userId) throws DaoException {
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
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return user;
    }


    @Override
    public void update(User user) throws DaoException {
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
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
    }


    @Override
    public void delete(User user) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, user.getUserId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
    }
}
