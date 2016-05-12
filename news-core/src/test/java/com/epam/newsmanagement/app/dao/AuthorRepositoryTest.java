package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.dao.AuthorRepository;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.dbunit.Assertion;
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
import java.sql.Timestamp;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Author repository test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/repository-test-context.xml"})
@DatabaseSetup("/META-INF/test-data.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@PropertySource("classpath:properties/test-database.properties")
@Transactional(TransactionMode.ROLLBACK)
public class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

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
    public void authorAdded() throws Exception {
        Author author = new Author();
        author.setAuthorName("test");
        Long authorId = authorRepository.add(author);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable authorsTable = actualDataSet.getTable("AUTHORS");

        assertEquals(3, authorsTable.getRowCount());
        assertNotNull(authorId);
    }


    @Test
    public void authorNotAdded() throws Exception {
        Author author = new Author();
        catchException(() -> authorRepository.add(author));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable authorsTable = actualDataSet.getTable("AUTHORS");

        assertEquals(2, authorsTable.getRowCount());
    }


    @Test
    public void authorFound() throws Exception {
        Author author = authorRepository.find(1L);

        assertNotNull(author);
    }


    @Test(expected = DaoException.class)
    public void authorNotFound() throws Exception {
        authorRepository.find(-1L);
    }


    @Test
    public void authorUpdated() throws Exception {
        Author author = new Author();
        author.setAuthorId(1L);
        author.setAuthorName("test1");
        authorRepository.update(author);
        Author foundAuthor = authorRepository.find(author.getAuthorId());

        assertEquals("test1", foundAuthor.getAuthorName());
    }


    @Test
    public void authorNotUpdated() throws Exception {
        Author author = new Author();
        author.setAuthorId(1L);
        author.setAuthorName(null);
        catchException(() -> authorRepository.update(author));
        assert caughtException() instanceof DaoException;
        Author foundAuthor = authorRepository.find(author.getAuthorId());

        assertEquals("test", foundAuthor.getAuthorName());
    }


    @Test(expected = DaoException.class)
    public void validAuthorNotDeleted() throws Exception {
        Author author = new Author();
        author.setAuthorId(1L);
        authorRepository.delete(author);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable authorsTable = actualDataSet.getTable("AUTHORS");
        ITable newsAuthorsTable = actualDataSet.getTable("NEWS_AUTHOR");

        assertEquals(2, authorsTable.getRowCount());
        assertEquals(3, newsAuthorsTable.getRowCount());
    }


    @Test(expected = DaoException.class)
    public void invalidAuthorNotDeleted() throws Exception {
        Author author = new Author();
        author.setAuthorId(-1L);
        authorRepository.delete(author);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable authorsTable = actualDataSet.getTable("AUTHORS");
        ITable newsAuthorsTable = actualDataSet.getTable("NEWS_AUTHOR");

        assertEquals(2, authorsTable.getRowCount());
        assertEquals(3, newsAuthorsTable.getRowCount());
    }


    @Test
    public void authorIsMadeExpired() throws Exception {
        Timestamp currentDate = new Timestamp(new java.util.Date().getTime());
        Author author = new Author();
        author.setAuthorId(1L);
        author.setExpired(currentDate);
        authorRepository.makeAuthorExpired(author);
        Author foundAuthor = authorRepository.find(author.getAuthorId());

        assertEquals(currentDate.getDate(), foundAuthor.getExpired().getDate());
    }


    @Test
    public void authorIsNotMadeExpired() throws Exception {
        Author author = new Author();
        author.setAuthorId(-1L);
        Timestamp currentDate = new Timestamp(new java.util.Date().getTime());
        author.setExpired(currentDate);

        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet oldDataSet = getActualDataSet(connection);
        ITable oldAuthorsTable = oldDataSet.getTable("AUTHORS");
        authorRepository.makeAuthorExpired(author);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable authorsTable = actualDataSet.getTable("AUTHORS");

        Assertion.assertEquals(oldAuthorsTable, authorsTable);
    }
}
