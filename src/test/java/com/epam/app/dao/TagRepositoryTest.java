package com.epam.app.dao;

import com.epam.app.exception.DaoException;
import com.epam.app.model.Tag;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.dbunit.DefaultDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.junit.After;
import org.junit.Assert;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    public void tagAdded() throws Exception {
        Tag tag = new Tag();
        tag.setTagName("test");
        Long tagId = tagRepository.add(tag);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("Tags");

        assertEquals(3, tagsTable.getRowCount());
        assertNotNull(tagId);
    }


    @Test
    public void tagNotAdded() throws Exception {
        Tag tag = new Tag();
        catchException(() -> tagRepository.add(tag));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("Tags");

        assertEquals(2, tagsTable.getRowCount());
        assertNull(tag.getTagId());
    }


    @Test
    public void tagFound() throws Exception {
        Tag tag = tagRepository.find(1L);

        assertNotNull(tag);
    }


    @Test(expected = DaoException.class)
    public void tagNotFound() throws Exception {
        tagRepository.find(-1L);
    }


    @Test
    public void tagUpdated() throws Exception {
        Tag tag = new Tag();
        tag.setTagId(1L);
        tag.setTagName("test1");
        tagRepository.update(tag);
        Tag foundTag = tagRepository.find(tag.getTagId());

        assertEquals("test1", foundTag.getTagName());
    }


    @Test
    public void tagNotUpdated() throws Exception {
        Tag tag = new Tag();
        tag.setTagId(1L);
        tag.setTagName(null);
        catchException(() -> tagRepository.update(tag));
        assert caughtException() instanceof DaoException;
        Tag foundTag = tagRepository.find(tag.getTagId());

        assertEquals("test", foundTag.getTagName());
    }


    @Test
    public void tagDeleted() throws Exception {
        Tag tag = new Tag();
        tag.setTagId(1L);
        tagRepository.delete(tag);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("Tags");
        ITable newsTagTable = actualDataSet.getTable("News_Tag");

        assertEquals(1, tagsTable.getRowCount());
        assertEquals(2, newsTagTable.getRowCount());
    }


    @Test
    public void tagNotDeleted() throws Exception {
        Tag tag = new Tag();
        tag.setTagId(-1L);
        tagRepository.delete(tag);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("Tags");
        ITable newsTagTable = actualDataSet.getTable("News_Tag");

        assertEquals(2, tagsTable.getRowCount());
        assertEquals(5, newsTagTable.getRowCount());
    }


    @Test
    public void allTagsAdded() throws Exception {
        Tag tag1 = new Tag();
        tag1.setTagName("test");
        Tag tag2 = new Tag();
        tag2.setTagName("test");
        List<Tag> tags = new LinkedList<>();
        tags.add(tag1);
        tags.add(tag2);
        List<Long> tagsIds = tagRepository.addAll(tags);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("Tags");

        assertEquals(4, tagsTable.getRowCount());
        tagsIds.forEach(Assert::assertNotNull);
    }


    @Test
    public void notAllTagsAdded() throws Exception {
        Tag tag1 = new Tag();
        tag1.setTagName("test");
        Tag tag2 = new Tag();
        List<Tag> tags = new LinkedList<>();
        tags.add(tag1);
        tags.add(tag2);
        catchException(() -> tagRepository.addAll(tags));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("Tags");

        assertEquals(3, tagsTable.getRowCount());
        for (Tag tag : tags)
            assertNull(tag.getTagId());
    }


    @Test
    public void allTagsNotAdded() throws Exception {
        Tag tag1 = new Tag();
        Tag tag2 = new Tag();
        List<Tag> tags = new LinkedList<>();
        tags.add(tag1);
        tags.add(tag2);
        catchException(() -> tagRepository.addAll(tags));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("Tags");

        assertEquals(2, tagsTable.getRowCount());
        for (Tag tag : tags)
            assertNull(tag.getTagId());
    }
}
