package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.exception.NotImplementedException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void authorIsAdded() throws Exception {
        Author author = new Author();
        author.setAuthorName("test");
        author = authorRepository.save(author);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable authorsTable = actualDataSet.getTable("AUTHORS");

        assertEquals(3, authorsTable.getRowCount());
        assertNotNull(author.getAuthorId());
    }


    @Test
    public void authorIsNotAdded() throws Exception {
        Author author = new Author();
        catchException(() -> authorRepository.save(author));
        assert caughtException() instanceof DataAccessException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable authorsTable = actualDataSet.getTable("AUTHORS");

        assertEquals(2, authorsTable.getRowCount());
    }


    @Test
    public void authorIsFound() throws Exception {
        Author author = authorRepository.findOne(1L);

        assertNotNull(author);
    }


    @Test
    public void authorIsNotFound() throws Exception {
        Author author = authorRepository.findOne(-1L);

        assertNull(author);
    }


    @Test
    public void authorIsUpdated() throws Exception {
        Author author = new Author();
        author.setAuthorId(1L);
        author.setAuthorName("test1");
        authorRepository.save(author);
        Author foundAuthor = authorRepository.findOne(author.getAuthorId());

        assertEquals("test1", foundAuthor.getAuthorName());
    }


    @Test
    public void authorIsNotUpdated() throws Exception {
        Author author = new Author();
        author.setAuthorId(1L);
        author.setAuthorName(null);
        catchException(() -> authorRepository.save(author));
        assert caughtException() instanceof DataAccessException;
        Author foundAuthor = authorRepository.findOne(author.getAuthorId());

        assertEquals("test", foundAuthor.getAuthorName());
    }


    @Test(expected = NotImplementedException.class)
    public void validAuthorIsNotDeleted() throws Exception {
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


    @Test(expected = NotImplementedException.class)
    public void invalidAuthorIsNotDeleted() throws Exception {
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
        author.setAuthorName("test");
        author.setExpired(currentDate);
        authorRepository.save(author);
        Author foundAuthor = authorRepository.findOne(author.getAuthorId());

        assertEquals(currentDate.getDate(), foundAuthor.getExpired().getDate());
    }


    @Test
    public void foundAllAuthorsByNews() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        List<Author> authors = authorRepository.findAllByNews(news);
        assertEquals(1L, authors.size());
    }


    @Test
    public void didNotFindAuthorsByNonExistentNews() throws Exception {
        News news = new News();
        news.setNewsId(4L);
        List<Author> authors = authorRepository.findAllByNews(news);
        assertEquals(0L, authors.size());
    }


    @Test
    public void noNonExpiredAuthors() throws Exception {
        List<Author> authors = authorRepository.findNotExpired();
        assertEquals(0L, authors.size());
    }


    @Test
    public void foundAllAuthors() throws Exception {
        List<Author> allAuthors = authorRepository.findAll();
        assertEquals(2L, allAuthors.size());
    }


    @Test
    public void authorExists() throws Exception {
        Author author = new Author();
        author.setAuthorName("test");
        boolean exists = authorRepository.exists(author.getAuthorName());

        assertTrue(exists);
    }


    @Test
    public void authorDoesNotExist() throws Exception {
        Author author = new Author();
        author.setAuthorName("test1");
        boolean exists = authorRepository.exists(author.getAuthorName());

        assertFalse(exists);
    }
}
