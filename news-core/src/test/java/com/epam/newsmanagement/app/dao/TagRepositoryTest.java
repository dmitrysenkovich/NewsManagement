package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;
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
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tag repository test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/repository-test-context.xml"})
@DatabaseSetup("/META-INF/test-data.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@PropertySource("classpath:properties/test-database.properties")
@Transactional(TransactionMode.ROLLBACK)
public class TagRepositoryTest {
    @Autowired
    private TagRepository tagRepository;

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
    public void tagIsAdded() throws Exception {
        Tag tag = new Tag();
        tag.setTagName("test");
        tag = tagRepository.save(tag);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("TAGS");

        assertEquals(4, tagsTable.getRowCount());
        assertNotNull(tag.getTagId());
    }


    @Test
    public void tagIsNotAdded() throws Exception {
        Tag tag = new Tag();
        catchException(() -> tagRepository.save(tag));
        assert caughtException() instanceof DataAccessException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("TAGS");

        assertEquals(3, tagsTable.getRowCount());
    }


    @Test
    public void tagIsFound() throws Exception {
        Tag tag = tagRepository.findOne(1L);

        assertNotNull(tag);
    }


    @Test
    public void tagIsNotFound() throws Exception {
        Tag tag = tagRepository.findOne(-1L);

        assertNull(tag);
    }


    @Test
    public void tagIsUpdated() throws Exception {
        Tag tag = new Tag();
        tag.setTagId(1L);
        tag.setTagName("test1");
        tagRepository.save(tag);
        Tag foundTag = tagRepository.findOne(tag.getTagId());

        assertEquals("test1", foundTag.getTagName());
    }


    @Test
    public void tagIsNotUpdated() throws Exception {
        Tag tag = new Tag();
        tag.setTagId(1L);
        tag.setTagName(null);
        catchException(() -> tagRepository.save(tag));
        assert caughtException() instanceof DataAccessException;
        Tag foundTag = tagRepository.findOne(tag.getTagId());

        assertEquals("test", foundTag.getTagName());
    }


    @Test
    public void tagIsDeleted() throws Exception {
        Tag tag = new Tag();
        tag.setTagId(1L);
        tagRepository.delete(tag);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("TAGS");
        ITable newsTagTable = actualDataSet.getTable("NEWS_TAG");

        assertEquals(2, tagsTable.getRowCount());
        assertEquals(2, newsTagTable.getRowCount());
    }


    @Test(expected = DataAccessException.class)
    public void tagIsNotDeleted() throws Exception {
        Tag tag = new Tag();
        tag.setTagId(-1L);
        tagRepository.delete(tag);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("TAGS");
        ITable newsTagTable = actualDataSet.getTable("NEWS_TAG");

        assertEquals(3, tagsTable.getRowCount());
        assertEquals(5, newsTagTable.getRowCount());
    }


    @Test
    public void foundAlLByNews() throws Exception {
        News news = new News();
        news.setNewsId(3L);
        List<Tag> allByNews = tagRepository.findAllByNews(news);

        assertEquals(2L, allByNews.size());
    }


    @Test
    public void didNotFindAllByNews() throws Exception {
        News news = new News();
        news.setNewsId(4L);
        List<Tag> allByNews = tagRepository.findAllByNews(news);

        assertEquals(0L, allByNews.size());
    }


    @Test
    public void gotAll() throws Exception {
        List<Tag> allTags = tagRepository.findAll();

        assertEquals(3L, allTags.size());
    }


    @Test
    public void exists() throws Exception {
        Tag tag = new Tag();
        tag.setTagName("test");
        boolean exists = tagRepository.exists(tag.getTagName());

        assertTrue(exists);
    }


    @Test
    public void notExists() throws Exception {
        Tag tag = new Tag();
        tag.setTagName("test1");
        boolean exists = tagRepository.exists(tag.getTagName());

        assertFalse(exists);
    }
}
