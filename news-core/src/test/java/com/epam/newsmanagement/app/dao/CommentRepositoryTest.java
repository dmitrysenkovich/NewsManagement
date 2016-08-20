package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.Comment;
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
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Comment repository test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/repository-test-context.xml"})
@DatabaseSetup("/META-INF/test-data.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@PropertySource("classpath:properties/test-database.properties")
@Transactional(TransactionMode.ROLLBACK)
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private NewsRepository newsRepository;

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
    public void commentIsAdded() throws Exception {
        Comment comment = new Comment();
        News news = newsRepository.findOne(2L);
        comment.setNews(news);
        comment.setCommentText("test");
        comment.setCreationDate(new Timestamp(new java.util.Date().getTime()));
        comment = commentRepository.save(comment);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("COMMENTS");

        assertEquals(6, commentsTable.getRowCount());
        assertNotNull(comment.getCommentId());
    }


    @Test
    public void commentIsNotAddedInvalidField() throws Exception {
        Comment comment = new Comment();
        News news = new News();
        news.setNewsId(1L);
        comment.setNews(news);
        catchException(() -> commentRepository.save(comment));
        assert caughtException() instanceof DataAccessException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("COMMENTS");

        assertEquals(5, commentsTable.getRowCount());
    }


    @Test
    public void commentIsNotAddedNewsAreInvalid() throws Exception {
        Comment comment = new Comment();
        News news = new News();
        news.setNewsId(-1L);
        comment.setNews(news);
        comment.setCommentText("test");
        catchException(() -> commentRepository.save(comment));
        assert caughtException() instanceof DataAccessException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("COMMENTS");

        assertEquals(5, commentsTable.getRowCount());
    }


    @Test
    public void commentIsFound() throws Exception {
        Comment comment = commentRepository.findOne(1L);

        assertNotNull(comment);
    }

    @Test
    public void commentIsNotFound() throws Exception {
        Comment comment = commentRepository.findOne(-1L);

        assertNull(comment);
    }


    @Test
    public void commentIsUpdated() throws Exception {
        Comment comment = new Comment();
        comment.setCommentId(1L);
        News news = newsRepository.findOne(2L);
        comment.setNews(news);
        comment.setCommentText("test1");
        comment.setCreationDate(new Timestamp(new Date().getTime()));
        commentRepository.save(comment);
        Comment foundComment = commentRepository.findOne(comment.getCommentId());

        assertEquals("test1", foundComment.getCommentText());
    }


    @Test
    public void commentIsNotUpdated() throws Exception {
        final Comment comment = new Comment();
        comment.setCommentId(1L);
        News news = new News();
        news.setNewsId(1L);
        comment.setNews(news);
        comment.setCommentText(null);
        catchException(() -> commentRepository.save(comment));
        assert caughtException() instanceof DataAccessException;
        Comment foundComment = commentRepository.findOne(comment.getCommentId());

        assertEquals("test", foundComment.getCommentText());
    }


    @Test
    public void commentIsDeleted() throws Exception {
        Comment comment = new Comment();
        comment.setCommentId(1L);
        commentRepository.delete(comment);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("COMMENTS");

        assertEquals(4, commentsTable.getRowCount());
    }


    @Test(expected = DataAccessException.class)
    public void commentIsNotDeleted() throws Exception {
        Comment comment = new Comment();
        comment.setCommentId(-1L);
        commentRepository.delete(comment);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("COMMENTS");

        assertEquals(5, commentsTable.getRowCount());
    }


    @Test
    public void countedNewsComments() throws Exception {
        News news = newsRepository.findOne(1L);
        Long commentCount = commentRepository.countAllByNews(news);

        assertEquals((Long) 2L, commentCount);
    }


    @Test
    public void countedNonExistentNewsComments() throws Exception {
        News news = newsRepository.findOne(4L);
        Long commentCount = commentRepository.countAllByNews(news);

        assertEquals((Long) 0L, commentCount);
    }


    @Test
    public void foundAllNewsComment() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        List<Comment> allComments = commentRepository.findAllByNews(news);

        assertEquals(2L, allComments.size());
    }


    @Test
    public void didNotFoundAllNewsComments() throws Exception {
        News news = new News();
        news.setNewsId(4L);
        List<Comment> allComments = commentRepository.findAllByNews(news);

        assertEquals(0L, allComments.size());
    }
}
