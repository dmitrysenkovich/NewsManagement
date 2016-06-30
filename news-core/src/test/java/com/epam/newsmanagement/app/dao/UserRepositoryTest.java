package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.Role;
import com.epam.newsmanagement.app.model.User;
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
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;

import java.sql.Connection;
import java.sql.DriverManager;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * User repository test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/repository-test-context.xml"})
@DatabaseSetup("/META-INF/test-data.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@PropertySource("classpath:properties/test-database.properties")
@Transactional(TransactionMode.ROLLBACK)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

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
    public void userIsAdded() throws Exception {
        User user = new User();
        Role role = new Role();
        role.setRoleId(1L);
        user.setRole(role);
        user.setUserName("test");
        user.setLogin("test");
        user.setPassword("test");
        user = userRepository.save(user);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable usersTable = actualDataSet.getTable("USERS");

        assertEquals(2, usersTable.getRowCount());
        assertNotNull(user.getUserId());
    }


    @Test
    public void userIsNotAddedInvalidField() throws Exception {
        User user = new User();
        Role role = new Role();
        role.setRoleId(1L);
        user.setRole(role);
        catchException(() -> userRepository.save(user));
        assert caughtException() instanceof DataAccessException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable usersTable = actualDataSet.getTable("USERS");

        assertEquals(1, usersTable.getRowCount());
    }


    @Test
    public void userIsNotAddedRoleIsInvalid() throws Exception {
        User user = new User();
        Role role = new Role();
        role.setRoleId(-1L);
        user.setRole(role);
        user.setUserName("test");
        user.setLogin("test");
        user.setPassword("test");
        catchException(() -> userRepository.save(user));
        assert caughtException() instanceof DataAccessException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable usersTable = actualDataSet.getTable("USERS");

        assertEquals(1, usersTable.getRowCount());
    }


    @Test
    public void userIsFound() throws Exception {
        User user = userRepository.findOne(1L);

        assertNotNull(user);
    }


    @Test
    public void userIsNotFound() throws Exception {
        User user = userRepository.findOne(-1L);

        assertNull(user);
    }


    @Test
    public void userIsUpdated() throws Exception {
        User user = new User();
        user.setUserId(1L);
        Role role = new Role();
        role.setRoleId(2L);
        user.setRole(role);
        user.setUserName("test1");
        user.setLogin("test1");
        user.setPassword("test1");
        userRepository.save(user);
        User foundUser = userRepository.findOne(user.getUserId());

        assertEquals((Long) 2L, foundUser.getRole().getRoleId());
        assertEquals("test1", foundUser.getUserName());
        assertEquals("test1", foundUser.getLogin());
        assertEquals("test1", foundUser.getPassword());
    }


    @Test
    public void userIsNotUpdatedInvalidField() throws Exception {
        User user = new User();
        user.setUserId(1L);
        Role role = new Role();
        role.setRoleId(1L);
        user.setRole(role);
        catchException(() -> userRepository.save(user));
        assert caughtException() instanceof DataAccessException;
        User foundUser = userRepository.findOne(user.getUserId());

        assertEquals("test", foundUser.getUserName());
    }


    @Test
    public void userIsNotUpdatedRoleIsInvalid() throws Exception {
        User user = new User();
        user.setUserId(1L);
        Role role = new Role();
        role.setRoleId(-1L);
        user.setRole(role);
        user.setUserName("test");
        user.setLogin("test");
        user.setPassword("test");
        catchException(() -> userRepository.save(user));
        assert caughtException() instanceof DataAccessException;
        User foundUser = userRepository.findOne(user.getUserId());

        assertEquals((Long) 1L, foundUser.getRole().getRoleId());
    }


    @Test
    public void userIsDeleted() throws Exception {
        User user = new User();
        user.setUserId(1L);
        userRepository.delete(user);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable usersTable = actualDataSet.getTable("USERS");

        assertEquals(0, usersTable.getRowCount());
    }


    @Test(expected = JpaSystemException.class)
    public void userIsNotDeleted() throws Exception {
        User user = new User();
        user.setUserId(-1L);
        userRepository.delete(user);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable usersTable = actualDataSet.getTable("USERS");

        assertEquals(1, usersTable.getRowCount());
    }


    @Test
    public void gotUserNameByLogin() throws Exception {
        String login = "test";
        String userName = userRepository.userNameByLogin(login);

        assertEquals("test", userName);
    }


    @Test
    public void didNotGetUserNameByLogin() throws Exception {
        String login = "test1";
        String userName = userRepository.userNameByLogin(login);

        assertNull(userName);
    }
}
