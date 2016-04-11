package com.epam.app.dao;

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

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

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
        author = authorRepository.add(author);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable authorsTable = actualDataSet.getTable("Authors");

        assertEquals(3, authorsTable.getRowCount());
        assertNotNull(author.getAuthorId());
    }

    @Test
    public void authorNotAdded() throws Exception {
        Author author = new Author();
        author = authorRepository.add(author);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable authorsTable = actualDataSet.getTable("Authors");

        assertEquals(2, authorsTable.getRowCount());
        assertNull(author.getAuthorId());
    }

    @Test
    public void authorFound() {
        Author author = new Author();
        author.setAuthorName("test");
        author = authorRepository.add(author);
        author = authorRepository.find(author.getAuthorId());

        assertNotNull(author);
    }

    @Test
    public void authorNotFound() {
        Author author = authorRepository.find(-1L);

        assertNull(author);
    }

    @Test
    public void authorUpdated() {
        Author author = new Author();
        author.setAuthorId(1L);
        author.setAuthorName("test1");
        boolean updated = authorRepository.update(author);
        Author foundAuthor = authorRepository.find(author.getAuthorId());

        assertEquals("test1", foundAuthor.getAuthorName());
        assertTrue(updated);
    }

    @Test
    public void authorNotUpdated() {
        Author author = new Author();
        author.setAuthorId(1L);
        author.setAuthorName(null);
        boolean updated = authorRepository.update(author);
        Author foundAuthor = authorRepository.find(author.getAuthorId());

        assertEquals("test", foundAuthor.getAuthorName());
        assertFalse(updated);
    }

    @Test
    public void authorDeleted() throws Exception {
        Author author = new Author();
        author.setAuthorId(1L);
        boolean deleted = authorRepository.delete(author);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable authorsTable = actualDataSet.getTable("Authors");
        ITable newsAuthorsTable = actualDataSet.getTable("News_Author");

        assertEquals(1, authorsTable.getRowCount());
        assertEquals(1, newsAuthorsTable.getRowCount());
        assertTrue(deleted);
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
