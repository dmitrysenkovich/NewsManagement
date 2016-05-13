package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.dao.CommentRepository;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.service.impl.CommentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Comment service test.
 */
public class CommentServiceTest {
    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void added() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        Comment comment = new Comment();
        comment.setCommentId(1L);
        when(commentRepository.add(comment)).thenReturn(1L);
        comment = commentService.add(news, comment);

        assertEquals((Long) 1L, comment.getCommentId());
        assertEquals((Long) 1L, comment.getNewsId());
    }


    @Test(expected = ServiceException.class)
    public void notAdded() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        doThrow(new DaoException()).when(commentRepository).add(any(Comment.class));
        commentService.add(news, new Comment());
    }


    @Test
    public void found() throws Exception {
        Comment comment = new Comment();
        comment.setCommentId(1L);
        when(commentRepository.find(1L)).thenReturn(comment);
        comment = commentService.find(1L);

        assertEquals((Long) 1L, comment.getCommentId());
    }


    @Test(expected = ServiceException.class)
    public void notFound() throws Exception {
        doThrow(new DaoException()).when(commentRepository).find(any(Long.class));
        commentService.find(1L);
    }


    @Test
    public void updated() throws Exception {
        doNothing().when(commentRepository).update(any(Comment.class));
        commentService.update(new Comment());
    }


    @Test(expected = ServiceException.class)
    public void notUpdated() throws Exception {
        doThrow(new DaoException()).when(commentRepository).update(any(Comment.class));
        commentService.update(new Comment());
    }


    @Test
    public void deleted() throws Exception {
        doNothing().when(commentRepository).delete(any(Comment.class));
        commentService.delete(new Comment());
    }


    @Test(expected = ServiceException.class)
    public void notDeleted() throws Exception {
        doThrow(new DaoException()).when(commentRepository).delete(any(Comment.class));
        commentService.delete(new Comment());
    }


    @Test
    public void deletedAll() throws Exception {
        List<Comment> comments = new LinkedList<>();
        comments.add(new Comment());
        comments.add(new Comment());
        doNothing().when(commentRepository).deleteAll(any(List.class));
        commentService.deleteAll(comments);
    }


    @Test(expected = ServiceException.class)
    public void notDeletedAll() throws Exception {
        List<Comment> comments = new LinkedList<>();
        comments.add(new Comment());
        comments.add(new Comment());
        doThrow(new DaoException()).when(commentRepository).deleteAll(any(List.class));
        commentService.deleteAll(comments);
    }
}