package com.epam.newsmanagement.app.dao.jdbc.impl;

import com.epam.newsmanagement.app.dao.jdbc.RoleRepositoryJdbc;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.Role;
import com.epam.newsmanagement.app.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Role repository implementation.
 */
public class RoleRepositoryJdbcImpl implements RoleRepositoryJdbc {
    private static final String ADD = "INSERT INTO ROLES(ROLE_NAME) VALUES(?)";
    private static final String FIND = "SELECT ROLE_ID, ROLE_NAME FROM ROLES WHERE ROLE_ID = ?";
    private static final String UPDATE = "UPDATE ROLES SET ROLE_NAME = ? WHERE ROLE_ID = ?";
    private static final String DELETE = "DELETE FROM ROLES WHERE ROLE_ID = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public Role save(Role role) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            if (role.getRoleId() == null) {
                preparedStatement = connection.prepareStatement(ADD, new String[]{ "ROLE_ID" });
                preparedStatement.setString(1, role.getRoleName());
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                role.setRoleId(resultSet.getLong(1));
            }
            else {
                preparedStatement = connection.prepareStatement(UPDATE);
                preparedStatement.setString(1, role.getRoleName());
                preparedStatement.setLong(2, role.getRoleId());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return role;
    }


    @Override
    public Role findOne(Long roleId) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Role role = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(FIND);
            preparedStatement.setLong(1, roleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            role = new Role();
            role.setRoleId(roleId);
            role.setRoleName(resultSet.getString(2));
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return role;
    }


    @Override
    public void delete(Role role) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, role.getRoleId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DaoException("", e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
    }
}
