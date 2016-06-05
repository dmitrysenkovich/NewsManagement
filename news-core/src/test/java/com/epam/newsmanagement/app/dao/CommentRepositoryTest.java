package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.exception.DaoException;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        comment.setNewsId(2L);
        comment.setCommentText("test");
        comment.setCreationDate(new Timestamp(new java.util.Date().getTime()));
        Long commentId = commentRepository.add(comment);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("COMMENTS");

        assertEquals(6, commentsTable.getRowCount());
        assertNotNull(commentId);
    }


    @Test
    public void commentIsNotAddedInvalidField() throws Exception {
        Comment comment = new Comment();
        comment.setNewsId(1L);
        catchException(() -> commentRepository.add(comment));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("COMMENTS");

        assertEquals(5, commentsTable.getRowCount());
    }


    @Test
    public void commentIsNotAddedNewsAreInvalid() throws Exception {
        Comment comment = new Comment();
        comment.setNewsId(-1L);
        comment.setCommentText("test");
        catchException(() -> commentRepository.add(comment));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("COMMENTS");

        assertEquals(5, commentsTable.getRowCount());
    }


    @Test
    public void commentIsFound() throws Exception {
        Comment comment = commentRepository.find(1L);

        assertNotNull(comment);
    }

    @Test(expected = DaoException.class)
    public void commentIsNotFound() throws Exception {
        commentRepository.find(-1L);
    }


    @Test
    public void commentIsUpdated() throws Exception {
        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setNewsId(2L);
        comment.setCommentText("test1");
        commentRepository.update(comment);
        Comment foundComment = commentRepository.find(comment.getCommentId());

        assertEquals((Long) 1L, foundComment.getNewsId());
        assertEquals("test1", foundComment.getCommentText());
    }


    @Test
    public void commentIsNotUpdated() throws Exception {
        final Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setNewsId(1L);
        comment.setCommentText(null);
        catchException(() -> commentRepository.update(comment));
        assert caughtException() instanceof DaoException;
        Comment foundComment = commentRepository.find(comment.getCommentId());

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


    @Test
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
    public void allCommentsAreDeleted() throws Exception {
        Comment comment1 = new Comment();
        comment1.setCommentId(1L);
        Comment comment2 = new Comment();
        comment2.setCommentId(2L);
        List<Comment> comments = new LinkedList<>();
        comments.add(comment1);
        comments.add(comment2);
        commentRepository.deleteAll(comments);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("COMMENTS");

        assertEquals(3, commentsTable.getRowCount());
    }


    @Test
    public void notAllCommentsAreDeleted() throws Exception {
        Comment comment1 = new Comment();
        comment1.setCommentId(1L);
        Comment comment2 = new Comment();
        comment2.setCommentId(-1L);
        List<Comment> comments = new LinkedList<>();
        comments.add(comment1);
        comments.add(comment2);
        commentRepository.deleteAll(comments);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("COMMENTS");

        assertEquals(4, commentsTable.getRowCount());
    }


    @Test
    public void allCommentsAreNotDeleted() throws Exception {
        Comment comment1 = new Comment();
        comment1.setCommentId(-1L);
        Comment comment2 = new Comment();
        comment2.setCommentId(-1L);
        List<Comment> comments = new LinkedList<>();
        comments.add(comment1);
        comments.add(comment2);
        commentRepository.deleteAll(comments);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("COMMENTS");

        assertEquals(5, commentsTable.getRowCount());
    }


    @Test
    public void countedNewsComments() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        Long commentCount = commentRepository.countAllByNews(news);

        assertEquals((Long) 2L, commentCount);
    }


    @Test
    public void countedNonExistentNewsComments() throws Exception {
        News news = new News();
        news.setNewsId(4L);
        Long commentCount = commentRepository.countAllByNews(news);

        assertEquals((Long) 0L, commentCount);
    }


    @Test
    public void gotAllNewsComment() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        List<Comment> allComments = commentRepository.getAllByNews(news);

        assertEquals(2L, allComments.size());
    }


    @Test
    public void didNotGetAllNewsComments() throws Exception {
        News news = new News();
        news.setNewsId(4L);
        List<Comment> allComments = commentRepository.getAllByNews(news);

        assertEquals(0L, allComments.size());
    }
}
