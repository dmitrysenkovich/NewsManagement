package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.dao.CommentRepository;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.service.impl.CommentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.RecoverableDataAccessException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        when(commentRepository.save(comment)).thenReturn(comment);
        comment = commentService.add(news, comment);

        assertEquals((Long) 1L, comment.getCommentId());
        assertEquals((Long) 1L, comment.getNews().getNewsId());
    }


    @Test(expected = ServiceException.class)
    public void didNotAdd() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        doThrow(new RecoverableDataAccessException("")).when(commentRepository).save(any(Comment.class));
        commentService.add(news, new Comment());
    }


    @Test
    public void found() throws Exception {
        Comment comment = new Comment();
        comment.setCommentId(1L);
        when(commentRepository.findOne(1L)).thenReturn(comment);
        comment = commentService.find(1L);

        assertEquals((Long) 1L, comment.getCommentId());
    }


    @Test(expected = ServiceException.class)
    public void didNotFind() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(commentRepository).findOne(any(Long.class));
        commentService.find(1L);
    }


    @Test
    public void updated() throws Exception {
        when(commentRepository.save(any(Comment.class))).thenReturn(new Comment());
        commentService.update(new Comment());
    }


    @Test(expected = ServiceException.class)
    public void didNotUpdate() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(commentRepository).save(any(Comment.class));
        commentService.update(new Comment());
    }


    @Test
    public void deleted() throws Exception {
        doNothing().when(commentRepository).delete(any(Comment.class));
        commentService.delete(new Comment());
    }


    @Test(expected = ServiceException.class)
    public void didNotDelete() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(commentRepository).delete(any(Comment.class));
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
    public void didNotDeleteAll() throws Exception {
        List<Comment> comments = new LinkedList<>();
        comments.add(new Comment());
        comments.add(new Comment());
        doThrow(new RecoverableDataAccessException("")).when(commentRepository).deleteAll(any(List.class));
        commentService.deleteAll(comments);
    }


    @Test
    public void countedAllCommentsByNews() throws Exception {
        when(commentRepository.countAllByNews(any(News.class))).thenReturn(1L);
        Long count = commentService.countAllByNews(new News());

        assertEquals((Long) 1L, count);
    }


    @Test(expected = ServiceException.class)
    public void didNotCountAllCommentsByNews() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(commentRepository).countAllByNews(any(News.class));
        commentService.countAllByNews(new News());
    }


    @Test
    public void gotAllCommentsByNews() throws Exception {
        when(commentRepository.getAllByNews(any(News.class))).thenReturn(new ArrayList<>());
        List<Comment> allCommentsByNews = commentService.getAllByNews(new News());

        assertNotNull(allCommentsByNews);
    }


    @Test(expected = ServiceException.class)
    public void didNotGetAllCommentsByNews() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(commentRepository).getAllByNews(any(News.class));
        commentService.getAllByNews(new News());
    }
}
