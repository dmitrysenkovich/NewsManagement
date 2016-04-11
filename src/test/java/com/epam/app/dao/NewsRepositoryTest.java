package com.epam.app.dao;

import com.epam.app.model.News;
import com.epam.app.utils.SearchCriteria;
import com.epam.app.utils.SearchUtils;
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
import java.sql.Date;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * News repository test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/repository-test-context.xml"})
@DatabaseSetup("/META-INF/test-data.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@PropertySource("classpath:properties/test-database.properties")
@Transactional(TransactionMode.ROLLBACK)
public class NewsRepositoryTest {
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private SearchUtils searchUtils;

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
    public void newsAdded() throws Exception {
        News news = new News();
        news.setTitle("test");
        news.setShortText("test");
        news.setFullText("test");
        news.setModificationDate(new Date(new java.util.Date().getTime()));
        news = newsRepository.add(news);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTable = actualDataSet.getTable("News");

        assertEquals(4, newsTable.getRowCount());
        assertNotNull(news.getNewsId());
    }

    @Test
    public void newsNotAdded() throws Exception {
        News news = new News();
        news = newsRepository.add(news);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTable = actualDataSet.getTable("News");

        assertEquals(3, newsTable.getRowCount());
        assertNull(news.getNewsId());
    }

    @Test
    public void newsFound() {
        News news = newsRepository.find(1L);

        assertNotNull(news);
    }

    @Test
    public void newsNotFound() {
        News news = newsRepository.find(-1L);

        assertNull(news);
    }

    @Test
    public void newsUpdated() {
        News news = new News();
        news.setNewsId(1L);
        news.setTitle("test1");
        news.setShortText("test1");
        news.setFullText("test1");
        news.setModificationDate(new Date(new java.util.Date().getTime()));
        boolean updated = newsRepository.update(news);
        News foundNews = newsRepository.find(news.getNewsId());

        assertEquals("test1", foundNews.getTitle());
        assertEquals("test1", foundNews.getShortText());
        assertEquals("test1", foundNews.getFullText());
        assertTrue(updated);
    }

    @Test
    public void newsNotUpdated() {
        News news = new News();
        news.setNewsId(1L);
        news.setTitle(null);
        boolean updated = newsRepository.update(news);
        News foundNews = newsRepository.find(news.getNewsId());

        assertEquals("title1", foundNews.getTitle());
        assertFalse(updated);
    }

    @Test
    public void newsDeleted() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        boolean deleted = newsRepository.delete(news);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTable = actualDataSet.getTable("News");
        ITable newsAuthorTable = actualDataSet.getTable("News_Author");
        ITable newsTagTable = actualDataSet.getTable("News_Tag");
        ITable commentTable = actualDataSet.getTable("Comments");

        assertEquals(2, newsTable.getRowCount());
        assertEquals(2, newsAuthorTable.getRowCount());
        assertEquals(3, newsTagTable.getRowCount());
        assertEquals(3, commentTable.getRowCount());
        assertTrue(deleted);
    }

    @Test
    public void newsNotDeleted() throws Exception {
        News news = new News();
        news.setNewsId(-1L);
        newsRepository.delete(news);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTable = actualDataSet.getTable("News");
        ITable newsAuthorTable = actualDataSet.getTable("News_Author");
        ITable newsTagTable = actualDataSet.getTable("News_Tag");
        ITable commentTable = actualDataSet.getTable("Comments");

        assertEquals(3, newsTable.getRowCount());
        assertEquals(3, newsAuthorTable.getRowCount());
        assertEquals(5, newsTagTable.getRowCount());
        assertEquals(5, commentTable.getRowCount());
    }

    @Test
    public void searchNothingFoundInvalidSearchQuery() {
        String searchQuery = "test";
        List<News> foundNews = newsRepository.search(searchQuery);

        assertNull(foundNews);
    }

    @Test
    public void searchNothingFoundSearchQueryIsNull() {
        String searchQuery = null;
        List<News> foundNews = newsRepository.search(searchQuery);

        assertNull(foundNews);
    }

    @Test
    public void searchSuccessfulNoTags() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setAuthorId(2L);
        String searchQuery = searchUtils.getSearchQuery(searchCriteria);
        List<News> foundNews = newsRepository.search(searchQuery);

        assertEquals(1L, foundNews.size());
        assertEquals("title2", foundNews.get(0).getTitle());
    }

    @Test
    public void searchSuccessfulNoAuthor() {
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> tagIds = new LinkedList<Long>();
        tagIds.add(1L);
        searchCriteria.setTagIds(tagIds);
        String searchQuery = searchUtils.getSearchQuery(searchCriteria);
        List<News> foundNews = newsRepository.search(searchQuery);

        assertEquals(3L, foundNews.size());
        assertEquals("title3", foundNews.get(0).getTitle());
        assertEquals("title1", foundNews.get(1).getTitle());
        assertEquals("title2", foundNews.get(2).getTitle());
    }

    @Test
    public void searchSuccessful() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setAuthorId(1L);
        List<Long> tagIds = new LinkedList<Long>();
        tagIds.add(2L);
        searchCriteria.setTagIds(tagIds);
        String searchQuery = searchUtils.getSearchQuery(searchCriteria);
        List<News> foundNews = newsRepository.search(searchQuery);

        assertEquals(2L, foundNews.size());
        assertEquals("title3", foundNews.get(0).getTitle());
        assertEquals("title1", foundNews.get(1).getTitle());
    }

    @Test
    public void searchNothingFound() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setAuthorId(3L);
        String searchQuery = searchUtils.getSearchQuery(searchCriteria);
        List<News> foundNews = newsRepository.search(searchQuery);

        assertTrue(foundNews.isEmpty());
    }

    @Test
    public void foundAllNewsSorted() {
        List<News> foundNews = newsRepository.findAllSorted();

        assertEquals(3L, foundNews.size());
        assertEquals("title3", foundNews.get(0).getTitle());
        assertEquals("title1", foundNews.get(1).getTitle());
        assertEquals("title2", foundNews.get(2).getTitle());
    }

    @Test
    public void countedAllNews() {
        Long newsCount = newsRepository.countAll();

        assertEquals((Long) 3L, newsCount);
    }
}
