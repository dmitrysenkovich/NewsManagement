package com.epam.app.dao;

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

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
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
        tag = tagRepository.add(tag);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("Tags");

        assertEquals(3, tagsTable.getRowCount());
        assertNotNull(tag.getTagId());
    }

    @Test
    public void tagNotAdded() throws Exception {
        Tag tag = new Tag();
        tag = tagRepository.add(tag);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("Tags");

        assertEquals(2, tagsTable.getRowCount());
        assertNull(tag.getTagId());
    }

    @Test
    public void tagFound() {
        Tag tag = tagRepository.find(1L);

        assertNotNull(tag);
    }

    @Test
    public void tagNotFound() {
        Tag tag = tagRepository.find(-1L);

        assertNull(tag);
    }

    @Test
    public void tagUpdated() {
        Tag tag = new Tag();
        tag.setTagId(1L);
        tag.setTagName("test1");
        boolean updated = tagRepository.update(tag);
        Tag foundTag = tagRepository.find(tag.getTagId());

        assertEquals("test1", foundTag.getTagName());
        assertTrue(updated);
    }

    @Test
    public void tagNotUpdated() {
        Tag tag = new Tag();
        tag.setTagId(1L);
        tag.setTagName(null);
        boolean updated = tagRepository.update(tag);
        Tag foundTag = tagRepository.find(tag.getTagId());

        assertEquals("test", foundTag.getTagName());
        assertFalse(updated);
    }

    @Test
    public void tagDeleted() throws Exception {
        Tag tag = new Tag();
        tag.setTagId(1L);
        boolean deleted = tagRepository.delete(tag);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("Tags");
        ITable newsTagTable = actualDataSet.getTable("News_Tag");

        assertEquals(1, tagsTable.getRowCount());
        assertEquals(2, newsTagTable.getRowCount());
        assertTrue(deleted);
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
        List<Tag> tags = new LinkedList<Tag>();
        tags.add(tag1);
        tags.add(tag2);
        tags = tagRepository.addAll(tags);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("Tags");

        assertEquals(4, tagsTable.getRowCount());
        for (Tag tag : tags)
            assertNotNull(tag.getTagId());
    }

    @Test
    public void notAllTagsAdded() throws Exception {
        Tag tag1 = new Tag();
        tag1.setTagName("test");
        Tag tag2 = new Tag();
        List<Tag> tags = new LinkedList<Tag>();
        tags.add(tag1);
        tags.add(tag2);
        tags = tagRepository.addAll(tags);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("Tags");

        assertEquals(2, tagsTable.getRowCount());
        for (Tag tag : tags)
            assertNull(tag.getTagId());
    }

    @Test
    public void allTagsNotAdded() throws Exception {
        Tag tag1 = new Tag();
        Tag tag2 = new Tag();
        List<Tag> tags = new LinkedList<Tag>();
        tags.add(tag1);
        tags.add(tag2);
        tags = tagRepository.addAll(tags);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable tagsTable = actualDataSet.getTable("Tags");

        assertEquals(2, tagsTable.getRowCount());
        for (Tag tag : tags)
            assertNull(tag.getTagId());
    }
}
