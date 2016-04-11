package com.epam.app.dao;

import com.epam.app.exception.DaoException;
import com.epam.app.model.Comment;
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
    public void commentAdded() throws Exception {
        Comment comment = new Comment();
        comment.setNewsId(1L);
        comment.setCommentText("test");
        comment = commentRepository.add(comment);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("Comments");

        assertEquals(6, commentsTable.getRowCount());
        assertNotNull(comment.getCommentId());
    }


    @Test
    public void commentNotAddedInvalidField() throws Exception {
        Comment comment = new Comment();
        comment.setNewsId(1L);
        catchException(() -> commentRepository.add(comment));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("Comments");

        assertEquals(5, commentsTable.getRowCount());
        assertNull(comment.getCommentId());
    }


    @Test
    public void commentNotAddedNewsAreInvalid() throws Exception {
        Comment comment = new Comment();
        comment.setNewsId(-1L);
        comment.setCommentText("test");
        catchException(() -> commentRepository.add(comment));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("Comments");

        assertEquals(5, commentsTable.getRowCount());
        assertNull(comment.getCommentId());
    }


    @Test
    public void commentFound() throws Exception {
        Comment comment = commentRepository.find(1L);

        assertNotNull(comment);
    }

    @Test(expected = DaoException.class)
    public void commentNotFound() throws Exception {
        commentRepository.find(-1L);
    }


    @Test
    public void commentUpdated() throws Exception {
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
    public void commentNotUpdated() throws Exception {
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
    public void commentDeleted() throws Exception {
        Comment comment = new Comment();
        comment.setCommentId(1L);
        commentRepository.delete(comment);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("Comments");

        assertEquals(4, commentsTable.getRowCount());
    }


    @Test
    public void commentNotDeleted() throws Exception {
        Comment comment = new Comment();
        comment.setCommentId(-1L);
        commentRepository.delete(comment);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("Comments");

        assertEquals(5, commentsTable.getRowCount());
    }


    @Test
    public void allCommentsAdded() throws Exception {
        Comment comment1 = new Comment();
        comment1.setNewsId(1L);
        comment1.setCommentText("test");
        Comment comment2 = new Comment();
        comment2.setNewsId(1L);
        comment2.setCommentText("test");
        List<Comment> comments = new LinkedList<>();
        comments.add(comment1);
        comments.add(comment2);
        comments = commentRepository.addAll(comments);
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("Comments");

        assertEquals(7, commentsTable.getRowCount());
        for (Comment comment : comments)
            assertNotNull(comment.getCommentId());
    }


    @Test
    public void notAllCommentsAdded() throws Exception {
        Comment comment1 = new Comment();
        comment1.setNewsId(1L);
        comment1.setCommentText("test");
        Comment comment2 = new Comment();
        comment2.setNewsId(1L);
        List<Comment> comments = new LinkedList<>();
        comments.add(comment1);
        comments.add(comment2);
        catchException(() -> commentRepository.addAll(comments));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("Comments");

        assertEquals(6, commentsTable.getRowCount());
        for (Comment comment : comments)
            assertNull(comment.getCommentId());
    }


    @Test
    public void allCommentsNotAdded() throws Exception {
        Comment comment1 = new Comment();
        comment1.setNewsId(1L);
        Comment comment2 = new Comment();
        comment2.setNewsId(1L);
        List<Comment> comments = new LinkedList<>();
        comments.add(comment1);
        comments.add(comment2);
        catchException(() -> commentRepository.addAll(comments));
        assert caughtException() instanceof DaoException;
        connection = DriverManager.getConnection(testDbUrl, testDbUsername, testDbPassword);
        IDataSet actualDataSet = getActualDataSet(connection);
        ITable commentsTable = actualDataSet.getTable("Comments");

        assertEquals(5, commentsTable.getRowCount());
        for (Comment comment : comments)
            assertNull(comment.getCommentId());
    }


    @Test
    public void allCommentsDeleted() throws Exception {
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
        ITable commentsTable = actualDataSet.getTable("Comments");

        assertEquals(3, commentsTable.getRowCount());
    }


    @Test
    public void notAllCommentsDeleted() throws Exception {
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
        ITable commentsTable = actualDataSet.getTable("Comments");

        assertEquals(4, commentsTable.getRowCount());
    }


    @Test
    public void allCommentsNotDeleted() throws Exception {
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
        ITable commentsTable = actualDataSet.getTable("Comments");

        assertEquals(5, commentsTable.getRowCount());
    }
}
