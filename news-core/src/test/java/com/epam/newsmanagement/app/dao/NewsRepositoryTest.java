package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import com.epam.newsmanagement.app.utils.SearchUtils;
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
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
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
    public void newsIsAdded() throws Exception {
        News news = new News();
        news.setTitle("test1");
        news.setShortText("test1");
        news.setFullText("test1");
        news.setModificationDate(new Timestamp(new java.util.Date().getTime()));
        news.setCreationDate(new Timestamp(new java.util.Date().getTime()));
        news = newsRepository.save(news);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTable = actualDataSet.getTable("NEWS");

        assertEquals(4, newsTable.getRowCount());
        assertNotNull(news.getNewsId());
    }


    @Test
    public void newsIsNotAdded() throws Exception {
        final News news = new News();
        news.setModificationDate(new Date(new java.util.Date().getTime()));
        catchException(() -> newsRepository.save(news));
        assert caughtException() instanceof DataAccessException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTable = actualDataSet.getTable("NEWS");

        assertEquals(3, newsTable.getRowCount());
    }


    @Test
    public void newsIsFound() throws Exception {
        News news = newsRepository.findOne(1L);

        assertNotNull(news);
    }


    @Test
    public void newsIsNotFound() throws Exception {
        News news = newsRepository.findOne(-1L);

        assertNull(news);
    }


    @Test
    public void newsIsUpdated() throws Exception {
        News news = newsRepository.findOne(1L);
        news.setTitle("test1");
        news.setShortText("test1");
        news.setFullText("test1");
        newsRepository.save(news);
        News foundNews = newsRepository.findOne(news.getNewsId());

        assertEquals("test1", foundNews.getTitle());
        assertEquals("test1", foundNews.getShortText());
        assertEquals("test1", foundNews.getFullText());
    }


    @Test
    public void newsIsNotUpdated() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        news.setTitle(null);
        news.setModificationDate(new Date(new java.util.Date().getTime()));
        catchException(()->newsRepository.save(news));
        assert caughtException() instanceof DataAccessException;
        News foundNews = newsRepository.findOne(news.getNewsId());

        assertEquals("title1", foundNews.getTitle());
    }


    @Test
    public void newsIsDeleted() throws Exception {
        News news = newsRepository.findOne(1L);
        newsRepository.delete(news);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTable = actualDataSet.getTable("NEWS");
        ITable newsAuthorTable = actualDataSet.getTable("NEWS_AUTHOR");
        ITable newsTagTable = actualDataSet.getTable("NEWS_TAG");
        ITable commentTable = actualDataSet.getTable("COMMENTS");

        assertEquals(2, newsTable.getRowCount());
        assertEquals(2, newsAuthorTable.getRowCount());
        assertEquals(3, newsTagTable.getRowCount());
        assertEquals(3, commentTable.getRowCount());
    }


    @Test(expected = JpaSystemException.class)
    public void newsIsNotDeleted() throws Exception {
        News news = new News();
        news.setNewsId(-1L);
        newsRepository.delete(news);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable newsTable = actualDataSet.getTable("NEWS");
        ITable newsAuthorTable = actualDataSet.getTable("NEWS_AUTHOR");
        ITable newsTagTable = actualDataSet.getTable("NEWS_TAG");
        ITable commentTable = actualDataSet.getTable("COMMENTS");

        assertEquals(3, newsTable.getRowCount());
        assertEquals(3, newsAuthorTable.getRowCount());
        assertEquals(5, newsTagTable.getRowCount());
        assertEquals(5, commentTable.getRowCount());
    }


    @Test
    public void searchIsSuccessfulNoTags() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorIds = new LinkedList<>();
        authorIds.add(1L);
        searchCriteria.setAuthorIds(authorIds);
        String searchQuery = searchUtils.getSearchQuery(searchCriteria);
        List<News> foundNews = newsRepository.search(searchQuery);

        assertEquals(2L, foundNews.size());
        assertEquals("title3", foundNews.get(0).getTitle());
    }


    @Test
    public void searchIsSuccessfulNoAuthors() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> tagIds = new LinkedList<>();
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
    public void searchIsSuccessful() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorIds = new LinkedList<>();
        authorIds.add(1L);
        searchCriteria.setAuthorIds(authorIds);
        List<Long> tagIds = new LinkedList<>();
        tagIds.add(2L);
        searchCriteria.setTagIds(tagIds);
        String searchQuery = searchUtils.getSearchQuery(searchCriteria);
        List<News> foundNews = newsRepository.search(searchQuery);

        assertEquals(2L, foundNews.size());
        assertEquals("title3", foundNews.get(0).getTitle());
        assertEquals("title1", foundNews.get(1).getTitle());
    }


    @Test(expected = DataAccessException.class)
    public void searchNothingFoundInvalidSearchQuery() throws Exception {
        String searchQuery = "test";
        newsRepository.search(searchQuery);
    }


    @Test(expected = DaoException.class)
    public void searchNothingIsFoundSearchQueryIsNull() throws Exception {
        String searchQuery = null;
        newsRepository.search(searchQuery);
    }


    @Test
    public void searchNothingIsFound() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorIds = new LinkedList<>();
        authorIds.add(3L);
        searchCriteria.setAuthorIds(authorIds);
        String searchQuery = searchUtils.getSearchQuery(searchCriteria);
        List<News> foundNews = newsRepository.search(searchQuery);

        assertTrue(foundNews.isEmpty());
    }


    @Test
    public void foundAllNewsSorted() throws Exception {
        List<News> foundNews = newsRepository.findAllSorted();

        assertEquals(3L, foundNews.size());
        assertEquals("title3", foundNews.get(0).getTitle());
        assertEquals("title1", foundNews.get(1).getTitle());
        assertEquals("title2", foundNews.get(2).getTitle());
    }


    @Test
    public void countedAllNews() throws Exception {
        Long newsCount = newsRepository.countAll();

        assertEquals((Long) 3L, newsCount);
    }


    @Test
    public void countedPagesBySearchCriteria() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorsIds = new ArrayList<>();
        authorsIds.add(1L);
        List<Long> tagsIds = new ArrayList<>();
        tagsIds.add(1L);
        searchCriteria.setAuthorIds(authorsIds);
        searchCriteria.setTagIds(tagsIds);
        searchCriteria.setPageSize(1L);
        String COUNT_PAGES_BY_SEARCH_CRITERIA_QUERY = searchUtils.getCountQuery(searchCriteria);
        Long newsCount = newsRepository.countPagesBySearchCriteria(COUNT_PAGES_BY_SEARCH_CRITERIA_QUERY);

        assertEquals((Long) 2L, newsCount);
    }


    @Test
    public void gotRowNumberBySearchCriteria() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorsIds = new ArrayList<>();
        authorsIds.add(1L);
        List<Long> tagsIds = new ArrayList<>();
        tagsIds.add(1L);
        searchCriteria.setAuthorIds(authorsIds);
        searchCriteria.setTagIds(tagsIds);
        searchCriteria.setPageSize(1L);
        String ROW_NUMBER_BY_SEARCH_CRITERIA_QUERY = searchUtils.getRowNumberQuery(searchCriteria, 1L);
        Long rowNumber = newsRepository.rowNumberBySearchCriteria(ROW_NUMBER_BY_SEARCH_CRITERIA_QUERY);

        assertEquals((Long) 2L, rowNumber);
    }


    @Test(expected = DataAccessException.class)
    public void didNotGetRowNumberBySearchCriteria() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorsIds = new ArrayList<>();
        authorsIds.add(1L);
        List<Long> tagsIds = new ArrayList<>();
        tagsIds.add(1L);
        searchCriteria.setAuthorIds(authorsIds);
        searchCriteria.setTagIds(tagsIds);
        searchCriteria.setPageSize(1L);
        String ROW_NUMBER_BY_SEARCH_CRITERIA_QUERY = searchUtils.getRowNumberQuery(searchCriteria, 2L);
        newsRepository.rowNumberBySearchCriteria(ROW_NUMBER_BY_SEARCH_CRITERIA_QUERY);
    }
}
