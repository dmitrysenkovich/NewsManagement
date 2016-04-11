package com.epam.app.service;

import com.epam.app.dao.CommentRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.exception.ServiceException;
import com.epam.app.model.Comment;
import com.epam.app.model.News;
import com.epam.app.service.impl.CommentServiceImpl;
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
        when(commentRepository.add(comment)).thenReturn(comment);
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
    public void addedAll() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        List<Comment> comments = new LinkedList<>();
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
    }


    @Test(expected = ServiceException.class)
    public void notAddedAll() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        List<Comment> comments = new LinkedList<>();
        comments.add(new Comment());
        comments.add(new Comment());
        doThrow(new DaoException()).when(commentRepository).addAll(any(List.class));
        commentService.addAll(news, comments);
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
