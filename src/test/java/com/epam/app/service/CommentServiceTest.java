package com.epam.app.service;

import com.epam.app.dao.impl.CommentRepositoryImpl;
import com.epam.app.model.Comment;
import com.epam.app.model.News;
import com.epam.app.service.impl.CommentServiceImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Comment service test.
 */
public class CommentServiceTest {
    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepositoryImpl commentRepository;

    @Mock
    private Logger logger;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(CommentServiceImpl.class, "logger", logger);
    }

    @Test
    public void notAdded() {
        News news = new News();
        Comment comment = new Comment();
        when(commentRepository.add(comment)).thenReturn(comment);
        comment = commentService.add(news, comment);

        assertNull(comment.getCommentId());
        verify(logger).error(eq("Failed to add new comment"));
    }

    @Test
    public void added() {
        News news = new News();
        news.setNewsId(1L);
        Comment comment = new Comment();
        comment.setCommentId(1L);
        when(commentRepository.add(comment)).thenReturn(comment);
        comment = commentService.add(news, comment);

        assertEquals((Long) 1L, comment.getCommentId());
        assertEquals((Long) 1L, comment.getNewsId());
        verify(logger).info(eq("Successfully added new comment"));
    }

    @Test
    public void notFound() {
        when(commentRepository.find(1L)).thenReturn(null);
        Comment comment = commentService.find(1L);

        assertNull(comment);
        verify(logger).error(eq("Failed to find comment"));
    }

    @Test
    public void found() {
        Comment comment = new Comment();
        comment.setCommentId(1L);
        when(commentRepository.find(1L)).thenReturn(comment);
        comment = commentService.find(1L);

        assertEquals((Long) 1L, comment.getCommentId());
        verify(logger).info(eq("Successfully found comment"));
    }

    @Test
    public void notUpdated() {
        when(commentRepository.update(null)).thenReturn(false);
        boolean updated = commentService.update(null);

        assertFalse(updated);
        verify(logger).error(eq("Failed to update comment"));
    }

    @Test
    public void updated() {
        Comment comment = new Comment();
        comment.setCommentId(1L);
        when(commentRepository.update(comment)).thenReturn(true);
        boolean updated = commentService.update(comment);

        assertTrue(updated);
        verify(logger).info(eq("Successfully updated comment"));
    }

    @Test
    public void notDeleted() {
        when(commentRepository.delete(null)).thenReturn(false);
        boolean deleted = commentService.delete(null);

        assertFalse(deleted);
        verify(logger).error(eq("Failed to delete comment"));
    }

    @Test
    public void deleted() {
        Comment comment = new Comment();
        comment.setCommentId(1L);
        when(commentRepository.delete(comment)).thenReturn(true);
        boolean deleted = commentService.delete(comment);

        assertTrue(deleted);
        verify(logger).info(eq("Successfully deleted comment"));
    }

    @Test
    public void notAddedAll() {
        News news = new News();
        List<Comment> comments = new LinkedList<Comment>();
        comments.add(new Comment());
        comments.add(new Comment());
        when(commentRepository.addAll(comments)).thenReturn(comments);
        commentService.addAll(news, comments);

        verify(logger).error(eq("Failed to add comments"));
    }

    @Test
    public void addedAll() {
        News news = new News();
        news.setNewsId(1L);
        List<Comment> comments = new LinkedList<Comment>();
        Comment comment1 = new Comment();
        comment1.setCommentId(1L);
        comments.add(comment1);
        Comment comment2 = new Comment();
        comment2.setCommentId(1L);
        comments.add(comment2);
        when(commentRepository.addAll(comments)).thenReturn(comments);
        List<Comment> returnedComments = commentService.addAll(news, comments);

        for (Comment comment : returnedComments)
            assertEquals((Long) 1L, comment.getNewsId());
        verify(logger).info(eq("Successfully added comments"));
    }

    @Test
    public void notDeletedAll() {
        List<Comment> comments = new LinkedList<Comment>();
        comments.add(new Comment());
        comments.add(new Comment());
        when(commentRepository.deleteAll(comments)).thenReturn(false);
        boolean deletedAll = commentService.deleteAll(comments);

        assertFalse(deletedAll);
        verify(logger).error(eq("Failed to delete comments"));
    }

    @Test
    public void deletedAll() {
        List<Comment> comments = new LinkedList<Comment>();
        comments.add(new Comment());
        comments.add(new Comment());
        when(commentRepository.deleteAll(comments)).thenReturn(true);
        boolean deletedAll = commentService.deleteAll(comments);

        assertTrue(deletedAll);
        verify(logger).info(eq("Successfully deleted comments"));
    }
}
