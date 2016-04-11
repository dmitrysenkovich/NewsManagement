package com.epam.app.dao.impl;

import com.epam.app.dao.RoleRepository;
import com.epam.app.model.Role;
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
    private static final Logger logger = Logger.getLogger(RoleRepositoryImpl.class.getName());

    private static final String ADD = "INSERT INTO Roles(role_name) VALUES(?)";
    private static final String FIND = "SELECT * FROM Roles WHERE role_id = ?";
    private static final String UPDATE = "UPDATE Roles SET role_name = ? WHERE role_id = ?";
    private static final String DELETE = "DELETE FROM Roles WHERE role_id = ?";

    @Autowired
    private DataSource dataSource;


    @Override
    public Role add(Role role) {
        logger.info("Adding role..");
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
            logger.info("Successfully added role");
        }
        catch (SQLException e) {
            logger.error("Error while adding role: ", e);
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after adding role", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after adding role", e);
                }
            }

            return role;
        }
    }


    @Override
    public Role find(Long roleId) {
        logger.info("Retrieving role..");
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
            logger.info("Successfully retrieved role");
        }
        catch (SQLException e) {
            logger.error("Error while retrieving role: ", e);
            role = null;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after retrieving role", e);
                    role = null;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after retrieving role", e);
                    role = null;
                }
            }

            return role;
        }
    }


    @Override
    public boolean update(Role role) {
        logger.info("Updating role..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, role.getRoleName());
            preparedStatement.setLong(2, role.getRoleId());
            preparedStatement.executeUpdate();
            logger.info("Successfully updated role");
        }
        catch (SQLException e) {
            logger.error("Error while updating role: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after updating role", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after updating role", e);
                    result = false;
                }
            }

            return result;
        }
    }


    @Override
    public boolean delete(Role role) {
        logger.info("Deleting role..");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, role.getRoleId());
            preparedStatement.executeUpdate();
            logger.info("Successfully deleted role");
        }
        catch (SQLException e) {
            logger.error("Error while deleting role: ", e);
            result = false;
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close prepared " +
                            "statement after deleting role", e);
                    result = false;
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error while trying to close connection " +
                            "after deleting role", e);
                    result = false;
                }
            }

            return result;
        }
    }
}
