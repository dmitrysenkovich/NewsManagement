package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.dao.AuthorRepository;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.service.impl.AuthorServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.RecoverableDataAccessException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
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
        doThrow(new RecoverableDataAccessException("")).when(authorRepository).save(any(Author.class));
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
        doThrow(new RecoverableDataAccessException("")).when(authorRepository).findOne(any(Long.class));
        authorService.find(1L);
    }


    @Test
    public void updated() throws Exception {
        when(authorRepository.save(any(Author.class))).thenReturn(new Author());
        authorService.update(new Author());
    }


    @Test(expected = ServiceException.class)
    public void didNotUpdate() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(authorRepository).save(any(Author.class));
        authorService.update(new Author());
    }


    @Test(expected = ServiceException.class)
    public void notDeleted() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(authorRepository).delete(any(Author.class));
        authorService.delete(new Author());
    }

    @Test
    public void authorIsMadeExpired() throws Exception {
        when(authorRepository.findOne(any(Long.class))).thenReturn(new Author());
        when(authorRepository.save(any(Author.class))).thenReturn(new Author());
        authorService.makeAuthorExpired(new Author());
    }


    @Test(expected = ServiceException.class)
    public void authorIsNotMadeExpired() throws Exception {
        when(authorRepository.findOne(any(Long.class))).thenReturn(new Author());
        doThrow(new RecoverableDataAccessException("")).when(authorRepository).save(any(Author.class));
        authorService.makeAuthorExpired(new Author());
    }

    @Test
    public void foundAllAuthorsByNews() throws Exception {
        when(authorRepository.findAllByNews(any(News.class))).thenReturn(new ArrayList<>());
        List<Author> allAuthorsByNews = authorService.findAllByNews(new News());
        assertNotNull(allAuthorsByNews);
    }


    @Test(expected = ServiceException.class)
    public void didNotFindAllAuthorsByNews() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(authorRepository).findAllByNews(any(News.class));
        authorService.findAllByNews(new News());
    }

    @Test
    public void foundAllAuthors() throws Exception {
        when(authorRepository.findAll()).thenReturn(new ArrayList<>());
        List<Author> allAuthors = authorService.findAll();
        assertNotNull(allAuthors);
    }


    @Test(expected = ServiceException.class)
    public void didNotFindAllAuthors() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(authorRepository).findAll();
        authorService.findAll();
    }

    @Test
    public void foundAllNotExpiredAuthors() throws Exception {
        when(authorRepository.findNotExpired()).thenReturn(new ArrayList<>());
        List<Author> notExpiredAuthors = authorService.findNotExpired();
        assertNotNull(notExpiredAuthors);
    }


    @Test(expected = ServiceException.class)
    public void didNotFindAllNotExpiredAuthors() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(authorRepository).findNotExpired();
        authorService.findNotExpired();
    }

    @Test
    public void checkedAuthorExistence() throws Exception {
        when(authorRepository.exists(any(String.class))).thenReturn(true);
        authorService.exists(new Author());
    }


    @Test(expected = ServiceException.class)
    public void didNotCheckAuthorExistence() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(authorRepository).exists(any(String.class));
        authorService.exists(new Author());
    }
}
