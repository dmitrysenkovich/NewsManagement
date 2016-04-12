package com.epam.app.dao;

import com.epam.app.exception.DaoException;
import com.epam.app.model.Author;
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
import static org.junit.Assert.assertNull;

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
        ITable authorsTable = actualDataSet.getTable("Authors");

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
        ITable authorsTable = actualDataSet.getTable("Authors");

        assertEquals(2, authorsTable.getRowCount());
        assertNull(author.getAuthorId());
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


    @Test
    public void authorDeleted() throws Exception {
        Author author = new Author();
        author.setAuthorId(1L);
        authorRepository.delete(author);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable authorsTable = actualDataSet.getTable("Authors");
        ITable newsAuthorsTable = actualDataSet.getTable("News_Author");

        assertEquals(1, authorsTable.getRowCount());
        assertEquals(1, newsAuthorsTable.getRowCount());
    }


    @Test
    public void authorNotDeleted() throws Exception {
        Author author = new Author();
        author.setAuthorId(-1L);
        authorRepository.delete(author);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable authorsTable = actualDataSet.getTable("Authors");
        ITable newsAuthorsTable = actualDataSet.getTable("News_Author");

        assertEquals(2, authorsTable.getRowCount());
        assertEquals(3, newsAuthorsTable.getRowCount());
    }
}
