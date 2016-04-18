package com.epam.app.dao;

import com.epam.app.exception.DaoException;
import com.epam.app.model.News;
import com.epam.app.model.NewsTag;
import com.epam.app.model.Tag;
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
import java.util.LinkedList;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;

/**
 * NewsTag repository test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/repository-test-context.xml"})
@DatabaseSetup("/META-INF/test-data.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@PropertySource("classpath:properties/test-database.properties")
@Transactional(TransactionMode.ROLLBACK)
public class NewsTagRepositoryTest {
    @Autowired
    private NewsTagRepository newsTagRepository;

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
    public void newsTagRelationAdded() throws Exception {
        NewsTag newsTag = new NewsTag();
        newsTag.setNewsId(2L);
        newsTag.setTagId(2L);
        newsTagRepository.add(newsTag);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTagTable = actualDataSet.getTable("NEWS_TAG");

        assertEquals(6, newsTagTable.getRowCount());
    }


    @Test
    public void newsTagRelationNotAddedInvalidNews() throws Exception {
        NewsTag newsTag = new NewsTag();
        newsTag.setNewsId(-1L);
        newsTag.setTagId(2L);
        catchException(() -> newsTagRepository.add(newsTag));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTagTable = actualDataSet.getTable("NEWS_TAG");

        assertEquals(5, newsTagTable.getRowCount());
    }


    @Test
    public void newsTagRelationNotAddedInvalidTag() throws Exception {
        NewsTag newsTag = new NewsTag();
        newsTag.setNewsId(2L);
        newsTag.setTagId(-1L);
        catchException(() -> newsTagRepository.add(newsTag));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTagTable = actualDataSet.getTable("NEWS_TAG");

        assertEquals(5, newsTagTable.getRowCount());
    }


    @Test
    public void newsTagRelationDeleted() throws Exception {
        NewsTag newsTag = new NewsTag();
        newsTag.setNewsId(1L);
        newsTag.setTagId(1L);
        newsTagRepository.delete(newsTag);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTagTable = actualDataSet.getTable("NEWS_TAG");

        assertEquals(4, newsTagTable.getRowCount());
    }


    @Test
    public void newsTagRelationNotDeletedInvalidNews() throws Exception {
        NewsTag newsTag = new NewsTag();
        newsTag.setNewsId(-1L);
        newsTag.setTagId(2L);
        newsTagRepository.delete(newsTag);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTagTable = actualDataSet.getTable("NEWS_TAG");

        assertEquals(5, newsTagTable.getRowCount());
    }


    @Test
    public void newsTagRelationNotDeletedInvalidTag() throws Exception {
        NewsTag newsTag = new NewsTag();
        newsTag.setNewsId(2L);
        newsTag.setTagId(-1L);
        newsTagRepository.delete(newsTag);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTagTable = actualDataSet.getTable("NEWS_TAG");

        assertEquals(5, newsTagTable.getRowCount());
    }


    @Test
    public void newsTagRelationsAdded() throws Exception {
        News news = new News();
        news.setNewsId(2L);
        List<Tag> tags = new LinkedList<>();
        Tag tag1 = new Tag();
        tag1.setTagId(2L);
        Tag tag2 = new Tag();
        tag2.setTagId(3L);
        tags.add(tag1);
        tags.add(tag2);
        newsTagRepository.addAll(news, tags);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTagTable = actualDataSet.getTable("NEWS_TAG");

        assertEquals(7, newsTagTable.getRowCount());
    }


    @Test
    public void newsTagRelationsNotAllAdded() throws Exception {
        News news = new News();
        news.setNewsId(2L);
        List<Tag> tags = new LinkedList<>();
        Tag tag1 = new Tag();
        tag1.setTagId(2L);
        Tag tag2 = new Tag();
        tag2.setTagId(-1L);
        tags.add(tag1);
        tags.add(tag2);
        catchException(() -> newsTagRepository.addAll(news, tags));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTagTable = actualDataSet.getTable("NEWS_TAG");

        assertEquals(6, newsTagTable.getRowCount());
    }


    @Test
    public void newsTagRelationsNotAddedInvalidTags() throws Exception {
        News news = new News();
        news.setNewsId(2L);
        List<Tag> tags = new LinkedList<>();
        Tag tag1 = new Tag();
        tag1.setTagId(-1L);
        Tag tag2 = new Tag();
        tag2.setTagId(-1L);
        tags.add(tag1);
        tags.add(tag2);
        catchException(() -> newsTagRepository.addAll(news, tags));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTagTable = actualDataSet.getTable("NEWS_TAG");

        assertEquals(5, newsTagTable.getRowCount());
    }


    @Test
    public void newsTagRelationsNotAddedInvalidNews() throws Exception {
        News news = new News();
        news.setNewsId(-1L);
        List<Tag> tags = new LinkedList<>();
        Tag tag1 = new Tag();
        tag1.setTagId(2L);
        Tag tag2 = new Tag();
        tag2.setTagId(3L);
        tags.add(tag1);
        tags.add(tag2);
        catchException(() -> newsTagRepository.addAll(news, tags));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTagTable = actualDataSet.getTable("NEWS_TAG");

        assertEquals(5, newsTagTable.getRowCount());
    }
}
