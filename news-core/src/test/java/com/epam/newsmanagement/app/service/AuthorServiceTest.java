package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.dao.AuthorRepository;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.service.impl.AuthorServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Author service test.
 */
public class AuthorServiceTest {
    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    private AuthorRepository authorRepository;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void added() throws Exception {
        Author author = new Author();
        author.setAuthorId(1L);
        when(authorRepository.save(author)).thenReturn(author);
        author = authorService.add(author);

        assertEquals((Long) 1L, author.getAuthorId());
    }


    @Test(expected = ServiceException.class)
    public void didNotAdd() throws Exception {
        doThrow(new DaoException()).when(authorRepository).save(any(Author.class));
        authorService.add(new Author());
    }


    @Test
    public void found() throws Exception {
        Author author = new Author();
        author.setAuthorId(1L);
        when(authorRepository.findOne(1L)).thenReturn(author);
        author = authorService.find(1L);

        assertEquals((Long) 1L, author.getAuthorId());
    }


    @Test(expected = ServiceException.class)
    public void didNotFind() throws Exception {
        doThrow(new DaoException()).when(authorRepository).findOne(any(Long.class));
        authorService.find(1L);
    }


    @Test
    public void updated() throws Exception {
        doNothing().when(authorRepository).save(any(Author.class));
        authorService.update(new Author());
    }


    @Test(expected = ServiceException.class)
    public void didNotUpdate() throws Exception {
        doThrow(new DaoException()).when(authorRepository).save(any(Author.class));
        authorService.update(new Author());
    }


    @Test(expected = ServiceException.class)
    public void notDeleted() throws Exception {
        doThrow(new DaoException()).when(authorRepository).delete(any(Author.class));
        authorService.delete(new Author());
    }

    @Test
    public void authorIsMadeExpired() throws Exception {
        doNothing().when(authorRepository).save(any(Author.class));
        authorService.makeAuthorExpired(new Author());
    }


    @Test(expected = ServiceException.class)
    public void authorIsNotMadeExpired() throws Exception {
        doThrow(new DaoException()).when(authorRepository).save(any(Author.class));
        authorService.makeAuthorExpired(new Author());
    }

    @Test
    public void gotAllAuthorsByNews() throws Exception {
        when(authorRepository.getAllByNews(any(News.class))).thenReturn(new ArrayList<>());
        List<Author> allAuthorsByNews = authorService.getAllByNews(new News());
        assertNotNull(allAuthorsByNews);
    }


    @Test(expected = ServiceException.class)
    public void didNotGetAllAuthorsByNews() throws Exception {
        doThrow(new DaoException()).when(authorRepository).getAllByNews(any(News.class));
        authorService.getAllByNews(new News());
    }

    @Test
    public void gotAllAuthors() throws Exception {
        when(authorRepository.findAll()).thenReturn(new ArrayList<>());
        List<Author> allAuthors = authorService.getAll();
        assertNotNull(allAuthors);
    }


    @Test(expected = ServiceException.class)
    public void didNotGetAllAuthors() throws Exception {
        doThrow(new DaoException()).when(authorRepository).findAll();
        authorService.getAll();
    }

    @Test
    public void gotAllNotExpiredAuthors() throws Exception {
        when(authorRepository.getNotExpired()).thenReturn(new ArrayList<>());
        List<Author> notExpiredAuthors = authorService.getNotExpired();
        assertNotNull(notExpiredAuthors);
    }


    @Test(expected = ServiceException.class)
    public void didNotGetAllNotExpiredAuthors() throws Exception {
        doThrow(new DaoException()).when(authorRepository).getNotExpired();
        authorService.getNotExpired();
    }

    @Test
    public void checkedAuthorExistence() throws Exception {
        when(authorRepository.exists(any(String.class))).thenReturn(true);
        authorService.exists(new Author());
    }


    @Test(expected = ServiceException.class)
    public void didNotCheckAuthorExistence() throws Exception {
        doThrow(new DaoException()).when(authorRepository).exists(any(String.class));
        authorService.exists(new Author());
    }
}
