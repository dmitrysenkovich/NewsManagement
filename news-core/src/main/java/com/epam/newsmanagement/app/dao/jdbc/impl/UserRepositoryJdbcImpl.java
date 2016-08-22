package com.epam.newsmanagement.app.dao.jdbc.impl;

import com.epam.newsmanagement.app.dao.jdbc.UserRepositoryJdbc;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.Role;
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
public class UserRepositoryJdbcImpl implements UserRepositoryJdbc {
    private static final String ADD = "INSERT INTO USERS(ROLE_ID, USER_NAME, LOGIN, PASSWORD) VALUES(?, ?, ?, ?)";
    private static final String FIND = "SELECT USER_ID, ROLE_ID, USER_NAME, LOGIN, " +
            "PASSWORD FROM USERS WHERE USER_ID = ?";
    private static final String UPDATE = "UPDATE USERS SET ROLE_ID = ?, USER_NAME = ?, LOGIN = ?, PASSWORD = ? " +
            "WHERE USER_ID = ?";
    private static final String DELETE = "DELETE FROM USERS WHERE USER_ID = ?";
    private static final String USER_NAME_BY_LOGIN = "SELECT USER_NAME FROM USERS WHERE LOGIN = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public User save(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            if (user.getUserId() == null) {
                preparedStatement = connection.prepareStatement(ADD, new String[]{ "USER_ID" });
                preparedStatement.setLong(1, user.getRole().getRoleId());
                preparedStatement.setString(2, user.getUserName());
                preparedStatement.setString(3, user.getLogin());
                preparedStatement.setString(4, user.getPassword());
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                user.setUserId(resultSet.getLong(1));
            }
            else {
                preparedStatement = connection.prepareStatement(UPDATE);
                preparedStatement.setLong(1, user.getRole().getRoleId());
                preparedStatement.setString(2, user.getUserName());
                preparedStatement.setString(3, user.getLogin());
                preparedStatement.setString(4, user.getPassword());
                preparedStatement.setLong(5, user.getUserId());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return user;
    }


    @Override
    public User findOne(Long userId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        User user = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND);
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setUserId(userId);
                Role role = new Role();
                role.setRoleId(resultSet.getLong(2));
                user.setRole(role);
                user.setUserName(resultSet.getString(3));
                user.setLogin(resultSet.getString(4));
                user.setPassword(resultSet.getString(5));
            }
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return user;
    }


    @Override
    public void delete(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, user.getUserId());
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
    public String userNameByLogin(String login) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String userName = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(USER_NAME_BY_LOGIN);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                userName = resultSet.getString(1);
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return userName;
    }
}
