package com.epam.app.dao.impl;

import com.epam.app.dao.RoleRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.model.Role;
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
 * Role repository implementation.
 */
public class RoleRepositoryImpl implements RoleRepository {
    private static final String ADD = "INSERT INTO Roles(role_name) VALUES(?)";
    private static final String FIND = "SELECT role_id, role_name FROM Roles WHERE role_id = ?";
    private static final String UPDATE = "UPDATE Roles SET role_name = ? WHERE role_id = ?";
    private static final String DELETE = "DELETE FROM Roles WHERE role_id = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseUtils databaseUtils;


    @Override
    public Role add(Role role) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(ADD, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, role.getRoleName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            Long roleId = resultSet.getLong(1);
            role.setRoleId(roleId);
        }
        catch (SQLException e) {
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return role;
    }


    @Override
    public Role find(Long roleId) throws DaoException {
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
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
        return role;
    }


    @Override
    public void update(Role role) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, role.getRoleName());
            preparedStatement.setLong(2, role.getRoleId());
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
            throw new DaoException(e);
        }
        finally {
            databaseUtils.closeConnectionAndStatement(preparedStatement, connection);
        }
    }
}
