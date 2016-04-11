package com.epam.app.dao;

import com.epam.app.model.NewsAuthor;
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

/**
 * NewsAuthor repository test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/repository-test-context.xml"})
@DatabaseSetup("/META-INF/test-data.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@PropertySource("classpath:properties/test-database.properties")
@Transactional(TransactionMode.ROLLBACK)
public class NewsAuthorRepositoryTest {
    @Autowired
    private NewsAuthorRepository newsAuthorRepository;

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
    public void newsAuthorRelationAdded() throws Exception {
        NewsAuthor newsAuthor = new NewsAuthor();
        newsAuthor.setNewsId(1L);
        newsAuthor.setAuthorId(2L);
        boolean added = newsAuthorRepository.add(newsAuthor);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsAuthorTable = actualDataSet.getTable("News_Author");

        assertEquals(4, newsAuthorTable.getRowCount());
        assertTrue(added);
    }

    @Test
    public void newsAuthorRelationNotAddedInvalidNews() throws Exception {
        NewsAuthor newsAuthor = new NewsAuthor();
        newsAuthor.setNewsId(-1L);
        newsAuthor.setAuthorId(2L);
        boolean added = newsAuthorRepository.add(newsAuthor);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsAuthorTable = actualDataSet.getTable("News_Author");

        assertEquals(3, newsAuthorTable.getRowCount());
        assertFalse(added);
    }

    @Test
    public void newsAuthorRelationNotAddedInvalidAuthor() throws Exception {
        NewsAuthor newsAuthor = new NewsAuthor();
        newsAuthor.setNewsId(2L);
        newsAuthor.setAuthorId(-1L);
        boolean added = newsAuthorRepository.add(newsAuthor);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsAuthorTable = actualDataSet.getTable("News_Author");

        assertEquals(3, newsAuthorTable.getRowCount());
        assertFalse(added);
    }

    @Test
    public void newsAuthorRelationDeleted() throws Exception {
        NewsAuthor newsAuthor = new NewsAuthor();
        newsAuthor.setNewsId(1L);
        newsAuthor.setAuthorId(1L);
        boolean deleted = newsAuthorRepository.delete(newsAuthor);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsAuthorTable = actualDataSet.getTable("News_Author");

        assertEquals(2, newsAuthorTable.getRowCount());
        assertTrue(deleted);
    }

    @Test
    public void newsAuthorRelationNotDeletedInvalidNews() throws Exception {
        NewsAuthor newsAuthor = new NewsAuthor();
        newsAuthor.setNewsId(-1L);
        newsAuthor.setAuthorId(2L);
        newsAuthorRepository.delete(newsAuthor);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsAuthorTable = actualDataSet.getTable("News_Author");

        assertEquals(3, newsAuthorTable.getRowCount());
    }

    @Test
    public void newsAuthorRelationNotDeletedInvalidAuthor() throws Exception {
        NewsAuthor newsAuthor = new NewsAuthor();
        newsAuthor.setNewsId(2L);
        newsAuthor.setAuthorId(-1L);
        newsAuthorRepository.delete(newsAuthor);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsAuthorTable = actualDataSet.getTable("News_Author");

        assertEquals(3, newsAuthorTable.getRowCount());
    }
}
