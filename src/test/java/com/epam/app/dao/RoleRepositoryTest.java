package com.epam.app.dao;

import com.epam.app.model.Role;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.dbunit.DefaultDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Role repository test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/repository-test-context.xml"})
@DatabaseSetup("/META-INF/test-data.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@PropertySource("classpath:properties/test-database.properties")
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    private Connection connection;

    @Value("${jdbc.url}")
    private String testDbUrl;
    @Value("${jdbc.username}")
    private String testDbUsername;
    @Value("${jdbc.password}")
    private String testDbPassword;

    private IDataSet getActualDataSet(Connection connection) throws Exception {
        return new DefaultDatabaseTester(
                new DatabaseConnection(connection)).getConnection().createDataSet();
    }

    @After
    public void destroy() throws Exception {
        if (connection != null)
            connection.close();
    }

    @Test
    public void roleAdded() throws Exception {
        Role role = new Role();
        role.setRoleName("test");
        role = roleRepository.add(role);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable rolesTable = actualDataSet.getTable("Roles");

        assertEquals(3, rolesTable.getRowCount());
        assertNotNull(role.getRoleId());
    }

    @Test
    public void roleNotAdded() throws Exception {
        Role role = new Role();
        role = roleRepository.add(role);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable rolesTable = actualDataSet.getTable("Roles");

        assertEquals(2, rolesTable.getRowCount());
        assertNull(role.getRoleId());
    }

    @Test
    public void roleFound() {
        Role role = roleRepository.find(1L);

        assertNotNull(role);
    }

    @Test
    public void roleNotFound() {
        Role role = roleRepository.find(-1L);

        assertNull(role);
    }

    @Test
    public void roleUpdated() {
        Role role = new Role();
        role.setRoleId(1L);
        role.setRoleName("test1");
        boolean updated = roleRepository.update(role);
        Role foundRole = roleRepository.find(role.getRoleId());

        assertEquals("test1", foundRole.getRoleName());
        assertTrue(updated);
    }

    @Test
    public void roleNotUpdated() {
        Role role = new Role();
        role.setRoleId(1L);
        role.setRoleName(null);
        boolean updated = roleRepository.update(role);
        Role foundRole = roleRepository.find(role.getRoleId());

        assertEquals("test", foundRole.getRoleName());
        assertFalse(updated);
    }

    @Test
    public void roleDeleted() throws Exception {
        Role role = new Role();
        role.setRoleId(1L);
        boolean deleted = roleRepository.delete(role);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable rolesTable = actualDataSet.getTable("Roles");
        ITable usersTable = actualDataSet.getTable("Users");

        assertEquals(1, rolesTable.getRowCount());
        assertEquals(0, usersTable.getRowCount());
        assertTrue(deleted);
    }

    @Test
    public void roleNotDeleted() throws Exception {
        Role role = new Role();
        role.setRoleId(-1L);
        roleRepository.delete(role);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable rolesTable = actualDataSet.getTable("Roles");
        ITable usersTable = actualDataSet.getTable("Users");

        assertEquals(2, rolesTable.getRowCount());
        assertEquals(1, usersTable.getRowCount());
    }
}
