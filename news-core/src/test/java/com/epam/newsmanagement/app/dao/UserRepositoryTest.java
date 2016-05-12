package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.User;
import com.epam.newsmanagement.app.dao.UserRepository;
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
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;

import java.sql.Connection;
import java.sql.DriverManager;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    public void userAdded() throws Exception {
        User user = new User();
        user.setRoleId(1L);
        user.setUserName("test");
        user.setLogin("test");
        user.setPassword("test");
        Long userId = userRepository.add(user);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable usersTable = actualDataSet.getTable("USERS");

        assertEquals(2, usersTable.getRowCount());
        assertNotNull(userId);
    }


    @Test
    public void userNotAddedInvalidField() throws Exception {
        User user = new User();
        user.setRoleId(1L);
        catchException(() -> userRepository.add(user));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable usersTable = actualDataSet.getTable("USERS");

        assertEquals(1, usersTable.getRowCount());
    }


    @Test
    public void userNotAddedRoleIsInvalid() throws Exception {
        User user = new User();
        user.setRoleId(-1L);
        user.setUserName("test");
        user.setLogin("test");
        user.setPassword("test");
        catchException(() -> userRepository.add(user));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable usersTable = actualDataSet.getTable("USERS");

        assertEquals(1, usersTable.getRowCount());
    }


    @Test
    public void userFound() throws Exception {
        User user = userRepository.find(1L);

        assertNotNull(user);
    }


    @Test(expected = DaoException.class)
    public void userNotFound() throws Exception {
        userRepository.find(-1L);
    }


    @Test
    public void userUpdated() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setRoleId(2L);
        user.setUserName("test1");
        user.setLogin("test1");
        user.setPassword("test1");
        userRepository.update(user);
        User foundUser = userRepository.find(user.getUserId());

        assertEquals((Long) 2L, foundUser.getRoleId());
        assertEquals("test1", foundUser.getUserName());
        assertEquals("test1", foundUser.getLogin());
        assertEquals("test1", foundUser.getPassword());
    }


    @Test
    public void userNotUpdatedInvalidField() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setRoleId(1L);
        catchException(() -> userRepository.update(user));
        assert caughtException() instanceof DaoException;
        User foundUser = userRepository.find(user.getUserId());

        assertEquals("test", foundUser.getUserName());
    }


    @Test
    public void userNotUpdatedRoleIsInvalid() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setRoleId(-1L);
        user.setUserName("test");
        user.setLogin("test");
        user.setPassword("test");
        catchException(() -> userRepository.update(user));
        assert caughtException() instanceof DaoException;
        User foundUser = userRepository.find(user.getUserId());

        assertEquals((Long) 1L, foundUser.getRoleId());
    }


    @Test
    public void userDeleted() throws Exception {
        User user = new User();
        user.setUserId(1L);
        userRepository.delete(user);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable usersTable = actualDataSet.getTable("USERS");

        assertEquals(0, usersTable.getRowCount());
    }


    @Test
    public void userNotDeleted() throws Exception {
        User user = new User();
        user.setUserId(-1L);
        userRepository.delete(user);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable usersTable = actualDataSet.getTable("USERS");

        assertEquals(1, usersTable.getRowCount());
    }
}
